# Verificación funcional en Docker

Fecha: 4 de octubre de 2026.

Resultado: **65 comprobaciones funcionales aprobadas** sobre el sistema ejecutado en Docker. Los cinco servicios registrados en Eureka estaban UP al finalizar. Se dejó el sistema en funcionamiento.

## Alcance

- Alta y actualización de clientes; DNI duplicado, DNI inválido, campos desconocidos y cliente inexistente.
- Alta de taxi y placa única; protección de taxis reservados.
- Viajes sin taxi, reintento, asignación, finalización y cancelación.
- Repetición de solicitudes con la misma clave; rechazo de la misma clave con otro contenido.
- Ocho solicitudes simultáneas con un taxi disponible: una asignación y siete viajes sin taxi, sin doble reserva.
- Servicio de taxis detenido: seis solicitudes conservadas como pendientes y recuperadas tras iniciarlo.
- RabbitMQ detenido: viaje completado y eventos retenidos para su envío posterior.
- Parada y arranque de los siete contenedores: clientes, taxis, viajes y notificaciones conservaron sus valores.
- Eventos pendientes antes de la parada completa entregados al recuperar el sistema, sin duplicados en el conjunto comprobado.
- Consultas finales de clientes, taxis, viajes y notificaciones respondieron HTTP 200.

La comparación de 99 archivos de código, configuración y construcción entre el proyecto entregado y la carpeta usada para Docker no encontró diferencias.

## Límites de la comprobación

Estas son pruebas funcionales reales por HTTP y reinicios controlados mediante Docker Desktop. No equivalen a una certificación sin errores, una prueba de carga masiva ni una auditoría de seguridad. No se comprobó eliminación/recreación de volúmenes, pérdida de disco ni despliegue con varias réplicas. Se verificó el comportamiento ante fallos; no se inspeccionaron directamente las métricas internas de estados del Circuit Breaker.

La ejecución completa de la suite JUnit mediante Maven sigue sin confirmarse por las restricciones locales de acceso a dependencias. La construcción de las imágenes y el arranque en Docker sí se completaron; el Dockerfile omite la ejecución de JUnit.

## Registros de prueba conservados

No se eliminaron datos. Los registros están identificados con PRUEBA QA / QA y sirven como evidencia de persistencia. El ZIP contiene código y documentación, no las bases de datos de Docker.

- Cliente: ID 1, nombre `PRUEBA QA 1791150009 actualizado`.
- Taxi: ID 1, placa `QA-1791150009`; quedó DISPONIBLE.
- Viajes de prueba: IDs 1 a 17; finalizados o cancelados.

## Detalle de comprobaciones

1. APROBADA: salud gateway.
2. APROBADA: entorno sin taxis disponibles previos.
3. APROBADA: crear cliente.
4. APROBADA: DNI duplicado.
5. APROBADA: DNI invalido.
6. APROBADA: campo desconocido.
7. APROBADA: cliente inexistente.
8. APROBADA: actualizar cliente.
9. APROBADA: viaje sin clave.
10. APROBADA: viaje cliente inexistente.
11. APROBADA: sin taxi queda pendiente de asignacion.
12. APROBADA: crear taxi.
13. APROBADA: placa duplicada.
14. APROBADA: reintentar asigna taxi.
15. APROBADA: reserva impide cambiar estado.
16. APROBADA: completar viaje.
17. APROBADA: completar otra vez idempotente.
18. APROBADA: completado no cancelable.
19. APROBADA: solicitud idempotente.
20. APROBADA: clave con contenido diferente.
21. APROBADA: cancelar libera taxi.
22. APROBADA: ocho solicitudes un solo taxi asignado.
23. APROBADA: cancelar concurrente 6.
24. APROBADA: cancelar concurrente 5.
25. APROBADA: cancelar concurrente 3.
26. APROBADA: cancelar concurrente 9.
27. APROBADA: cancelar concurrente 7.
28. APROBADA: cancelar concurrente 8.
29. APROBADA: cancelar concurrente 10.
30. APROBADA: cancelar concurrente 4.
31. APROBADA: taxi liberado.
32. APROBADA: evento completado recibido RabbitMQ.
33. APROBADA: eventos sin duplicados.
34. APROBADA: fallo taxi conserva solicitud 0.
35. APROBADA: fallo taxi conserva solicitud 1.
36. APROBADA: fallo taxi conserva solicitud 2.
37. APROBADA: fallo taxi conserva solicitud 3.
38. APROBADA: fallo taxi conserva solicitud 4.
39. APROBADA: fallo taxi conserva solicitud 5.
40. APROBADA: taxi persiste tras reiniciar servicio.
41. APROBADA: recupera viaje pendiente 11.
42. APROBADA: cancela viaje recuperado 11.
43. APROBADA: recupera viaje pendiente 12.
44. APROBADA: cancela viaje recuperado 12.
45. APROBADA: recupera viaje pendiente 13.
46. APROBADA: cancela viaje recuperado 13.
47. APROBADA: recupera viaje pendiente 14.
48. APROBADA: cancela viaje recuperado 14.
49. APROBADA: recupera viaje pendiente 15.
50. APROBADA: cancela viaje recuperado 15.
51. APROBADA: recupera viaje pendiente 16.
52. APROBADA: cancela viaje recuperado 16.
53. APROBADA: viaje funciona sin RabbitMQ.
54. APROBADA: completa sin RabbitMQ.
55. APROBADA: eventos retenidos con broker caido.
56. APROBADA: cliente intacto tras reinicio completo.
57. APROBADA: taxi intacto tras reinicio completo.
58. APROBADA: viaje intacto tras reinicio completo.
59. APROBADA: notificaciones previas persisten.
60. APROBADA: eventos pendientes sobreviven y se entregan.
61. APROBADA: sin eventos duplicados tras recuperar broker.
62. APROBADA: consulta final clientes.
63. APROBADA: consulta final taxis.
64. APROBADA: consulta final viajes.
65. APROBADA: consulta final notificaciones.
