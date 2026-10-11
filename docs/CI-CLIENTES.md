# Integración continua: Listado de clientes

## Alcance

La tubería `.github/workflows/ci-clientes.yml` construye exclusivamente
`services/clientes-service`, el servicio existente que expone `GET /api/clientes`.
Se conserva la estructura de Circuit Break y no se modifican otros servicios.
Este módulo tiene su propio POM y no depende del POM agregador; por eso se
usa `-f services/clientes-service/pom.xml` en lugar de construir todos los módulos.

## Activación

- Push en cualquier rama con cambios en `services/clientes-service/**`.
- Pull request hacia `main`, abierto, actualizado o reabierto con cambios en ese módulo.
- También se ejecuta si cambia su workflow, reusable-java-ci.yml o Maven Wrapper (`.mvn/**`,
  `mvnw`, `mvnw.cmd`).
- Ejecución manual mediante **Actions > CI - Listado de clientes > Run workflow**,
  cuando el workflow ya exista en la rama predeterminada.

Los cambios exclusivos en otros servicios no disparan esta CI. En una PR,
el filtro considera el conjunto de archivos cambiados respecto de la rama base.

## Etapas

1. Checkout del repositorio con permiso de lectura.
2. Configuración de Temurin Java 21 y caché de dependencias Maven.
3. Maven Wrapper descarga y usa Maven 3.9.12, según la configuración existente.
4. `clean compile`: valida y compila el código de clientes.
5. `verify`: ejecuta JUnit una sola vez, falla si no encuentra pruebas, empaqueta
   y verifica el JAR Spring Boot.
6. Guarda informes Surefire, incluso si fallan las pruebas, y el JAR solamente
   cuando la verificación es exitosa. Los artefactos se conservan 14 días.

Un error detiene las etapas dependientes y marca el job en rojo. No hay despliegue.
El límite es 15 minutos; una nueva ejecución en la misma rama cancela la anterior.

## Pruebas del listado

`ClientesIntegrationTest` carga Spring Boot, MockMvc y la persistencia JPA real
con H2 en memoria. Eureka queda deshabilitado y no se necesita Docker, Gateway,
RabbitMQ ni otro servicio. La base se limpia antes de cada prueba.

- Sin clientes: GET devuelve HTTP 200, JSON y un arreglo vacío.
- Con dos clientes creados por POST: GET devuelve HTTP 200 y ambos registros,
  con identificadores positivos, nombres, DNI, teléfonos y correos correctos.
  Las aserciones no dependen del orden de la lista.
- Se conservan las pruebas de persistencia, DNI duplicado, DNI inválido y
  rechazo de campos desconocidos.

## Publicar y comprobar en GitHub

La carpeta que contiene `pom.xml`, `services/clientes-service` y `.github` debe ser
la raíz del repositorio. No subas la carpeta contenedora `Circuit-Break-CI-Nexo`
como un nivel adicional: GitHub busca workflows en `.github/workflows`
de la raíz. No subas `target/`, `data/` ni credenciales.

1. Crea un repositorio GitHub público llamado `circuit-break-ci-clientes`, conforme
   al alcance solicitado, y sube el contenido conservando los archivos ocultos.
2. En **Actions**, abre **CI - Listado de clientes** y selecciona la ejecución
   correspondiente al commit.
3. Abre **Compilar, probar y validar clientes** y revisa cada etapa.
4. En el registro de Maven deben aparecer `Tests run: 5`, cero fallos y
   `BUILD SUCCESS`. En el resumen descarga `clientes-service-resultados-pruebas` y
   `clientes-service-jar`.
5. Crea una rama, cambia una prueba del módulo y abre una PR para comprobar
   también el disparador `pull_request`. Una aserción incorrecta debe producir
   un fallo; restáurala para recuperar el estado verde.

Si Actions está deshabilitado, habilítalo en **Settings > Actions > General**.
La disponibilidad y el consumo de minutos dependen de la cuenta GitHub.
Un filtro por rutas puede dejar un check requerido pendiente en PR que no toca
clientes; tenlo en cuenta antes de exigir este check para todas las PR.

## Reproducir localmente

Desde la raíz del proyecto, con Java 21:

```powershell
.\mvnw.cmd -B -ntp -f services/clientes-service/pom.xml -DfailIfNoTests=true clean verify
```

En Linux:

```sh
sh ./mvnw -B -ntp -f services/clientes-service/pom.xml -DfailIfNoTests=true clean verify
```

## Referencias

- Java y caché Maven: https://github.com/actions/setup-java
- Checkout: https://github.com/actions/checkout
- Artefactos: https://github.com/actions/upload-artifact
