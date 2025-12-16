package com.libreriasfi.producto.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/clima")
@CrossOrigin(origins = "https://libreria-scifi-react-ewnt.vercel.app")
public class ClimaController {

    @Value("${meteored.api.key}")
    private String apiKey;
    
    private final String METEORED_BASE_URL = "https://api.meteored.com/api";

    @GetMapping
    public ResponseEntity<?> obtenerClima(@RequestParam String direccion) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            // PASO 1: Buscar ubicación por nombre de texto
            String searchUrl = METEORED_BASE_URL + "/location/v1/search/txt/" + direccion 
                             + "?api_key=" + apiKey;
            Map<String, Object> locationResponse = restTemplate.getForObject(searchUrl, Map.class);
            
            // Verificar si hay resultados
            if (locationResponse == null || !locationResponse.containsKey("locations")) {
                return ResponseEntity.status(404).body(Map.of("error", "Ubicación no encontrada"));
            }
            
            List<Map<String, Object>> locations = (List<Map<String, Object>>) locationResponse.get("locations");
            if (locations == null || locations.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "No hay resultados para esta ubicación"));
            }
            
            // Obtener la primera ubicación
            Map<String, Object> primeraUbicacion = locations.get(0);
            String locationHash = (String) primeraUbicacion.get("hash");
            
            if (locationHash == null) {
                return ResponseEntity.status(400).body(Map.of("error", "No se pudo obtener el hash de la ubicación"));
            }
            
            // PASO 2: Obtener el pronóstico por hora usando el hash
            String forecastUrl = METEORED_BASE_URL + "/forecast/v1/hourly/" + locationHash 
                               + "?api_key=" + apiKey;
            Map<String, Object> forecastResponse = restTemplate.getForObject(forecastUrl, Map.class);
            
            // PASO 3: Procesar respuesta para extraer datos útiles
            Map<String, Object> resultado = new LinkedHashMap<>();
            resultado.put("ubicacion", primeraUbicacion.get("name"));
            resultado.put("pais", primeraUbicacion.get("country"));
            resultado.put("provincia", primeraUbicacion.get("province"));
            
            // Si hay datos de pronóstico
            if (forecastResponse != null && forecastResponse.containsKey("data")) {
                Map<String, Object> datos = (Map<String, Object>) forecastResponse.get("data");
                
                // Extraer datos actuales (primer elemento)
                if (datos.containsKey("hours") && datos.get("hours") != null) {
                    List<Map<String, Object>> horas = (List<Map<String, Object>>) datos.get("hours");
                    if (!horas.isEmpty()) {
                        Map<String, Object> horaActual = horas.get(0);
                        resultado.put("temperatura", horaActual.get("temperature"));
                        resultado.put("sensacionTermica", horaActual.get("feels_like"));
                        resultado.put("humedad", horaActual.get("humidity"));
                        resultado.put("velocidadViento", horaActual.get("wind_speed"));
                        resultado.put("descripcion", horaActual.get("description"));
                        resultado.put("icono", horaActual.get("icon"));
                    }
                }
            }
            
            resultado.put("fuente", "Meteored API");
            
            return ResponseEntity.ok(resultado);
            
        } catch (RestClientException e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error conectando con API de clima: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error procesando solicitud: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
