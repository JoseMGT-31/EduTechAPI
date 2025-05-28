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

// Controlador REST para manejar las operaciones relacionadas con los pagos
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    // Inyección del servicio de pagos
    @Autowired
    private PagoService pagoService;

    // Obtiene la lista de todos los pagos
    @GetMapping
    public ResponseEntity<?> getAllPagos() {
        List<Pago> pagos = pagoService.getAllPagos();

        // Si no hay pagos registrados, retorna un mensaje informativo
        if (pagos.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "No hay pagos registrados",
                    "data", List.of()
            ));
        }

        // Retorna la lista de pagos
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", pagos
        ));
    }

    // Obtiene un pago por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPagoById(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.getPagoById(id);

        // Si el pago existe, lo retorna
        if (pago.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", pago.get()
            ));
        } else {
            // Si no existe, retorna un mensaje de error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El pago con ID " + id + " no existe"
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPago(@Valid @RequestBody Pago pago, BindingResult result) {
        // Validar los errores de entrada
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
            // Verificar si el curso asociado existe
            Long cursoId = pago.getId_curso();
            String url = "http://localhost:8080/api/cursos/" + cursoId;
    
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
    
            if (response.getBody() == null || response.getBody().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "El curso con ID " + cursoId + " no existe"
                ));
            }
    
            // Si todo está correcto, crea el pago
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


    // Elimina un pago por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePago(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.getPagoById(id);

        // Si el pago no existe, retorna un mensaje de error
        if (pago.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El pago con ID " + id + " no existe"
            ));
        }

        // Elimina el pago y retorna un mensaje de éxito
        pagoService.deletePago(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Pago eliminado exitosamente"
        ));
    }


    // Actualiza un pago por su ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePago(@PathVariable Long id, @Valid @RequestBody Pago pago, BindingResult result) {
        Optional<Pago> pagoExistente = pagoService.getPagoById(id);

        // Verifica si el pago existe
        if (pagoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El pago con ID " + id + " no existe"
            ));
        }

        // Valida los datos enviados
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

        // Realiza la actualización
        Pago pagoToUpdate = pagoExistente.get();
        pagoToUpdate.setFechaPago(pago.getFechaPago());
        pagoToUpdate.setMonto(pago.getMonto());
        pagoToUpdate.setEstadoPago(pago.getEstadoPago());
        pagoToUpdate.setMetodoPago(pago.getMetodoPago());
        pagoToUpdate.setCupon(pago.getCupon());

        pagoService.createPago(pagoToUpdate);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Pago actualizado correctamente"
        ));
    }
}
