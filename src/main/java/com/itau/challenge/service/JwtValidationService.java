package com.itau.challenge.service;

import com.itau.challenge.model.ValidationResponse;
import com.itau.challenge.exception.InvalidJwtException;
import com.itau.challenge.exception.ValidationException;
import com.itau.challenge.model.ErrorCode;
import com.itau.challenge.validator.ClaimCountValidator;
import com.itau.challenge.validator.ClaimValidator;
import com.itau.challenge.validator.JwtStructureValidator;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;


@Slf4j
@Service
public class JwtValidationService {
    private final JwtStructureValidator jwtStructureValidator;
    private final ClaimCountValidator claimCountValidator;
    private final List<ClaimValidator> claimValidators;
    private final MeterRegistry meterRegistry;

    public JwtValidationService(
            JwtStructureValidator jwtStructureValidator,
            ClaimCountValidator claimCountValidator,
            List<ClaimValidator> claimValidators,
            MeterRegistry meterRegistry) {
        this.jwtStructureValidator = jwtStructureValidator;
        this.claimCountValidator = claimCountValidator;
        this.claimValidators = claimValidators;
        this.meterRegistry = meterRegistry;
    }

    public ValidationResponse validate(String jwtToken) {

        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            log.info("Starting JWT validation");


            Counter.builder("jwt.validation.total")
                    .description("Total number of JWT validations")
                    .register(meterRegistry)
                    .increment();


            Map<String, Object> claims = jwtStructureValidator.validateAndParse(jwtToken);


            claimCountValidator.validate(claims);


            for (ClaimValidator validator : claimValidators) {

                if (!(validator instanceof ClaimCountValidator)) {
                    validator.validate(claims);
                }
            }


            Counter.builder("jwt.validation.success")
                    .description("Number of successful JWT validations")
                    .register(meterRegistry)
                    .increment();

            return ValidationResponse.success();

        } catch (InvalidJwtException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            recordErrorMetric(ErrorCode.ERR001);
            return ValidationResponse.error(ErrorCode.ERR001);

        } catch (ValidationException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            ValidationResponse response = mapValidationExceptionToErrorCode(e);
            if (response.getErrorCode() != null) {
                try {
                    recordErrorMetric(ErrorCode.valueOf(response.getErrorCode()));
                } catch (IllegalArgumentException ex) {
                    recordErrorMetric(ErrorCode.ERR009);
                }
            } else {
                recordErrorMetric(ErrorCode.ERR009);
            }
            return response;

        } catch (Exception e) {
            log.error("Unexpected error during JWT validation", e);
            recordErrorMetric(ErrorCode.ERR009);
            return ValidationResponse.error(ErrorCode.ERR009);

        } finally {

            sample.stop(Timer.builder("jwt.validation.duration")
                    .description("Time taken to validate JWT")
                    .register(meterRegistry));
        }
    }


    private void recordErrorMetric(ErrorCode errorCode) {
        Counter.builder("jwt.validation.error")
                .description("Number of failed JWT validations")
                .tag("error_code", errorCode.name())
                .register(meterRegistry)
                .increment();
    }


    private ValidationResponse mapValidationExceptionToErrorCode(ValidationException e) {
        String message = e.getMessage();

        if (message.contains("exactly 3 claims")) {
            return ValidationResponse.error(ErrorCode.ERR002);
        }
        if (message.contains("Name") && message.contains("cannot contain numeric characters")) {
            return ValidationResponse.error(ErrorCode.ERR003);
        }
        if (message.contains("Name") && message.contains("exceeds maximum length")) {
            return ValidationResponse.error(ErrorCode.ERR004);
        }
        if (message.contains("Role") && message.contains("must be one of")) {
            return ValidationResponse.error(ErrorCode.ERR005);
        }
        if (message.contains("Seed") && message.contains("must be a prime number")) {
            return ValidationResponse.error(ErrorCode.ERR006);
        }
        if (message.contains("Seed") && message.contains("must be a valid number")) {
            return ValidationResponse.error(ErrorCode.ERR007);
        }
        if (message.contains("is required") || message.contains("cannot be null")) {
            return ValidationResponse.error(ErrorCode.ERR008);
        }


        return ValidationResponse.error(ErrorCode.ERR009);
    }
}
