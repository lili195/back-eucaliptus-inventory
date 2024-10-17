package com.eucaliptus.springboot_app_person.model;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long idRole;

    @Column(name = "name_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumRole nameRole;

    public Role(EnumRole nameRole) {
        this.nameRole = nameRole;
    }
    public Role() {}

}
