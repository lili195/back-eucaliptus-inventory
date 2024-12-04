package com.eucaliptus.springboot_app_person.enums;

/**
 * Enumeración que representa los diferentes roles disponibles en el sistema.
 * Los roles definen los permisos y responsabilidades asignados a los usuarios.
 */

public enum EnumRole {
    /**
     * Rol de administrador.
     * Tiene acceso completo al sistema y puede gestionar todos los recursos.
     */
    ROLE_ADMIN,
    /**
     * Rol de vendedor.
     * Responsable de las operaciones de ventas y atención a clientes.
     */
    ROLE_SELLER,
    /**
     * Rol de proveedor.
     * Encargado de suministrar bienes o servicios al sistema.
     */
    ROLE_PROVIDER,
    /**
     * Rol de cliente.
     * Representa a los usuarios que consumen los bienes o servicios ofrecidos.
     */

    ROLE_CLIENT,
    /**
     * Rol de empresa.
     * Utilizado para representar a entidades corporativas registradas en el sistema.
     */
    ROLE_COMPANY
}
