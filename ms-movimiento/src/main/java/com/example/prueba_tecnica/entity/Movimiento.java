
package com.example.prueba_tecnica.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movement")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="movement_id")
    private Long id;
    @Column(name="mov_date")
    private Date date;
    @Column(name="mov_typeMovement")
    private String typeMovement;
    @Column(name="mov_value")
    private double value;
    @Column(name="mov_balance")
    private double balance;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Cuenta account;
}
