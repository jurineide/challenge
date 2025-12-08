package com.itau.challenge.validator;

import java.util.Map;

public interface ClaimValidator {
    void validate(Map<String, Object> claims);
    String getClaimName();
}
