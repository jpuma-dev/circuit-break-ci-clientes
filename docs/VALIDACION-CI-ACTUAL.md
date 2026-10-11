# Validación de CI del listado de clientes

Fecha: 11 de octubre de 2026, America/Bogota.

## Resultado local comprobado

- Maven 3.9.12 y JDK 21.0.8.
- Módulo: `services/clientes-service`.
- Ciclo ejecutado: `clean verify`, con `-DfailIfNoTests=true`.
- **BUILD SUCCESS**.
- **Tests run: 5, Failures: 0, Errors: 0, Skipped: 0**.
- JAR Spring Boot generado: `clientes-service-0.0.1-SNAPSHOT.jar`.

Se ejecutó el Maven 3.9.12 ya instalado por el wrapper. El proceso requirió
acceso fuera del sandbox para resolver dependencias y ejecutar las pruebas.
La ejecución exitosa no necesitó un `argLine` local ni omitir pruebas.
Los informes originales están en `services/clientes-service/target/surefire-reports/`.
`target/` está excluido de Git; Actions generará sus propios informes.

## Correcciones realizadas

- README con arquitectura, alcance, requisitos, ejecución y pruebas.
- `.gitignore` excluye `.env`, claves y archivos de IDE adicionales.
- `.gitattributes` conserva el wrapper Linux con finales de línea LF.
- Pull requests de CI dirigidas a `main`, manteniendo el filtro del módulo.
- `verify` ejecuta las pruebas una sola vez, después de la compilación.
- Guía de publicación pública y protocolo para fallo controlado y corrección.
- Repositorio Git local inicializado en `main`.

## Evidencia en la nube

**Pendiente.** No se ha publicado un repositorio ni ejecutado Actions durante
esta validación. Se requiere conectar GitHub y seleccionar la cuenta de destino.
Consultar `DEMOSTRACION-CI.md` para el registro de runs reales.

La validación corresponde a CI de Clientes. La conversión a Hospital Vida Salud
con WebFlux y R2DBC queda fuera del alcance aclarado de esta entrega.
