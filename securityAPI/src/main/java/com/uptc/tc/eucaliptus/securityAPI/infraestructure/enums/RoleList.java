package com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums;

/**
 * Enum que define los diferentes roles disponibles en el sistema.
 * <p>
 * Este enum es utilizado para asignar roles a los usuarios, como el rol de administrador
 * o vendedor, proporcionando un control de acceso basado en roles en el sistema.
 * </p>
 */
public enum RoleList {
    /**
     * Rol de administrador.
     * Los usuarios con este rol tienen permisos completos para administrar
     * el sistema.
     */
    ROLE_ADMIN,

    /**
     * Rol de vendedor.
     * Los usuarios con este rol tienen permisos para vender productos,
     * pero con permisos limitados en otras áreas del sistema.
     */
    ROLE_SELLER
}
