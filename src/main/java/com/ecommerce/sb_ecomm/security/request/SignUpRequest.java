package com.ecommerce.sb_ecomm.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Size(min = 5, max = 50, message = "Email length Should be between 10 and 50")
    private String email;
    @NotBlank
    @Size(min = 5, max = 20, message = "Username length Should be between 10 and 50")
    private String userName;
    @NotBlank
    @Size(min = 5, max = 50, message = "Password length Should be between 10 and 50")
    private String password;

    private Set<String> role;

}
