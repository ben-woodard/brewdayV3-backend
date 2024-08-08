package com.coderscampus.SpringSecurityJWTDemo.dto.response;

import com.coderscampus.SpringSecurityJWTDemo.domain.User;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {

    private User user;
    private Cookie accessCookie;
    private Cookie refreshCookie;

}
