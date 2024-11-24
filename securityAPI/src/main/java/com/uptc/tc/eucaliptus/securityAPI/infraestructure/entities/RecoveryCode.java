package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

/**
 * Representa un código de recuperación asociado a un usuario en el sistema.
 * <p>
 * Esta clase almacena un código de recuperación que se asocia a un usuario específico.
 * El código se genera cuando el usuario solicita un proceso de recuperación de contraseña.
 * También almacena la fecha de caducidad del código.
 * </p>
 */

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "recovery_codes")
public class RecoveryCode {

    /**
     * El identificador único del código de recuperación.
     * Este campo es generado automáticamente por el sistema.
     */

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    /**
     * El código de recuperación único asociado al usuario.
     * Este valor debe ser único y no puede ser nulo.
     */

    @NotNull
    @NaturalId
    @Column(unique = true)
    private int code;

    /**
     * El usuario al que pertenece este código de recuperación.
     * La relación es de tipo Muchos a Uno, ya que un usuario puede tener varios códigos de recuperación.
     * Este campo no puede ser nulo.
     */

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * La fecha de caducidad del código de recuperación.
     * Este campo no puede ser nulo y especifica cuándo el código ya no será válido.
     */

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Constructor que inicializa un nuevo código de recuperación.
     *
     * @param code      El código de recuperación generado.
     * @param user      El usuario al que se asocia este código de recuperación.
     * @param expiryDate La fecha de caducidad del código.
     */

    public RecoveryCode(int code, User user, LocalDateTime expiryDate) {
        this.code = code;
        this.user = user;
        this.expiryDate = expiryDate;
    }
}
