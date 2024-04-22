
package com.example.prueba_tecnica.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @ApiModelProperty(value = "Id de la Cuenta", example = "100")
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
