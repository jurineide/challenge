package com.itau.challenge.validator;

import com.itau.challenge.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RoleValidator Tests")
class RoleValidatorTest {

    private RoleValidator roleValidator;

    @BeforeEach
    void setUp() {
        roleValidator = new RoleValidator();
    }

    @Test
    @DisplayName("Should validate Admin role")
    void shouldValidateAdminRole() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");

        assertDoesNotThrow(() -> roleValidator.validate(claims));
    }

    @Test
    @DisplayName("Should validate Member role")
    void shouldValidateMemberRole() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Member");
        claims.put("Seed", "7841");

        assertDoesNotThrow(() -> roleValidator.validate(claims));
    }

    @Test
    @DisplayName("Should validate External role")
    void shouldValidateExternalRole() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "External");
        claims.put("Seed", "7841");

        assertDoesNotThrow(() -> roleValidator.validate(claims));
    }

    @Test
    @DisplayName("Should throw exception for invalid role")
    void shouldThrowExceptionForInvalidRole() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "InvalidRole");
        claims.put("Seed", "7841");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> roleValidator.validate(claims));

        assertTrue(exception.getMessage().contains("must be one of"));
    }

    @Test
    @DisplayName("Should throw exception when Role claim is missing")
    void shouldThrowExceptionWhenRoleClaimIsMissing() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Seed", "7841");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> roleValidator.validate(claims));

        assertTrue(exception.getMessage().contains("is required"));
    }

    @Test
    @DisplayName("Should throw exception when Role claim is null")
    void shouldThrowExceptionWhenRoleClaimIsNull() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", null);
        claims.put("Seed", "7841");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> roleValidator.validate(claims));

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    @DisplayName("Should be case sensitive")
    void shouldBeCaseSensitive() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "admin"); // lowercase
        claims.put("Seed", "7841");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> roleValidator.validate(claims));

        assertTrue(exception.getMessage().contains("must be one of"));
    }
}

