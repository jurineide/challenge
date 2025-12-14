package com.itau.challenge.validator;

import com.itau.challenge.exception.ValidationException;
import com.itau.challenge.util.PrimeNumberUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SeedValidator Tests")
class SeedValidatorTest {

    private SeedValidator seedValidator;

    @BeforeEach
    void setUp() {
        seedValidator = new SeedValidator(new PrimeNumberUtil());
    }

    @Test
    @DisplayName("Should validate prime number seed")
    void shouldValidatePrimeNumberSeed() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");
        claims.put("Seed", "7841");

        assertDoesNotThrow(() -> seedValidator.validate(claims));
    }

    @Test
    @DisplayName("Should validate another prime number seed")
    void shouldValidateAnotherPrimeNumberSeed() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Member");
        claims.put("Seed", "14627");

        assertDoesNotThrow(() -> seedValidator.validate(claims));
    }

    @Test
    @DisplayName("Should throw exception for non-prime number")
    void shouldThrowExceptionForNonPrimeNumber() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");
        claims.put("Seed", "7842"); // not prime

        ValidationException exception = assertThrows(ValidationException.class,
                () -> seedValidator.validate(claims));

        assertTrue(exception.getMessage().contains("must be a prime number"));
    }

    @Test
    @DisplayName("Should throw exception when Seed claim is missing")
    void shouldThrowExceptionWhenSeedClaimIsMissing() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> seedValidator.validate(claims));

        assertTrue(exception.getMessage().contains("is required"));
    }

    @Test
    @DisplayName("Should throw exception when Seed claim is null")
    void shouldThrowExceptionWhenSeedClaimIsNull() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");
        claims.put("Seed", null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> seedValidator.validate(claims));

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    @DisplayName("Should throw exception for non-numeric seed")
    void shouldThrowExceptionForNonNumericSeed() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");
        claims.put("Seed", "abc");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> seedValidator.validate(claims));

        assertTrue(exception.getMessage().contains("must be a valid number"));
    }

    @Test
    @DisplayName("Should validate small prime numbers")
    void shouldValidateSmallPrimeNumbers() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Name", "Test User");
        claims.put("Role", "Admin");
        claims.put("Seed", "2");

        assertDoesNotThrow(() -> seedValidator.validate(claims));
    }
}

