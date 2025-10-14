package com.ecommerce.sb_ecomm.controller;

import com.ecommerce.sb_ecomm.model.AppRole;
import com.ecommerce.sb_ecomm.model.Role;
import com.ecommerce.sb_ecomm.model.User;
import com.ecommerce.sb_ecomm.repositories.RoleRepository;
import com.ecommerce.sb_ecomm.repositories.UserRepository;
import com.ecommerce.sb_ecomm.security.jwt.JwtUtils;
import com.ecommerce.sb_ecomm.security.request.LoginRequest;
import com.ecommerce.sb_ecomm.security.request.SignUpRequest;
import com.ecommerce.sb_ecomm.security.response.MessageResponse;
import com.ecommerce.sb_ecomm.security.response.UserInfoResponse;
import com.ecommerce.sb_ecomm.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager,
                          UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Invalid username or password");
            map.put("status", "false");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        //String jwtToken = jwtUtils.generateTokenFromUserName(userDetails.getUsername());
        ResponseCookie jwtCookie = jwtUtils.generateJWTCookieFromUser(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().
                map(item -> item.getAuthority()).toList();

        UserInfoResponse userInfoResponse = new UserInfoResponse
                (userDetails.getId(), userDetails.getUsername(), roles);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(userInfoResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            return new ResponseEntity<>(new MessageResponse("Error::: User is already registered"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("Error::: Email is already registered"), HttpStatus.BAD_REQUEST);
        }
        //Create New User Account
        User user = new User(signUpRequest.getUserName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> new RuntimeException("Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).
                                orElseThrow(() -> new RuntimeException("Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "user":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).
                                orElseThrow(() -> new RuntimeException("Role is not found."));
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).
                                orElseThrow(() -> new RuntimeException("Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        if (authentication == null) {
            return "";
        } else {
            return authentication.getName();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().
                map(item -> item.getAuthority()).toList();

        UserInfoResponse userInfoResponse = new UserInfoResponse
                (userDetails.getId(), userDetails.getUsername(), roles);
        return ResponseEntity.ok().body(userInfoResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJWTCookies();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).
                body(new MessageResponse("You have been successfully signed out !!"));
    }



}
