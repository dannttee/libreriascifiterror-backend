package com.libreriasfi.pago.service;

import com.libreriasfi.pago.entity.Pago;
import com.libreriasfi.pago.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PagoService {
    
    @Autowired
    private PagoRepository pagoRepository;
    
    public Pago procesarPago(Long pedidoId, Double monto, String metodoPago) {
        Pago pago = new Pago();
        pago.setPedidoId(pedidoId);
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setEstado("COMPLETADO");
        pago.setFechaPago(LocalDateTime.now());
        
        return pagoRepository.save(pago);
    }
    
    public Optional<Pago> obtenerPagoPorPedido(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId);
    }
}
