
package com.example.prueba_tecnica.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Client extends Person {
    @Column(name="client_password")
    private String password;
    @Column(name="client_status")
    private char status;
}
