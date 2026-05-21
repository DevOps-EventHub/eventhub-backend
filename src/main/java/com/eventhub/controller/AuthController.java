package com.eventhub.controller;

import com.eventhub.dto.auth.AuthResponseDTO;
import com.eventhub.dto.auth.LoginRequestDTO;
import com.eventhub.dto.auth.RegisterRequestDTO;
import com.eventhub.dto.auth.UserMeResponseDTO;
import com.eventhub.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserMeResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
        AuthResponseDTO response = authService.login(request);
        SecurityContext context = SecurityContextHolder.getContext();
        httpRequest.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponseDTO> me() {
        return ResponseEntity.ok(authService.me());
    }
}
