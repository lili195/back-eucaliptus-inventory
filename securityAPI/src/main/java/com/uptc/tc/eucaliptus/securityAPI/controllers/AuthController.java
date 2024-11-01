package com.uptc.tc.eucaliptus.securityAPI.controllers;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.dtos.*;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Message;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.RecoveryCode;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.Role;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.enums.RoleList;
import com.uptc.tc.eucaliptus.securityAPI.infraestructure.services.*;
import com.uptc.tc.eucaliptus.securityAPI.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
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

import java.time.LocalDateTime;
import java.util.Optional;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;
    private final EmailService emailService;
    private final RecoveryCodeService recoveryCodeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, RoleService roleService, EmailService emailService, RecoveryCodeService recoveryCodeService, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.roleService = roleService;
        this.emailService = emailService;
        this.recoveryCodeService = recoveryCodeService;
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

            User user = userService.getByUserName(username).get();
            String token = jwtProvider.generateToken(loginUser.getUsername(), user.getRole(), user.getEmail());
            Claims claims = jwtProvider.getAllClaims(token);
            TokenDTO tokenDTO = new TokenDTO(token, claims.get("role", String.class), username, claims.get("email", String.class));
            return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Credenciales invalidas"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addSeller")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addSeller(@Valid @RequestBody UserDTO userDTO){
        if (userService.getByEmail(userDTO.getEmail()).isPresent())
            return new ResponseEntity<>(new Message("El email ya existe"), HttpStatus.BAD_REQUEST);
        Role role = roleService.save(new Role(RoleList.ROLE_SELLER));;
        User userEntity = new User(userDTO.getUsername(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()),
                role);
        if(!userService.existByUserName(userDTO.getUsername())) {
            User userSaved = userService.save(userEntity);
            UserDTO userSavedDTO = new UserDTO(userSaved.getUsername(), userSaved.getEmail(), userDTO.getPassword(), userDTO.getRole());
            return new ResponseEntity<>(userSavedDTO, HttpStatus.CREATED);
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

    @PutMapping("/updateUserInfo")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Object> updateUserInfo(@RequestBody UpdateUserDTO updateUserDTO, HttpServletRequest request){
        try{
            if (!userService.existByUserName(updateUserDTO.getOldUsername()))
                return new ResponseEntity<>(new Message("El usuario no existe"), HttpStatus.BAD_REQUEST);
            if (userService.existByUserName(updateUserDTO.getNewUsername()) && !updateUserDTO.getNewUsername().equals(updateUserDTO.getOldUsername()))
                return new ResponseEntity<>(new Message("El username ya existe"), HttpStatus.CONFLICT);
            String headerToken = request.getHeader("Authorization");
            if(headerToken != null && headerToken.startsWith("Bearer ")) {
                String token = headerToken.substring(7);
                if (!updateUserDTO.getOldUsername().equals(jwtProvider.getUsernameFromToken(token)))
                    return new ResponseEntity<>(new Message("No tienes permiso"), HttpStatus.UNAUTHORIZED);
                userService.updateUser(updateUserDTO);
                return new ResponseEntity<>(new Message("Usuario actualizado"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new Message("No se puede actualizar el usuario"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteSeller")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteSeller(@RequestParam String username ){
        if(userService.existByUserName(username)){
            userService.delete(username);
            return new ResponseEntity<>(new Message("Usuario eliminado existosamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El usuario no existe"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response, HttpServletRequest request){
        return new ResponseEntity<>(new Message("Se ha cerrado la sesion"), HttpStatus.OK);
    }

    @PostMapping("/requestRecoveryPassword/{email}")
    public ResponseEntity<Object> requestRecoveryPassword(@PathVariable String email){
        try{
            recoveryCodeService.deleteExpiratedCodes();
            Optional<User> opUser = userService.getByEmail(email);
            if(opUser.isEmpty())
                return new ResponseEntity<>(new Message("Este email no es no esta registrado"), HttpStatus.NOT_FOUND);
            User user = opUser.get();
            int code = recoveryCodeService.generateCode();
            LocalDateTime expirationDate = recoveryCodeService.getExpirationDate();
            RecoveryCode recoveryCode = recoveryCodeService.saveNewRecoveryCode(new RecoveryCode(code, user, expirationDate));
            emailService.sendEmailPasswordRecovery(user.getEmail(), code);
            return new ResponseEntity<>(recoveryCode, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validateRecoveryCode")
    public ResponseEntity<Object> validateRecoveryCode(@RequestBody RecoveryCodeDTO recoveryCodeDTO){
        try{
            recoveryCodeService.deleteExpiratedCodes();
            Optional<User> opUser = userService.getByEmail(recoveryCodeDTO.getEmail());
            if(opUser.isEmpty())
                return new ResponseEntity<>(new Message("Este email no es no esta registrado"), HttpStatus.NOT_FOUND);
            User user = opUser.get();
            if (!recoveryCodeService.existsByCodeAndUser(recoveryCodeDTO.getCode(), user.getId()))
                return new ResponseEntity<>(new Message("Codigo no valido"), HttpStatus.BAD_REQUEST);
            RecoveryCode recoveryCode = recoveryCodeService.findByCode(recoveryCodeDTO.getCode()).get();
            recoveryCode.setExpiryDate(LocalDateTime.now().plusDays(1));
            recoveryCodeService.saveNewRecoveryCode(recoveryCode);
            return new ResponseEntity<>(new Message("Codigo valido"), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/recoveryPassword")
    public ResponseEntity<Object> recoveryPassword(@RequestBody RecoveryPasswordDTO recoveryPasswordDTO){
        try{
            Optional<User> opUser = userService.getByEmail(recoveryPasswordDTO.getEmail());
            if(opUser.isEmpty())
                return new ResponseEntity<>(new Message("Este email no es no esta registrado"), HttpStatus.NOT_FOUND);
            User user = opUser.get();
            user.setPassword(passwordEncoder.encode(recoveryPasswordDTO.getNewPassword()));
            userService.save(user);
            recoveryCodeService.deleteByCode(recoveryPasswordDTO.getCode());
            return new ResponseEntity<>(new Message("Contraseña"), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new Message("Intente de nuevo mas tarde"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
