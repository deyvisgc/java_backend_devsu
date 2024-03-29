
package com.example.prueba_tecnica.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persona")
public class Person{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name="id")
    private Long id;
    @Column(name="per_name")
    private String name;
    @Column(name="per_gender")
    private String gender;
    @Column(name="per_age")
    private int age;
    @Column(name="per_identification")
    private String identification;
    @Column(name="per_address")
    private String address;
    @Column(name="per_phone")
    private String phone;


}
