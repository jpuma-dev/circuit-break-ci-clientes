# Taxi Seguro S.A.C.

## Arquitectura y CI de Clientes

Los módulos están organizados bajo `services/`. Clientes sigue las capas
`domain`, `application`, `infrastructure` y `presentation`, tomando como
referencia Nexo Ecommerce. La CI valida solamente Clientes mediante un workflow
de servicio y uno Java reutilizable.

- [Comparación de arquitectura con Nexo](docs/COMPARACION-NEXO.md)
- [Funcionamiento y verificación de la CI](docs/CI-CLIENTES.md)

Para comprobar sólo Clientes con Java 21:

```powershell
.\mvnw.cmd -B -ntp -f services/clientes-service/pom.xml -DfailIfNoTests=true clean verify
```

Proyecto con Java 21, Spring Boot 3.5.6 y Spring Cloud 2025.0.0, organizado en seis módulos. `CircuitBreak` ejecuta el servicio de viajes. Incluye reservas, reintentos y publicación de eventos.

## Ejecutar todo con Docker Compose

Instala e inicia Docker Desktop con contenedores Linux. Desde esta carpeta:

```powershell
docker compose up --build -d
docker compose ps
```

La primera ejecución descarga dependencias e imágenes. Espera a que los siete componentes estén saludables; el descubrimiento de servicios puede tardar unos segundos adicionales.

- Eureka: http://localhost:8761
- API Gateway: http://localhost:8080
- RabbitMQ: http://localhost:15672 (usuario `taxi`, contraseña local `taxi_local`).

Los servicios de negocio sólo se exponen a través del Gateway. Si tienes otra versión funcionando en esos puertos, detén esa versión o copia `.env.example` a `.env` y cambia los puertos publicados. Para revisar y detener:

```powershell
docker compose logs --tail=100 viajes-service
docker compose down
```

## Datos y organización

| Modelo | Campos |
|---|---|
| Cliente | id, nombre, dni, telefono, email |
| Taxi | id, placa, modelo, conductor, estado |
| Viaje | id, clienteId, taxiId, origen, destino, estado, fechaSolicitud |

Los identificadores de negocio son números generados. DNI tiene ocho dígitos y es único; placa también es única. Estado de taxi: `DISPONIBLE`, `EN_SERVICIO`, `MANTENIMIENTO`. Estado de viaje: `PENDIENTE`, `ASIGNADO`, `SIN_TAXI_DISPONIBLE`, `COMPLETADO`, `CANCELADO`.

Cada servicio de negocio usa JPA y su propia H2 **en disco**, dentro de `/app/data` al ejecutar Docker. Clientes, taxis, viajes y notificaciones tienen volúmenes separados. Los registros, reservas, claves de solicitud y eventos pendientes se conservan al reiniciar o recrear los contenedores. Hibernate usa `update` para no eliminar las tablas al cerrar. RabbitMQ también conserva su cola en un volumen.

No hay registros precargados. Las pruebas automatizadas usan H2 en memoria para no modificar las bases de la aplicación. Fuera de Docker, las bases se crean en `./data`, relativo a la carpeta desde donde inicias cada servicio; ejecuta cada uno desde su módulo. La variable `DB_URL` permite cambiar esa ubicación.

`docker compose down` conserva los datos; **`docker compose down -v` elimina permanentemente todos los volúmenes del proyecto**. Los datos de una ejecución anterior en memoria no se recuperan automáticamente. Mantén una sola instancia por servicio con esta configuración H2.

Para comprobar persistencia, registra tus datos siguiendo el ejemplo de abajo y consulta sus identificadores. Ejecuta `docker compose restart`, espera a que los servicios estén saludables y vuelve a consultar esos mismos identificadores. Deben conservar sus valores. También puedes repetir con `docker compose down` y `docker compose up -d`, sin la opción `-v`.

- `domain`: modelos, estados y errores de negocio, sin dependencias de Spring.
- `application/usecases`: operaciones de negocio.
- `application/ports`: contratos de almacenamiento, comunicación y eventos.
- `adapters`: HTTP, JPA/H2, OpenFeign y RabbitMQ.
- `configuration`: construcción de casos de uso y configuración técnica.

Las tablas internas de claves de solicitud, reservas y eventos pendientes sirven para evitar duplicados y reintentar envíos; no agregan campos a Cliente, Taxi o Viaje. Notificaciones almacena el evento recibido y su identificador técnico.

## Probar desde PowerShell

Comprobación inicial, sin crear datos:

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
Invoke-RestMethod http://localhost:8080/api/clientes
Invoke-RestMethod http://localhost:8080/api/taxis
Invoke-RestMethod http://localhost:8080/api/viajes
```

Para probar el flujo con tus propios valores, ejecuta estas instrucciones en la misma consola. Los datos se solicitan por teclado:

```powershell
$base = 'http://localhost:8080'
$clienteBody = @{
  nombre = Read-Host 'Nombre'
  dni = Read-Host 'DNI de ocho digitos'
  telefono = Read-Host 'Telefono'
  email = Read-Host 'Correo'
} | ConvertTo-Json
$cliente = Invoke-RestMethod "$base/api/clientes" -Method Post -ContentType 'application/json' -Body ([Text.Encoding]::UTF8.GetBytes($clienteBody))
$taxiBody = @{
  placa = Read-Host 'Placa'
  modelo = Read-Host 'Modelo'
  conductor = Read-Host 'Conductor'
  estado = 'DISPONIBLE'
} | ConvertTo-Json
$taxi = Invoke-RestMethod "$base/api/taxis" -Method Post -ContentType 'application/json' -Body ([Text.Encoding]::UTF8.GetBytes($taxiBody))
$viajeBody = @{
  clienteId = $cliente.id
  origen = Read-Host 'Origen'
  destino = Read-Host 'Destino'
} | ConvertTo-Json
$clave = [guid]::NewGuid().ToString()
$viaje = Invoke-RestMethod "$base/api/viajes" -Method Post -Headers @{'Idempotency-Key'=$clave} -ContentType 'application/json' -Body ([Text.Encoding]::UTF8.GetBytes($viajeBody))
$viaje
Invoke-RestMethod "$base/api/viajes/$($viaje.id)/completar" -Method Post
Invoke-RestMethod "$base/api/notificaciones?viajeId=$($viaje.id)"
```

La entrega de eventos es asíncrona; repite la última consulta unos segundos después. Repetir la solicitud con la misma clave y el mismo cuerpo devuelve el viaje existente; cambiar el cuerpo con esa clave devuelve 409.

## Operaciones de la API

| Método | Ruta | Uso |
|---|---|---|
| GET / POST | /api/clientes | Listar / crear |
| GET / PUT | /api/clientes/{id} | Consultar / actualizar |
| GET / POST | /api/taxis | Listar / crear |
| GET | /api/taxis?estado=DISPONIBLE | Filtrar |
| GET | /api/taxis/{id} | Consultar |
| PATCH | /api/taxis/{id}/estado | Cuerpo con `estado` |
| GET / POST | /api/viajes | Listar / solicitar |
| GET | /api/viajes/{id} | Consultar |
| POST | /api/viajes/{id}/reintentar | Reintentar asignación |
| POST | /api/viajes/{id}/completar | Liberar taxi y completar |
| POST | /api/viajes/{id}/cancelar | Liberar taxi y cancelar |
| GET | /api/viajes/eventos/pendientes | Contar eventos por enviar |
| GET | /api/notificaciones | Eventos recibidos; filtro opcional `viajeId` |

Los cuerpos de creación contienen los campos del modelo salvo `id`; en viajes sólo se envían `clienteId`, `origen` y `destino`. Se rechazan propiedades desconocidas.

## Circuit Breaker y mensajería

OpenFeign encuentra clientes y taxis mediante Eureka. Resilience4j usa circuitos separados: cuatro llamadas mínimas, 50 % de fallos, diez segundos abierto y dos llamadas de recuperación. Cliente inexistente y falta de taxis son resultados de negocio, no fallos técnicos del circuito. Una indisponibilidad deja el viaje pendiente; `/reintentar` intenta asignarlo de nuevo. Sin taxis, queda `SIN_TAXI_DISPONIBLE`.

Para observarlo, detén `taxis-service`, solicita varios viajes con claves distintas, vuelve a iniciar el servicio y reintenta tras diez segundos. Los taxis registrados se conservan; debe existir al menos uno disponible. La reserva se identifica por viaje para evitar dos asignaciones en un reintento. Esta versión de laboratorio está diseñada para una instancia de cada servicio.

Los cambios de estado y sus eventos se guardan en la misma transacción H2. El publicador conserva eventos pendientes hasta recibir confirmación de RabbitMQ; notificaciones descarta duplicados por identificador de evento. Puedes detener y reiniciar RabbitMQ para observar el reenvío. Los eventos pendientes sobreviven al reinicio del servicio de viajes gracias a su volumen. Los volúmenes no sustituyen una copia de seguridad ante pérdida del disco.

## Compilar y ejecutar pruebas

Con JDK 21 instalado, desde esta carpeta:

```powershell
.\mvnw.cmd verify
```

Las pruebas usan H2 real y simulan las comunicaciones externas; no requieren Docker ni un broker. Sus registros de prueba están limitados a `src/test` y no se cargan al ejecutar el sistema.
