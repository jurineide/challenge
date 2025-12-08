package com.itau.challenge.validator;

import com.itau.challenge.exception.ValidationException;
import com.itau.challenge.util.PrimeNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SeedValidator implements ClaimValidator{
    private static final String CLAIM_NAME = "Seed";
    private final PrimeNumberUtil primeNumberUtil;

    public SeedValidator(PrimeNumberUtil primeNumberUtil) {
        this.primeNumberUtil = primeNumberUtil;
    }

    @Override
    public void validate(Map<String, Object> claims) {
        if (!claims.containsKey(CLAIM_NAME)) {
            String message = String.format("Claim '%s' is required", CLAIM_NAME);
            log.warn(message);
            throw new ValidationException(message);
        }

        Object seedValue = claims.get(CLAIM_NAME);
        if (seedValue == null) {
            String message = String.format("Claim '%s' cannot be null", CLAIM_NAME);
            log.warn(message);
            throw new ValidationException(message);
        }

        String seedStr = seedValue.toString();

        try {
            long seed = Long.parseLong(seedStr);

            if (!primeNumberUtil.isPrime(seed)) {
                String message = String.format("Claim '%s' must be a prime number, but found: %d",
                        CLAIM_NAME, seed);
                log.warn(message);
                throw new ValidationException(message);
            }

        } catch (NumberFormatException e) {
            String message = String.format("Claim '%s' must be a valid number, but found: '%s'",
                    CLAIM_NAME, seedStr);
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    @Override
    public String getClaimName() {
        return CLAIM_NAME;
    }

}
