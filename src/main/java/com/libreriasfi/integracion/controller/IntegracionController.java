package com.libreriasfi.integracion.controller;

import com.libreriasfi.integracion.service.IntegracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/integracion")
// Permite que Vercel, localhost y cualquier otro frontend le hablen a este controlador
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IntegracionController {

    @Autowired
    private IntegracionService integracionService;

    @GetMapping("/clima")
    public ResponseEntity<Map<String, Object>> getClima(@RequestParam String direccion) {
        Map<String, Object> resultado = integracionService.obtenerClima(direccion);
        
        // Si el servicio nos devolvió un error, respondemos con un 400 Bad Request
        if (resultado.containsKey("error")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        
        // Si todo salió bien, respondemos con un 200 OK
        return ResponseEntity.ok(resultado);
    }
}

