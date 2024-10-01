package com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities;

import java.util.HashSet;
import java.util.Set;

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
    private String userName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User() {
    }

    public User(@NotNull String userName, @NotNull String email, @NotNull String password, @NotNull Role role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
