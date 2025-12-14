package com.itau.challenge.validator;

import com.itau.challenge.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NameValidator Tests")
class NameValidatorTest {

    private NameValidator nameValidator;

    @BeforeEach
    void setUp() {
        nameValidator = new NameValidator();
    }

    @Test
    @DisplayName("Should validate name without numbers")
    void shouldValidateNameWithoutNumbers() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Toninho Araujo");
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");

        assertDoesNotThrow(() -> nameValidator.validate(claims));
    }

    @Test
    @DisplayName("Should throw exception when name contains numbers")
    void shouldThrowExceptionWhenNameContainsNumbers() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "M4ria Olivia");
        claims.put("Role", "External");
        claims.put("Seed", "88037");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> nameValidator.validate(claims));

        assertTrue(exception.getMessage().contains("cannot contain numeric characters"));
    }

    @Test
    @DisplayName("Should throw exception when name exceeds max length")
    void shouldThrowExceptionWhenNameExceedsMaxLength() {
        Map<String, Object> claims = new HashMap<>();
        String longName = "A".repeat(257);
        claims.put("Name", longName);
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> nameValidator.validate(claims));

        assertTrue(exception.getMessage().contains("exceeds maximum length"));
    }

    @Test
    @DisplayName("Should accept name with exactly 256 characters")
    void shouldAcceptNameWithExactly256Characters() {
        Map<String, Object> claims = new HashMap<>();
        String name = "A".repeat(256);
        claims.put("Name", name);
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");

        assertDoesNotThrow(() -> nameValidator.validate(claims));
    }

    @Test
    @DisplayName("Should throw exception when Name claim is missing")
    void shouldThrowExceptionWhenNameClaimIsMissing() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> nameValidator.validate(claims));

        assertTrue(exception.getMessage().contains("is required"));
    }

    @Test
    @DisplayName("Should throw exception when Name claim is null")
    void shouldThrowExceptionWhenNameClaimIsNull() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", null);
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> nameValidator.validate(claims));

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    @DisplayName("Should accept name with special characters")
    void shouldAcceptNameWithSpecialCharacters() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "José da Silva-Santos");
        claims.put("Role", "Member");
        claims.put("Seed", "7841");

        assertDoesNotThrow(() -> nameValidator.validate(claims));
    }
}

