package com.api.edutech.controller;

import com.api.edutech.model.Pago;
import com.api.edutech.service.PagoService;
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
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<?> getAllPagos() {
        List<Pago> pagos = pagoService.getAllPagos();

        if (pagos.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "No hay pagos registrados",
                    "data", List.of()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", pagos
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPagoById(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.getPagoById(id);

        if (pago.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", pago.get()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El pago con ID " + id + " no existe"
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPago(@Valid @RequestBody Pago pago, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errores.put(error.getField(), error.getDefaultMessage());
            });

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "errors", errores
            ));
        }

        try {
            pagoService.createPago(pago);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Pago registrado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al registrar el pago"
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePago(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.getPagoById(id);

        if (pago.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El pago con ID " + id + " no existe"
            ));
        }

        pagoService.deletePago(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Pago eliminado exitosamente"
        ));
    }
}