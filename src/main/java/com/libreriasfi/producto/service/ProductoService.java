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
    
    public List<Producto> obtenerPorGenero(String genero) {
        return productoRepository.findByGenero(genero);
    }
    
    public List<Producto> buscarPorTitulo(String titulo) {
        return productoRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    public List<Producto> obtenerDisponibles() {
        return productoRepository.findByDisponibleTrue();
    }
    
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Optional<Producto> producto = productoRepository.findById(id);
        
        if (producto.isPresent()) {
            Producto p = producto.get();
            p.setTitulo(productoActualizado.getTitulo());
            p.setDescripcion(productoActualizado.getDescripcion());
            p.setAutor(productoActualizado.getAutor());
            p.setGenero(productoActualizado.getGenero());
            p.setPrecio(productoActualizado.getPrecio());
            p.setStock(productoActualizado.getStock());
            p.setDisponible(productoActualizado.getDisponible());
            
            return productoRepository.save(p);
        }
        
        return null;
    }
}
