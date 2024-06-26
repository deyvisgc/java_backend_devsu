
package com.example.prueba_tecnica.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCuenta")
    @SequenceGenerator(name = "seqCuenta", allocationSize = 1, sequenceName = "SEQ_CUENTA")
    @Column(name="account_id")
    private Long id;
    @Column(name="acc_number")
    private String accountNumber;
    @Column(name="acc_type")
    private String accountType;
    @Column(name="acc_initial_balance")
    private BigDecimal initialBalance;
    @Column(name="acc_balance_actual")
    private BigDecimal balanceActual;
    @Column(name="acc_status")
    private char status;
    @Column(name="client_id")
    private Long clienteId;
    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Movimiento> movement;
    @Transient
    private String nameCustomer;

}
