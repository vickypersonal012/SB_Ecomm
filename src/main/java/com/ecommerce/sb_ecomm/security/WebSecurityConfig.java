package com.ecommerce.sb_ecomm.security;

import com.ecommerce.sb_ecomm.model.AppRole;
import com.ecommerce.sb_ecomm.model.Role;
import com.ecommerce.sb_ecomm.model.User;
import com.ecommerce.sb_ecomm.repositories.RoleRepository;
import com.ecommerce.sb_ecomm.repositories.UserRepository;
import com.ecommerce.sb_ecomm.security.jwt.AuthEntryPointJwt;
import com.ecommerce.sb_ecomm.security.jwt.AuthTokenFilter;
import com.ecommerce.sb_ecomm.security.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final AuthTokenFilter authTokenFilter;


    @Autowired
    public WebSecurityConfig(UserDetailServiceImpl userDetailServiceImpl, AuthEntryPointJwt authEntryPointJwt, AuthTokenFilter authTokenFilter) {
        this.userDetailServiceImpl = userDetailServiceImpl;
        this.authEntryPointJwt = authEntryPointJwt;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.
                        authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(sessionManagement -> sessionManagement.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.
                        requestMatchers("/api/auth/signin").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // .requestMatchers("/api/public/**").permitAll()
                        // .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());
        http.headers(headers ->headers.frameOptions(
                frameOptions -> frameOptions.sameOrigin()
        ));
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs/**", "/swagger-resources/**",
                "/webjars/**", "/configuration/security", "/swagger-resources"));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseGet(() -> {
                Role newUserRole = new Role(AppRole.ROLE_USER);
                return roleRepository.save(newUserRole);
            });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseGet(() -> {
                Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                return roleRepository.save(newSellerRole);
            });

            Role adminrRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseGet(() -> {
                Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                return roleRepository.save(newAdminRole);
            });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(adminrRole);

            //create users if not present
            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", passwordEncoder.encode("password1"), "user1@example.com"
                        );
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1", passwordEncoder.encode("password2"),
                        "seller1@example.com");
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", passwordEncoder.encode("password3"), "admin@example.com");
                userRepository.save(admin);
            }

            //updating Roles for Existing users

            userRepository.findByUserName("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("seller1").ifPresent(user -> {
                user.setRoles(sellerRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("admin").ifPresent(user -> {
                user.setRoles(adminRoles);
                userRepository.save(user);
            });

        };
    }

}
