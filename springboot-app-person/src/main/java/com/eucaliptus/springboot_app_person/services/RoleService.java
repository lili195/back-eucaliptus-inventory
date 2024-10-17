package com.eucaliptus.springboot_app_person.services;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Role;
import com.eucaliptus.springboot_app_person.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public boolean existsRoleByName(EnumRole role) {
        return roleRepository.existsByNameRole(role);
    }

    public Optional<Role> getRoleByName(EnumRole name) {
        return roleRepository.findByNameRole(name);
    }

    public Optional<Role> updateRole(Long id, Role roleDetails) {
        return roleRepository.findById(id).map(role -> {
            role.setNameRole(roleDetails.getNameRole());
            return roleRepository.save(role);
        });
    }

    public boolean deleteRole(Long id) {
        return roleRepository.findById(id).map(role -> {
            roleRepository.delete(role);
            return true;
        }).orElse(false);
    }
}
