package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tokens")
public class TokenEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    @NotNull
    @NaturalId
    @Column(unique = true)
    private String token;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public TokenEntity(@NotNull String token, @NotNull User user) {
        this.token = token;
        this.user = user;
    }

}
