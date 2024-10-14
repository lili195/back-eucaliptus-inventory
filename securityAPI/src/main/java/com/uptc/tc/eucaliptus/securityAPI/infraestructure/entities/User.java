package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    @NotNull
    @NaturalId
    @Column(unique = true)
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User(@NotNull String username, @NotNull String email, @NotNull String password, @NotNull Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {

    }
}
