package com.libreriasfi.pago.controller;

import com.libreriasfi.pago.entity.Pago;
import com.libreriasfi.pago.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pagos")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:3000",
        "http://127.0.0.1:5173"
})
@Tag(name = "pago-controller", description = "Operaciones de pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // POST /api/v1/pagos/procesar/{pedidoId}
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

    // GET /api/v1/pagos/pedido/{pedidoId}
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

    // GET /api/v1/pagos  -> listar todas las compras realizadas
    @GetMapping
    public ResponseEntity<?> listarPagos() {
        try {
            List<Pago> pagos = pagoService.listarPagos();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", pagos);
            response.put("total", pagos.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}

