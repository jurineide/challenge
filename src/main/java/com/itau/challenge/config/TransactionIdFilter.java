package com.itau.challenge.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TransactionIdFilter  extends OncePerRequestFilter {
    private static final String TRANSACTION_ID_KEY = "transactionId";
    private static final String TRANSACTION_ID_HEADER = "X-Transaction-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String transactionId = request.getHeader(TRANSACTION_ID_HEADER);
            if (transactionId == null || transactionId.isEmpty()) {
                transactionId = UUID.randomUUID().toString();
            }

            MDC.put(TRANSACTION_ID_KEY, transactionId);

            response.setHeader(TRANSACTION_ID_HEADER, transactionId);

            filterChain.doFilter(request, response);

        } finally {

            MDC.remove(TRANSACTION_ID_KEY);
        }
    }
}