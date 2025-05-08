package com.api.edutech.controller;

import com.api.edutech.model.Pago;
import com.api.edutech.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<Pago> getAllPagos() {
        return pagoService.getAllPagos();
    }

    @GetMapping("/{id}")
    public Optional<Pago> getPagoById(@PathVariable Long id) {
        return pagoService.getPagoById(id);
    }

    @PostMapping
    public Pago createPago(@RequestBody Pago pago) {
        return pagoService.createPago(pago);
    }

    @DeleteMapping("/{id}")
    public void deletePago(@PathVariable Long id) {
        pagoService.deletePago(id);
    }
}