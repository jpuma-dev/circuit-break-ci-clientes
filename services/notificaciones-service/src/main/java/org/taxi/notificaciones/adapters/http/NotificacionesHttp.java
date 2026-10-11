package org.taxi.notificaciones.adapters.http;

import org.taxi.notificaciones.application.usecases.RecibirNotificaciones;
import com.fasterxml.jackson.databind.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/notificaciones")
public class NotificacionesHttp {
    private final RecibirNotificaciones casos;
    private final ObjectMapper json;
    public NotificacionesHttp(RecibirNotificaciones casos,ObjectMapper json) { this.casos=casos;this.json=json; }
    @GetMapping public List<JsonNode> listar(@RequestParam(required=false) Long viajeId) {
        return casos.consultar(viajeId).stream().map(n->{
            try { return json.readTree(n.contenido()); }
            catch(Exception e) { throw new IllegalStateException("Evento almacenado invalido",e); }
        }).toList();
    }
}
