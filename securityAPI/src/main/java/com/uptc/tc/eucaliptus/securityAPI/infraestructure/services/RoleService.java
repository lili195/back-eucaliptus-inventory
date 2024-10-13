package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getByRoleName(RoleList name){
        return roleRepository.findByRoleName(name).get();
    }

    public Role save(Role role){
        if(!this.roleRepository.existsByRoleName(role.getRoleName())) {
            Role newRole = roleRepository.save(role);
            return newRole;
        } else {
            return getByRoleName(role.getRoleName());
        }
    }


}
