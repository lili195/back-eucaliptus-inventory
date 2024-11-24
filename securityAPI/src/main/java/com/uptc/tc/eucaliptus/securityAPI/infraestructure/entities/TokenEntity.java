package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

/**
 * Representa un token de autenticación asociado a un usuario en el sistema.
 * <p>
 * Esta clase se utiliza para almacenar los tokens generados para los usuarios,
 * los cuales se utilizan para la autenticación o validación en el sistema.
 * </p>
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tokens")
public class TokenEntity {

    /**
     * El identificador único del token.
     * Este campo es generado automáticamente usando un generador de UUID.
     */

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    /**
     * El valor del token.
     * Este campo es único para cada token generado y no puede ser nulo.
     */

    @NotNull
    @NaturalId
    @Column(unique = true)
    private String token;

    /**
     * El usuario al que está asociado el token.
     * Este campo es obligatorio y se mapea a la tabla {@link User}.
     * La relación se establece con la estrategia de carga EAGER.
     */

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Constructor que inicializa un nuevo token con un valor específico y un usuario asociado.
     *
     * @param token El valor del token.
     * @param user  El usuario al que está asociado el token.
     */

    public TokenEntity(@NotNull String token, @NotNull User user) {
        this.token = token;
        this.user = user;
    }

}