package com.api.edutech.controller;

import com.api.edutech.model.Cupon;
import com.api.edutech.service.CuponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cupones")
public class CuponController {

    @Autowired
    private CuponService cuponService;

    @GetMapping
    public List<Cupon> getAllCupones() {
        return cuponService.getAllCupones();
    }

    @GetMapping("/{id}")
    public Optional<Cupon> getCuponById(@PathVariable Long id) {
        return cuponService.getCuponById(id);
    }

    @PostMapping
    public Cupon createCupon(@RequestBody Cupon cupon) {
        return cuponService.createCupon(cupon);
    }

    @DeleteMapping("/{id}")
    public void deleteCupon(@PathVariable Long id) {
        cuponService.deleteCupon(id);
    }
}
