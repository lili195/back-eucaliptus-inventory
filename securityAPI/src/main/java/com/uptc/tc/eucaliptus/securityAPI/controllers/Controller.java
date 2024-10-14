package com.uptc.tc.eucaliptus.securityAPI.controllers;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos.LoginUser;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos.UserDTO;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Message;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.TokenEntity;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.RoleService;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.TokenService;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.UserService;
import com.uptc.tc.eucaliptus.securityAPI.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class Controller {

    private final UserService userService;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public Controller(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(HttpServletResponse response, HttpServletRequest request,
                                        @Valid @RequestBody LoginUser loginUser, BindingResult bidBindingResult) {
        if (bidBindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        try {
            String username = loginUser.getUsername();
            String password = loginUser.getPassword();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authenticationToken);

            String token = jwtProvider.generateToken(loginUser.getUsername());
            tokenService.save(new TokenEntity(token, userService.getByUserName(username).get()));
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Credenciales invalidas"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addSeller")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addSeller(@Valid @RequestBody UserDTO userDTO){
        Role role = roleService.save(new Role(RoleList.ROLE_SELLER));;
        User userEntity = new User(userDTO.getUsername(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()),
                role);
        if(!userService.existByUserName(userDTO.getUsername())) {
            User userSaved = userService.save(userEntity);
            return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new Message("El usuario ya existe"), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> changePassword(@RequestBody LoginUser loginUser, BindingResult bidBindingResult,
                                                 HttpServletRequest request){
        String headerToken = request.getHeader("Authorization");
        if(headerToken != null && headerToken.startsWith("Bearer ")){
            String token = headerToken.substring(7);
            String username = loginUser.getUsername();
            if(username.equals(jwtProvider.getUsernameFromToken(token)) && userService.existByUserName(username)) {
                User user = userService.getByUserName(loginUser.getUsername()).get();
                user.setPassword(passwordEncoder.encode(loginUser.getPassword()));
                userService.save(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Message("El usuario no existe"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new Message("Token invalido"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteSeller")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteSeller(@RequestParam String username ){
        if(userService.existByUserName(username)){
            tokenService.deleteByUserId(userService.getByUserName(username).get().getId());
            userService.delete(username);
            return new ResponseEntity<>(new Message("Usuario eliminado existosamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El usuario no existe"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response, HttpServletRequest request){
        tokenService.delete(request.getHeader("Authorization").substring(7));
        return new ResponseEntity<>(new Message("Se ha cerrado la sesion"), HttpStatus.OK);
    }

}
