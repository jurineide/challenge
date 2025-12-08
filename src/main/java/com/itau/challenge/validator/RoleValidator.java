package com.itau.challenge.validator;

import com.itau.challenge.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class RoleValidator implements ClaimValidator{
    private static final String CLAIM_NAME = "Role";
    private static final Set<String> VALID_ROLES = Set.of("Admin", "Member", "External");

    @Override
    public void validate(Map<String, Object> claims) {
        if (!claims.containsKey(CLAIM_NAME)) {
            String message = String.format("Claim '%s' is required", CLAIM_NAME);
            log.warn(message);
            throw new ValidationException(message);
        }

        Object roleValue = claims.get(CLAIM_NAME);
        if (roleValue == null) {
            String message = String.format("Claim '%s' cannot be null", CLAIM_NAME);
            log.warn(message);
            throw new ValidationException(message);
        }

        String role = roleValue.toString();

        if (!VALID_ROLES.contains(role)) {
            String message = String.format("Claim '%s' must be one of: %s, but found: '%s'",
                    CLAIM_NAME, VALID_ROLES, role);
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    @Override
    public String getClaimName() {
        return CLAIM_NAME;
    }
}
