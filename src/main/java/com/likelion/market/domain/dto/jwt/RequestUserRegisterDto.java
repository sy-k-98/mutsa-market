package com.likelion.market.domain.dto.jwt;

import lombok.Data;

@Data
public class RequestUserRegisterDto {
    private String username;
    private String password;
    private String passwordCheck;
    private String phone;
    private String email;
    private String address;
}
