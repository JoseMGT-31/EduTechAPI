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

    @GetMapping
    public ResponseEntity<List<Cupon>> getAllCupones() {
        List<Cupon> cupones = cuponService.getAllCupones();
        return ResponseEntity.ok(cupones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCuponById(@PathVariable Long id) {
        Optional<Cupon> cupon = cuponService.getCuponById(id);

        if (cupon.isPresent()) {
            return ResponseEntity.ok(cupon.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "El cupón con ID " + id + " no existe"
                    ));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCupon(@Valid @RequestBody Cupon cupon, BindingResult result) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCupon(@PathVariable Long id) {
        Optional<Cupon> cupon = cuponService.getCuponById(id);
        if (cupon.isEmpty()) {
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
}