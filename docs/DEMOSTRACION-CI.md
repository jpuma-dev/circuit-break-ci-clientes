# Demostración real de integración continua

## Alcance

Solo `services/clientes-service`, con pruebas del listado de clientes. Se requiere
un repositorio público GitHub cuya raíz contenga `.github`, `services` y `mvnw`.

## Secuencia de evidencias

1. Subir la versión correcta a `main`. Esperar el estado **success** del workflow
   **CI - Listado de clientes**. Guardar el enlace del run, commit y logs.
2. Crear la rama `demo/ci-listado-clientes`. En
   `ClientesIntegrationTest.listadoVacioDevuelve200YArregloVacio`, cambiar solamente
   `content().json("[]")` por `content().json("[{}]")`.
   La implementación seguirá devolviendo `[]`; fallará una aserción real.
3. Hacer commit y push, y abrir una PR hacia `main`. Esperar tanto el run de push
   como el de pull request. Conservar el resultado **failure**, el mensaje de la
   aserción y el informe Surefire. No integrar el commit defectuoso.
4. Restaurar `content().json("[]")`, hacer un segundo commit y push a la misma
   rama. Esperar **success** y conservar sus logs y el resultado de la PR.
5. Guardar capturas de la lista de runs, fallo en la etapa de pruebas y corrección
   en verde, además de descargar el JAR y los informes de la versión correcta.

No basta con disparar un workflow o mostrar capturas locales: comprobar los
estados terminales y los SHA de los tres runs. No cancelar el run fallido mediante
un push de corrección antes de que termine.

## Registro de evidencias en la nube

Estado actual: **pendiente de conexión a GitHub y ejecución**.

| Evidencia | Enlace / resultado |
|---|---|
| Repositorio público | Pendiente |
| Éxito inicial en main | Pendiente |
| PR de demostración | Pendiente |
| Fallo controlado | Pendiente |
| Corrección exitosa | Pendiente |
| Logs y artefactos | Pendiente |

Esta tabla debe completarse con enlaces reales después de la ejecución.
