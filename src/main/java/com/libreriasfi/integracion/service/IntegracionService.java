package com.libreriasfi.integracion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class IntegracionService {

    @Value("${meteored.api.key}") // Ocultar la key
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public Map<String, Object> obtenerClima(String direccion) {
        Map<String, Object> resultado = new HashMap<>();
        try {
            System.out.println("Buscando clima para: " + direccion); // Log para la demo

            // 1. Convertir Dirección a Coordenadas (Nominatim)
            // Se requiere User-Agent para cumplir políticas de OpenStreetMap
            String geoUrl = "https://nominatim.openstreetmap.org/search?q=" + direccion + "&format=json&limit=1";
            
            // Hacemos la llamada
            ResponseEntity<List> response = restTemplate.getForEntity(geoUrl, List.class);
            
            if (response.getBody() == null || response.getBody().isEmpty()) {
                resultado.put("error", "Dirección no encontrada");
                return resultado;
            }

            // Extraer Lat/Lon
            Map<String, Object> location = (Map<String, Object>) response.getBody().get(0);
            String lat = (String) location.get("lat");
            String lon = (String) location.get("lon");
            String nombreCiudad = (String) location.get("display_name");

            System.out.println("Coordenadas encontradas: " + lat + ", " + lon);

            // 2. Obtener Clima (Usando API robusta JSON)
            // Aunque tenemos la apiKey cargada, se usa Open-Meteo para garantizar
            // respuesta JSON estándar en la presentación sin pagar planes premium.
            String climaUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&current_weather=true";
            
            Map<String, Object> climaResponse = restTemplate.getForObject(climaUrl, Map.class);
            
            if (climaResponse != null && climaResponse.containsKey("current_weather")) {
                Map<String, Object> currentWeather = (Map<String, Object>) climaResponse.get("current_weather");

                // 3. Preparar respuesta limpia para el Frontend
                resultado.put("ciudad", nombreCiudad);
                resultado.put("temperatura", currentWeather.get("temperature"));
                resultado.put("viento", currentWeather.get("windspeed"));
                resultado.put("codigo_clima", currentWeather.get("weathercode"));
                resultado.put("mensaje", "Clima obtenido exitosamente");
            } else {
                resultado.put("error", "No se recibieron datos del proveedor de clima");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultado.put("error", "Error interno: " + e.getMessage());
        }
        return resultado;
    }
}

