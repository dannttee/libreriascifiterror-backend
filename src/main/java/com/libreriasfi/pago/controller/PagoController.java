package com.libreriasfi.pago.controller;

import com.libreriasfi.pago.entity.Pago;
import com.libreriasfi.pago.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pagos")
@CrossOrigin(origins = "*")
public class PagoController {
    
    @Autowired
    private PagoService pagoService;
    
    @PostMapping("/procesar/{pedidoId}")
    public ResponseEntity<?> procesarPago(
            @PathVariable Long pedidoId,
            @RequestParam Double monto,
            @RequestParam String metodoPago) {
        try {
            Pago pago = pagoService.procesarPago(pedidoId, monto, metodoPago);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pago procesado correctamente");
            response.put("pago", pago);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerPagoPorPedido(@PathVariable Long pedidoId) {
        try {
            var pago = pagoService.obtenerPagoPorPedido(pedidoId);
            
            if (pago.isPresent()) {
                return ResponseEntity.ok(pago.get());
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Pago no encontrado");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
