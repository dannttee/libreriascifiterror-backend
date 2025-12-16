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
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/clima")
@CrossOrigin(origins = {"https://libreria-scifi-react-ewnt.vercel.app", "http://localhost:3000"})
public class ClimaController {

    @Value("${meteored.api.key:}")
    private String apiKey;
    
    private final String METEORED_BASE_URL = "https://api.meteored.com/api";
    
    // Caché en memoria para ubicaciones (ubicacion -> hash)
    private final Map<String, String> locationCache = new ConcurrentHashMap<>();

    @GetMapping
    public ResponseEntity<?> obtenerClima(@RequestParam String direccion) {
        System.out.println("=== DEBUG CLIMA ===");
        System.out.println("Dirección recibida: " + direccion);
        System.out.println("API Key cargada: " + (apiKey != null && !apiKey.isEmpty() ? "SÍ" : "NO"));
        System.out.println("API Key length: " + (apiKey != null ? apiKey.length() : 0));
        
        try {
            // Validar API Key
            if (apiKey == null || apiKey.isEmpty()) {
                System.out.println("ERROR: API Key no está configurada");
                return ResponseEntity.status(500).body(Map.of(
                    "error", "API Key de Meteored no configurada en el servidor"
                ));
            }
            
            RestTemplate restTemplate = new RestTemplate();
            
            // Normalizar dirección para búsqueda en caché
            String direccionNormalizada = direccion.toLowerCase().trim();
            String locationHash = null;
            
            // PASO 1: Buscar en caché primero
            if (locationCache.containsKey(direccionNormalizada)) {
                System.out.println("Ubicación encontrada en caché");
                locationHash = locationCache.get(direccionNormalizada);
            } else {
                System.out.println("Buscando ubicación en API...");
                // Si no está en caché, buscar en la API
                String searchUrl = METEORED_BASE_URL + "/location/v1/search/txt/" + direccion 
                                 + "?api_key=" + apiKey;
                System.out.println("URL de búsqueda: " + searchUrl.replace(apiKey, "***KEY***"));
                
                Map<String, Object> locationResponse = restTemplate.getForObject(searchUrl, Map.class);
                System.out.println("Respuesta de búsqueda: " + locationResponse);
                
                // Verificar si hay resultados
                if (locationResponse == null || !locationResponse.containsKey("locations")) {
                    System.out.println("ERROR: Sin resultados de ubicación");
                    return ResponseEntity.status(404).body(Map.of("error", "Ubicación no encontrada"));
                }
                
                List<Map<String, Object>> locations = (List<Map<String, Object>>) locationResponse.get("locations");
                if (locations == null || locations.isEmpty()) {
                    System.out.println("ERROR: Lista de ubicaciones vacía");
                    return ResponseEntity.status(404).body(Map.of("error", "No hay resultados para esta ubicación"));
                }
                
                // Obtener la primera ubicación
                Map<String, Object> primeraUbicacion = locations.get(0);
                locationHash = (String) primeraUbicacion.get("hash");
                System.out.println("Hash obtenido: " + locationHash);
                
                if (locationHash == null) {
                    System.out.println("ERROR: No se pudo obtener el hash");
                    return ResponseEntity.status(400).body(Map.of("error", "No se pudo obtener el hash de la ubicación"));
                }
                
                // Guardar en caché
                locationCache.put(direccionNormalizada, locationHash);
            }
            
            // PASO 2: Obtener el pronóstico por hora usando el hash
            System.out.println("Obteniendo pronóstico con hash: " + locationHash);
            String forecastUrl = METEORED_BASE_URL + "/forecast/v1/hourly/" + locationHash 
                               + "?api_key=" + apiKey;
            System.out.println("URL de pronóstico: " + forecastUrl.replace(apiKey, "***KEY***"));
            
            Map<String, Object> forecastResponse = restTemplate.getForObject(forecastUrl, Map.class);
            System.out.println("Respuesta de pronóstico recibida: " + (forecastResponse != null ? "SÍ" : "NO"));
            
            // PASO 3: Procesar respuesta para extraer datos útiles
            Map<String, Object> resultado = new LinkedHashMap<>();
            resultado.put("ubicacion", direccion);
            
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
            
            System.out.println("=== ÉXITO ===");
            return ResponseEntity.ok(resultado);
            
        } catch (RestClientException e) {
            System.out.println("ERROR RestClient: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error conectando con API de clima: " + e.getMessage());
            error.put("tipo", "RestClientException");
            return ResponseEntity.status(500).body(error);
        } catch (Exception e) {
            System.out.println("ERROR General: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error procesando solicitud: " + e.getMessage());
            error.put("tipo", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(error);
        }
    }
}
