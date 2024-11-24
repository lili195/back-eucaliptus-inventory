package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

/**
 * Representa un usuario dentro del sistema.
 * <p>
 * Esta clase almacena la información básica de un usuario, como su nombre de usuario,
 * correo electrónico, contraseña y el rol asignado al usuario.
 * </p>
 */

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    /**
     * El identificador único del usuario.
     * Este campo es generado automáticamente usando un generador de UUID.
     */

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    /**
     * El nombre de usuario único en el sistema.
     * Este campo no puede ser nulo y debe ser único.
     */

    @NotNull
    @Column(unique = true)
    private String username;

    /**
     * El correo electrónico del usuario.
     * Este campo no puede ser nulo.
     */

    @NotNull
    private String email;

    /**
     * La contraseña del usuario.
     * Este campo no puede ser nulo.
     */

    @NotNull
    private String password;

    /**
     * El rol asignado al usuario.
     * Este campo establece una relación con la entidad {@link Role}.
     * La relación se establece con la estrategia de carga EAGER, lo que significa que
     * el rol se carga inmediatamente cuando se carga el usuario.
     */

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Constructor para inicializar un nuevo usuario con los valores proporcionados.
     *
     * @param username El nombre de usuario del usuario.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param role El rol asignado al usuario.
     */

    public User(@NotNull String username, @NotNull String email, @NotNull String password, @NotNull Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Constructor por defecto para crear un usuario vacío.
     */

    public User() {

    }
}
