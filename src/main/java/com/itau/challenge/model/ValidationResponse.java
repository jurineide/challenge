package com.itau.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponse {
    private boolean valid;
    private String errorCode;

    public static ValidationResponse success() {
        return new ValidationResponse(true, null);
    }

    public static ValidationResponse error(ErrorCode errorCode) {
        return new ValidationResponse(false, errorCode.name());
    }
}
