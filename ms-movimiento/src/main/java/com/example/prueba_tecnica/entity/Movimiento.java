
package com.example.prueba_tecnica.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movement")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqMovimiento")
    @SequenceGenerator(name = "seqMovimiento", allocationSize = 1, sequenceName = "SEQ_MOVIMIENTO")
    @Column(name="movement_id")
    private Long id;
    @Column(name="mov_date")
    private Date date;
    @Column(name="mov_typeMovement")
    private String typeMovement;
    @Column(name="mov_value")
    private BigDecimal value;
    @Column(name="mov_balance")
    private BigDecimal balance;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Cuenta account;
}
