package com.eucaliptus.springboot_app_products.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "units")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unit")
    private int idUnit;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(name = "unit_description")
    private String unitDescription;

    public Unit(String unitName, String unitDescription) {
        this.unitName = unitName;
        this.unitDescription = unitDescription;
    }
}
