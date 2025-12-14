package com.itau.challenge.controller;

import com.itau.challenge.model.ErrorCode;
import com.itau.challenge.model.ValidationResponse;
import com.itau.challenge.service.JwtValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("JwtValidatorController Unit Tests")
class JwtValidatorControllerTest {

    private JwtValidatorController controller;
    private JwtValidationService jwtValidationService;

    @BeforeEach
    void setUp() {
        jwtValidationService = mock(JwtValidationService.class);
        controller = new JwtValidatorController(jwtValidationService);
    }

    @Test
    @DisplayName("Should return true for valid JWT")
    void shouldReturnTrueForValidJwt() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        when(jwtValidationService.validate(anyString())).thenReturn(ValidationResponse.success());

        ValidationResponse response = controller.validateJwt(jwt).getBody();

        assertNotNull(response);
        assertTrue(response.isValid());
        assertNull(response.getErrorCode());
    }

    @Test
    @DisplayName("Should return false for invalid JWT")
    void shouldReturnFalseForInvalidJwt() {
        String jwt = "invalid.jwt.token";

        when(jwtValidationService.validate(anyString())).thenReturn(ValidationResponse.error(ErrorCode.ERR001));

        ValidationResponse response = controller.validateJwt(jwt).getBody();

        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals(ErrorCode.ERR001.name(), response.getErrorCode());
    }

    @Test
    @DisplayName("Caso 1: Should validate test case 1")
    void caso1_shouldValidateTestCase1() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        when(jwtValidationService.validate(jwt)).thenReturn(ValidationResponse.success());

        ValidationResponse response = controller.validateJwt(jwt).getBody();

        assertNotNull(response);
        assertTrue(response.isValid());
        assertNull(response.getErrorCode());
    }

    @Test
    @DisplayName("Caso 2: Should reject test case 2")
    void caso2_shouldRejectTestCase2() {
        String jwt = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        when(jwtValidationService.validate(jwt)).thenReturn(ValidationResponse.error(ErrorCode.ERR001));

        ValidationResponse response = controller.validateJwt(jwt).getBody();

        assertNotNull(response);
        assertFalse(response.isValid());
        assertEquals(ErrorCode.ERR001.name(), response.getErrorCode());
    }
}

