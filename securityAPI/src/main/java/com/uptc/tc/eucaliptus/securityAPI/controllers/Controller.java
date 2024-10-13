package com.uptc.tc.eucaliptus.securityAPI.controllers;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos.UserDTO;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Message;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.RoleService;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class Controller {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Controller(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/addSeller")
    public ResponseEntity<Object> addSeller(@Valid @RequestBody UserDTO userDTO){
        Role role = roleService.save(new Role(RoleList.ROLE_SELLER));;
        User userEntity = new User(userDTO.getUsername(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()),
                role);
        User userSaved = userService.save(userEntity);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteSeller")
    public ResponseEntity<Object> deleteSeller(@RequestParam String username ){
        if(!userService.existByUserName(username))
            return new ResponseEntity<>(new Message("El usuario no existe"), HttpStatus.BAD_REQUEST);
        else {
            userService.delete(username);
            return new ResponseEntity<>(new Message("Usuario eliminado existosamente"), HttpStatus.OK);
        }
    }
}
