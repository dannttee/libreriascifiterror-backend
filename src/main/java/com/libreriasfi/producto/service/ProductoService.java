package com.libreriasfi.producto.service;

import com.libreriasfi.producto.entity.Producto;
import com.libreriasfi.producto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Buscar por título (esto sí lo puedes mantener)
    public List<Producto> buscarPorTitulo(String titulo) {
        return productoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setTitulo(productoActualizado.getTitulo());
                    p.setAutor(productoActualizado.getAutor());
                    p.setDescripcion(productoActualizado.getDescripcion());
                    p.setPrecio(productoActualizado.getPrecio());
                    p.setStock(productoActualizado.getStock());
                    p.setImagen(productoActualizado.getImagen());
                    p.setCategoriaId(productoActualizado.getCategoriaId());
                    return productoRepository.save(p);
                })
                .orElse(null);
    }
}

