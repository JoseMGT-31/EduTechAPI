package com.api.edutech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date fechaPago;

    private Double monto;

    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;

    private String metodoPago;

    @ManyToOne
    @JoinColumn(name = "cupon_id")
    private Cupon cupon;
}

enum EstadoPago {
    PENDIENTE,
    COMPLETADO,
    FALLIDO
}