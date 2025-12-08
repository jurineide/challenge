package com.itau.challenge.util;

import org.springframework.stereotype.Component;

@Component
public class PrimeNumberUtil {
    public boolean isPrime(long number) {
        if (number < 2) {
            return false;
        }
        if (number == 2) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }


        long sqrt = (long) Math.sqrt(number);
        for (long i = 3; i <= sqrt; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
