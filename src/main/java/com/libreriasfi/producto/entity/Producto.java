package com.libreriasfi.producto.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "libros")   // <--- importante
@Data
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // titulo VARCHAR(255) NOT NULL
    @Column(nullable = false)
    private String titulo;

    // autor VARCHAR(255) NOT NULL
    @Column(nullable = false)
    private String autor;

    // precio DECIMAL(10,2) NOT NULL
    @Column(nullable = false)
    private BigDecimal precio;

    // stock INT NOT NULL DEFAULT 15
    @Column(nullable = false)
    private Integer stock;

    // imagen VARCHAR(255)
    private String imagen;

    // descripcion TEXT
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // categoria_id INT
    @Column(name = "categoria_id")
    private Integer categoriaId;
}
