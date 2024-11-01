package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "recovery_codes")
public class RecoveryCode {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    @NotNull
    @NaturalId
    @Column(unique = true)
    private int code;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    public RecoveryCode(int code, User user, LocalDateTime expiryDate) {
        this.code = code;
        this.user = user;
        this.expiryDate = expiryDate;
    }
}
