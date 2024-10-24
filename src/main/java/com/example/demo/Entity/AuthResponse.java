package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String jwt;
    private String message;
    private boolean status;

    public AuthResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

}
