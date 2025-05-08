package com.api.edutech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cupones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    private Double descuento;

    @Temporal(TemporalType.DATE)
    private Date fechaExpiracion;

    @Enumerated(EnumType.STRING)
    private EstadoCupon estado;
}

enum EstadoCupon {
    ACTIVO,
    USADO,
    EXPIRADO
}