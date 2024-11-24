package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Servicio para gestionar las operaciones relacionadas con los roles de usuario.
 * <p>
 * Este servicio permite obtener roles por su nombre y guardar nuevos roles en la base de datos.
 * </p>
 */

@Service
public class RoleService {

    private RoleRepository roleRepository;

    /**
     * Constructor para inyectar la dependencia del repositorio de roles.
     *
     * @param roleRepository El repositorio que maneja las operaciones de persistencia de roles.
     */

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Obtiene un rol de la base de datos según su nombre.
     *
     * @param name El nombre del rol que se desea obtener.
     * @return El rol correspondiente al nombre proporcionado.
     * @throws NoSuchElementException Si el rol con el nombre especificado no se encuentra.
     */

    public Role getByRoleName(RoleList name){
        return roleRepository.findByRoleName(name).get();
    }

    /**
     * Guarda un nuevo rol en la base de datos si no existe previamente.
     * <p>
     * Si el rol ya existe, se retorna el rol existente en lugar de crear uno nuevo.
     * </p>
     *
     * @param role El rol que se desea guardar.
     * @return El rol guardado o el rol existente si ya está registrado.
     */

    public Role save(Role role){
        if(!this.roleRepository.existsByRoleName(role.getRoleName())) {
            Role newRole = roleRepository.save(role);
            return newRole;
        } else {
            return getByRoleName(role.getRoleName());
        }
    }
}