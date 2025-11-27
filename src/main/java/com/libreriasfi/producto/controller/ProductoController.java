package com.libreriasfi.producto.controller;

import com.libreriasfi.producto.entity.Producto;
import com.libreriasfi.producto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Producto> productos = productoService.obtenerTodos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productos);
            response.put("total", productos.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            var producto = productoService.obtenerPorId(id);
            
            if (producto.isPresent()) {
                return ResponseEntity.ok(producto.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/genero/{genero}")
    public ResponseEntity<?> obtenerPorGenero(@PathVariable String genero) {
        try {
            List<Producto> productos = productoService.obtenerPorGenero(genero);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productos);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/buscar/{titulo}")
    public ResponseEntity<?> buscarPorTitulo(@PathVariable String titulo) {
        try {
            List<Producto> productos = productoService.buscarPorTitulo(titulo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productos);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerDisponibles() {
        try {
            List<Producto> productos = productoService.obtenerDisponibles();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", productos);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
