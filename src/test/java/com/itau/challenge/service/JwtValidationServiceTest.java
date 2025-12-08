package com.itau.challenge.service;

import com.itau.challenge.model.ErrorCode;
import com.itau.challenge.model.ValidationResponse;
import com.itau.challenge.util.PrimeNumberUtil;
import com.itau.challenge.validator.*;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JwtValidationService Integration Tests")
class JwtValidationServiceTest {

    private JwtValidationService jwtValidationService;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        JwtStructureValidator jwtStructureValidator = new JwtStructureValidator();
        ClaimCountValidator claimCountValidator = new ClaimCountValidator();
        PrimeNumberUtil primeNumberUtil = new PrimeNumberUtil();
        meterRegistry = new SimpleMeterRegistry();

        List<ClaimValidator> claimValidators = List.of(
                claimCountValidator,
                new NameValidator(),
                new RoleValidator(),
                new SeedValidator(primeNumberUtil)
        );

        jwtValidationService = new JwtValidationService(
                jwtStructureValidator,
                claimCountValidator,
                claimValidators,
                meterRegistry
        );
    }

    @Test
    @DisplayName("Caso 1: Should validate valid JWT with correct claims")
    void caso1_shouldValidateValidJwtWithCorrectClaims() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        ValidationResponse response = jwtValidationService.validate(jwt);

        assertTrue(response.isValid(), "JWT should be valid");
        assertNull(response.getErrorCode(), "Error code should be null for valid JWT");
    }

    @Test
    @DisplayName("Caso 2: Should reject invalid JWT structure")
    void caso2_shouldRejectInvalidJwtStructure() {
        String jwt = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        ValidationResponse response = jwtValidationService.validate(jwt);

        assertFalse(response.isValid(), "JWT should be invalid due to structure");
        assertEquals(ErrorCode.ERR001.name(), response.getErrorCode(), "Should return ERR001 for invalid structure");
    }

    @Test
    @DisplayName("Caso 3: Should reject JWT with name containing numbers")
    void caso3_shouldRejectJwtWithNameContainingNumbers() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";

        ValidationResponse response = jwtValidationService.validate(jwt);

        assertFalse(response.isValid(), "JWT should be invalid - Name contains numbers");
        assertEquals(ErrorCode.ERR003.name(), response.getErrorCode(), "Should return ERR003 for name with numbers");
    }

    @Test
    @DisplayName("Caso 4: Should reject JWT with more than 3 claims")
    void caso4_shouldRejectJwtWithMoreThanThreeClaims() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";

        ValidationResponse response = jwtValidationService.validate(jwt);

        assertFalse(response.isValid(), "JWT should be invalid - more than 3 claims");
        assertEquals(ErrorCode.ERR002.name(), response.getErrorCode(), "Should return ERR002 for incorrect claim count");
    }

    @Test
    @DisplayName("Should reject JWT with invalid role")
    void shouldRejectJwtWithInvalidRole() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiSW52YWxpZFJvbGUiLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUZXN0IFVzZXIifQ.invalid";

        ValidationResponse response = jwtValidationService.validate(jwt);

        assertFalse(response.isValid(), "JWT should be invalid - invalid role");
        assertEquals(ErrorCode.ERR005.name(), response.getErrorCode(), "Should return ERR005 for invalid role");
    }

    @Test
    @DisplayName("Should reject JWT with non-prime seed")
    void shouldRejectJwtWithNonPrimeSeed() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MiIsIk5hbWUiOiJUZXN0IFVzZXIifQ.invalid";

        ValidationResponse response = jwtValidationService.validate(jwt);

        assertFalse(response.isValid(), "JWT should be invalid - seed is not prime");
        assertEquals(ErrorCode.ERR006.name(), response.getErrorCode(), "Should return ERR006 for non-prime seed");
    }
}

