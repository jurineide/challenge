package com.itau.challenge.validator;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClaimCountValidator implements ClaimValidator {

    private static final int EXPECTED_CLAIM_COUNT = 3;

    @Override
    public void validate(java.util.Map<String, Object> claims) {
        if (claims.size() != EXPECTED_CLAIM_COUNT) {
            String message = String.format("JWT must contain exactly %d claims, but found %d",
                    EXPECTED_CLAIM_COUNT, claims.size());
            log.warn(message);
            throw new com.itau.challenge.exception.ValidationException(message);
        }
    }

    @Override
    public String getClaimName() {
        return "ClaimCount";
    }
}
