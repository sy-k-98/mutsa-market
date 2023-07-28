package com.likelion.market.controller;

import com.likelion.market.domain.dto.ResponseDto;
import com.likelion.market.domain.dto.UserLoginDto;
import com.likelion.market.domain.dto.jwt.RequestUserRegisterDto;
import com.likelion.market.domain.dto.jwt.TokenDto;
import com.likelion.market.domain.entity.CustomUserDetails;
import com.likelion.market.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginDto dto) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        TokenDto token = new TokenDto();
        token.setToken(jwtTokenUtils.generateToken(userDetails));
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody RequestUserRegisterDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        manager.createUser(CustomUserDetails.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .build());

        return ResponseEntity.ok(ResponseDto.getMessage("회원가입이 완료되었습니다."));
    }


}
