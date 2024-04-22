package com.example.prueba_tecnica.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transaccionAudit")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="audit_id")
    private Long id;
    @Column(name="audit_type_transaction")
    private String typeTransacction;
    @Column(name="audit_date")
    private Date date;
    @Column(name="audit_user")
    private String user;
    @Column(name="audit_description")
    private String description;
    @Column(name="audit_amount")
    private BigDecimal amount;
    @Column(name="audit_account_id")
    private Long accountId;
}
