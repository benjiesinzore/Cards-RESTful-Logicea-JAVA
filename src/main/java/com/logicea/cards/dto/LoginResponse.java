package com.logicea.cards.dto;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String status;
    private String token;
    private String message;
}