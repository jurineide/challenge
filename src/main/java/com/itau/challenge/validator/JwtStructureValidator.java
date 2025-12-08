package com.itau.challenge.validator;

import com.itau.challenge.exception.InvalidJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
public class JwtStructureValidator {

    private final ObjectMapper objectMapper;

    public JwtStructureValidator() {
        this.objectMapper = new ObjectMapper();
    }


    public Map<String, Object> validateAndParse(String jwtToken) {
        try {

            String[] parts = jwtToken.split("\\.");
            if (parts.length != 3) {
                throw new InvalidJwtException("JWT must have 3 parts separated by dots");
            }


            String payload = parts[1];
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes);


            @SuppressWarnings("unchecked")
            Map<String, Object> claims = objectMapper.readValue(decodedPayload, Map.class);

            return claims;

        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException("Invalid JWT structure: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new InvalidJwtException("Invalid JWT structure: " + e.getMessage(), e);
        }
    }
}

