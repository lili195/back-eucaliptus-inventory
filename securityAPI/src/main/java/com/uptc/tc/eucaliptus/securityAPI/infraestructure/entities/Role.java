package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un rol de usuario en el sistema.
 * <p>
 * Esta clase se utiliza para definir los diferentes roles que un usuario puede tener en el sistema.
 * Los roles se asignan a los usuarios para controlar sus permisos y accesos a diferentes funcionalidades.
 * </p>
 */

@Setter
@Getter
@Entity
public class Role {

    /**
     * El identificador único del rol.
     * Este campo es generado automáticamente con una estrategia de identidad.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * El nombre del rol, que se define mediante un valor de la enumeración {@link RoleList}.
     * Este campo no puede ser nulo y se almacena como un valor de tipo cadena.
     */

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleList roleName;

    /**
     * Constructor vacío. Utilizado por JPA para crear instancias de la clase {@link Role}.
     */

    public Role() {
    }

    /**
     * Constructor que inicializa un nuevo rol con un nombre específico.
     *
     * @param roleName El nombre del rol, definido como un valor de la enumeración {@link RoleList}.
     */

    public Role(@NotNull RoleList roleName) {
        this.roleName = roleName;
    }

}
