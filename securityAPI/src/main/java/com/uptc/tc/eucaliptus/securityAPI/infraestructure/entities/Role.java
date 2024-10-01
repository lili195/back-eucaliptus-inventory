package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleList roleName;

    public Role() {
    }

    public Role(@NotNull RoleList roleName) {
        this.roleName = roleName;
    }

}
