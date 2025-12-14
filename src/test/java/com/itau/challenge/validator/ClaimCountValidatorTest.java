package com.itau.challenge.validator;

import com.itau.challenge.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClaimCountValidator Tests")
class ClaimCountValidatorTest {

    private ClaimCountValidator claimCountValidator;

    @BeforeEach
    void setUp() {
        claimCountValidator = new ClaimCountValidator();
    }

    @Test
    @DisplayName("Should validate exactly 3 claims")
    void shouldValidateExactlyThreeClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");

        assertDoesNotThrow(() -> claimCountValidator.validate(claims));
    }

    @Test
    @DisplayName("Should throw exception for less than 3 claims")
    void shouldThrowExceptionForLessThanThreeClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> claimCountValidator.validate(claims));

        assertTrue(exception.getMessage().contains("exactly 3 claims"));
    }

    @Test
    @DisplayName("Should throw exception for more than 3 claims")
    void shouldThrowExceptionForMoreThanThreeClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");
        claims.put("Org", "BR");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> claimCountValidator.validate(claims));

        assertTrue(exception.getMessage().contains("exactly 3 claims"));
    }

    @Test
    @DisplayName("Should throw exception for empty claims")
    void shouldThrowExceptionForEmptyClaims() {
        Map<String, Object> claims = new HashMap<>();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> claimCountValidator.validate(claims));

        assertTrue(exception.getMessage().contains("exactly 3 claims"));
    }
}

