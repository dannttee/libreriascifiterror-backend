package com.libreriasfi.pago.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long pedidoId;
    
    @Column(nullable = false)
    private Double monto;
    
    @Column(nullable = false)
    private String metodoPago;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(nullable = false)
    private LocalDateTime fechaPago;
}
