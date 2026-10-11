# Validación de la adaptación a Nexo

Fecha: 11 de octubre de 2026.

## Resultado

Módulo Clientes: BUILD SUCCESS. Cinco pruebas ejecutadas, cero fallos, cero errores y cero omitidas. Se compiló y empaquetó el JAR Spring Boot después de reorganizar las capas y extraer el caso de uso de listado.

Pruebas: listado vacío; listado con dos clientes y todos sus datos; persistencia y DNI duplicado; DNI inválido; campos no declarados.

La primera ejecución local se interrumpió porque Windows restringió la carga dinámica del agente de Mockito. La ejecución exitosa usó Maven 3.9.12, Java 21, dependencias locales y un agente Mockito cargado al inicio mediante -DargLine=-javaagent:<ruta-local-al-jar-de-mockito>. No se omiten pruebas en el workflow; GitHub usa Ubuntu. El agente local no se incorpora al ZIP.

Comprobaciones estructurales adicionales:
- Los seis módulos declarados en el POM raíz existen bajo services/.
- Los paquetes Java de Clientes corresponden a sus directorios.
- Los imports de domain y application no dependen de Spring, Jakarta, infrastructure ni presentation.
- El contenido de los otros cinco módulos se conserva sin cambios respecto del ZIP previo; sólo cambia su ubicación.
- El Dockerfile usa services/$MODULE tanto para seleccionar el módulo como para localizar el JAR. No se ejecutó Docker en esta adaptación.

## Cambios

Organización services/, arquitectura por capas de Clientes, DTO de petición separado, ListarClientesUseCase, wiring en CasosConfig, rutas del POM y Dockerfile, CI reutilizable sólo invocada por Clientes y documentación.

## Pendiente

Crear y publicar el repositorio requiere iniciar sesión o conectar GitHub. No se ha ejecutado ni confirmado la tubería en GitHub Actions. La validación anterior es local del módulo Clientes, no una ejecución de todos los microservicios.
