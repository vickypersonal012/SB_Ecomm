package com.ecommerce.sb_ecomm.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String jwtToken;
    private String userName;
    private List<String> roles;

    public UserInfoResponse(Long id, String userName, List<String> roles) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
    }


}
