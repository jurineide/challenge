package com.itau.challenge.validator;

import com.itau.challenge.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class NameValidator implements ClaimValidator{
    private static final String CLAIM_NAME = "Name";
    private static final int MAX_LENGTH = 256;

    @Override
    public void validate(Map<String, Object> claims) {
        if (!claims.containsKey(CLAIM_NAME)) {
            String message = String.format("Claim '%s' is required", CLAIM_NAME);
            log.warn(message);
            throw new ValidationException(message);
        }

        Object nameValue = claims.get(CLAIM_NAME);
        if (nameValue == null) {
            String message = String.format("Claim '%s' cannot be null", CLAIM_NAME);
            log.warn(message);
            throw new ValidationException(message);
        }

        String name = nameValue.toString();


        if (name.length() > MAX_LENGTH) {
            String message = String.format("Claim '%s' exceeds maximum length of %d characters",
                    CLAIM_NAME, MAX_LENGTH);
            log.warn(message);
            throw new ValidationException(message);
        }


        if (containsNumbers(name)) {
            String message = String.format("Claim '%s' cannot contain numeric characters", CLAIM_NAME);
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    private boolean containsNumbers(String str) {
        return str.chars().anyMatch(Character::isDigit);
    }

    @Override
    public String getClaimName() {
        return CLAIM_NAME;
    }

}
