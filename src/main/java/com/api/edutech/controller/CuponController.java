package com.api.edutech.controller;

import com.api.edutech.model.Cupon;
import com.api.edutech.service.CuponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cupones")
public class CuponController {

    @Autowired
    private CuponService cuponService;

    // Obtiene la lista de todos los cupones
    @GetMapping
    public ResponseEntity<List<Cupon>> getAllCupones() {
        List<Cupon> cupones = cuponService.getAllCupones();
        return ResponseEntity.ok(cupones);
    }

    // Obtiene un cupón por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCuponById(@PathVariable Long id) {
        Optional<Cupon> cupon = cuponService.getCuponById(id);

        if (cupon.isPresent()) {
            return ResponseEntity.ok(cupon.get());
        } else {
            // Retorna un mensaje si el cupón no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "El cupón con ID " + id + " no existe"
                    ));
        }
    }

    // Crea un nuevo cupón
    @PostMapping
    public ResponseEntity<?> createCupon(@Valid @RequestBody Cupon cupon, BindingResult result) {
        // Valida los datos recibidos
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errores.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        cuponService.createCupon(cupon);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true));
    }

    // Elimina un cupón por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCupon(@PathVariable Long id) {
        Optional<Cupon> cupon = cuponService.getCuponById(id);
        if (cupon.isEmpty()) {
            // Retorna un mensaje si el cupón no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "El cupón con ID " + id + " no existe"
                    ));
        }

        cuponService.deleteCupon(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cupón eliminado exitosamente"
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCupon(@PathVariable Long id, @Valid @RequestBody Cupon cupon, BindingResult result) {
        Optional<Cupon> cuponExistente = cuponService.getCuponById(id);

        if (cuponExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "El cupón con ID " + id + " no existe"
                    ));
        }

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errores.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        Cupon cuponToUpdate = cuponExistente.get();
        cuponToUpdate.setCodigo(cupon.getCodigo());
        cuponToUpdate.setDescuento(cupon.getDescuento());
        cuponToUpdate.setFechaExpiracion(cupon.getFechaExpiracion());
        cuponToUpdate.setEstado(cupon.getEstado());

        cuponService.createCupon(cuponToUpdate);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cupón actualizado correctamente"
        ));
    }
}