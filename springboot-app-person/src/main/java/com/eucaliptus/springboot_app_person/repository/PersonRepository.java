package com.eucaliptus.springboot_app_person.repository;

import com.eucaliptus.springboot_app_person.enums.EnumRole;
import com.eucaliptus.springboot_app_person.model.Person;
import com.eucaliptus.springboot_app_person.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    boolean existsByIdNumber(String idNumber);

    Optional<Person> findByIdNumber(String idNumber);

    Optional<Person> findByActiveTrueAndIdNumber(String idNumber);

    Optional<Seller> findByRole(EnumRole role);
}