package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.domain.exception.InvalidCredentialsException;
import com.pedrocmoreira.garagesystem.infrastructure.security.JwtTokenProvider;
import com.pedrocmoreira.garagesystem.presentation.dto.AuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login e geração e token JWT")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Value("${jwt.expiration-ms}")
    private long expiracaoMs;

    @PostMapping("/login")
    @Operation(summary = "Realizar o login e obter token JWT")
    public AuthDTO.TokenResponse login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            String token = tokenProvider.generateToken(auth);
            return new AuthDTO.TokenResponse(token, "Bearer", expiracaoMs);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
    }
}
