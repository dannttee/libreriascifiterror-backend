package com.libreriasfi.producto.repository;

import com.libreriasfi.producto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByGenero(String genero);
    List<Producto> findByTituloContainingIgnoreCase(String titulo);
    List<Producto> findByDisponibleTrue();
}
