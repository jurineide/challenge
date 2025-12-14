package com.itau.challenge.validator;

import com.itau.challenge.exception.InvalidJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtStructureValidator Tests")
class JwtStructureValidatorTest {

    private JwtStructureValidator jwtStructureValidator;

    @BeforeEach
    void setUp() {
        jwtStructureValidator = new JwtStructureValidator();
    }

    @Test
    @DisplayName("Should parse valid JWT structure")
    void shouldParseValidJwtStructure() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        Map<String, Object> claims = jwtStructureValidator.validateAndParse(jwt);

        assertNotNull(claims);
        assertEquals(3, claims.size());
        assertEquals("Admin", claims.get("Role"));
        assertEquals("7841", claims.get("Seed"));
        assertEquals("Toninho Araujo", claims.get("Name"));
    }

    @Test
    @DisplayName("Should throw exception for JWT with less than 3 parts")
    void shouldThrowExceptionForJwtWithLessThanThreeParts() {
        String jwt = "invalid.jwt";

        InvalidJwtException exception = assertThrows(InvalidJwtException.class,
                () -> jwtStructureValidator.validateAndParse(jwt));

        assertTrue(exception.getMessage().contains("3 parts"));
    }

    @Test
    @DisplayName("Should throw exception for JWT with more than 3 parts")
    void shouldThrowExceptionForJwtWithMoreThanThreeParts() {
        String jwt = "part1.part2.part3.part4";

        InvalidJwtException exception = assertThrows(InvalidJwtException.class,
                () -> jwtStructureValidator.validateAndParse(jwt));

        assertTrue(exception.getMessage().contains("3 parts"));
    }

    @Test
    @DisplayName("Should throw exception for invalid base64 payload")
    void shouldThrowExceptionForInvalidBase64Payload() {
        String jwt = "header.invalid-base64!.signature";

        assertThrows(InvalidJwtException.class,
                () -> jwtStructureValidator.validateAndParse(jwt));
    }

    @Test
    @DisplayName("Should throw exception for invalid JSON in payload")
    void shouldThrowExceptionForInvalidJsonInPayload() {
        // Criar um payload base64 válido mas com JSON inválido
        String invalidJson = "not a json";
        String base64Payload = java.util.Base64.getUrlEncoder().encodeToString(invalidJson.getBytes());
        String jwt = "header." + base64Payload + ".signature";

        assertThrows(InvalidJwtException.class,
                () -> jwtStructureValidator.validateAndParse(jwt));
    }
}

