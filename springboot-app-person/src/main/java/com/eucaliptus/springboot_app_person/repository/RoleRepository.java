package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
