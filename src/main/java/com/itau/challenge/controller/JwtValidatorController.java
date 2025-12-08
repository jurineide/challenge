package com.itau.challenge.controller;
import com.itau.challenge.model.ValidationResponse;
import com.itau.challenge.service.JwtValidationService;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController@RequestMapping("/api/v1/jwt")
@Validatedpublic
class JwtValidatorController {
    private final JwtValidationService jwtValidationService;
    public JwtValidatorController(JwtValidationService jwtValidationService) {
        this.jwtValidationService = jwtValidationService;    }
    @GetMapping("/validate")
    public ResponseEntity<ValidationResponse> validateJwt(
            @RequestParam @NotBlank(message = "JWT token is required") String jwt) {
        ValidationResponse response = jwtValidationService.validate(jwt);
        log.info("JWT validation result: valid={}, errorCode={}", response.isValid(), response.getErrorCode());
        return ResponseEntity.ok(response);
    }
}