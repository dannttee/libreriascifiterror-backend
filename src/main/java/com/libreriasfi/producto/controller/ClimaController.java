package com.libreriasfi.producto.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/clima")
@CrossOrigin(origins = "*") 
public class ClimaController {

    // Aquí le decimos a Spring: "Busca la variable weather.api.key en el entorno"
    @Value("${meteored.api.key}")
    private String apiKey; 
    
    private final String API_URL = "http://api.weatherapi.com/v1/current.json";

    @GetMapping
    public ResponseEntity<?> obtenerClima(@RequestParam String direccion) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // Usamos la variable apiKey que se cargó sola
            String url = API_URL + "?key=" + apiKey + "&q=" + direccion + "&aqi=no";
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "No se pudo obtener el clima: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
