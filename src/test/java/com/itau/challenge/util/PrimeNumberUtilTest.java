package com.itau.challenge.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PrimeNumberUtil Tests")
class PrimeNumberUtilTest {

    private PrimeNumberUtil primeNumberUtil;

    @BeforeEach
    void setUp() {
        primeNumberUtil = new PrimeNumberUtil();
    }

    @Test
    @DisplayName("Should return false for numbers less than 2")
    void shouldReturnFalseForNumbersLessThanTwo() {
        assertFalse(primeNumberUtil.isPrime(0));
        assertFalse(primeNumberUtil.isPrime(1));
        assertFalse(primeNumberUtil.isPrime(-1));
    }

    @Test
    @DisplayName("Should return true for 2")
    void shouldReturnTrueForTwo() {
        assertTrue(primeNumberUtil.isPrime(2));
    }

    @Test
    @DisplayName("Should return false for even numbers greater than 2")
    void shouldReturnFalseForEvenNumbersGreaterThanTwo() {
        assertFalse(primeNumberUtil.isPrime(4));
        assertFalse(primeNumberUtil.isPrime(6));
        assertFalse(primeNumberUtil.isPrime(8));
        assertFalse(primeNumberUtil.isPrime(100));
    }

    @Test
    @DisplayName("Should return true for prime numbers")
    void shouldReturnTrueForPrimeNumbers() {
        assertTrue(primeNumberUtil.isPrime(3));
        assertTrue(primeNumberUtil.isPrime(5));
        assertTrue(primeNumberUtil.isPrime(7));
        assertTrue(primeNumberUtil.isPrime(11));
        assertTrue(primeNumberUtil.isPrime(13));
        assertTrue(primeNumberUtil.isPrime(17));
        assertTrue(primeNumberUtil.isPrime(19));
        assertTrue(primeNumberUtil.isPrime(23));
        assertTrue(primeNumberUtil.isPrime(29));
        assertTrue(primeNumberUtil.isPrime(7841));
        assertTrue(primeNumberUtil.isPrime(88037));
        assertTrue(primeNumberUtil.isPrime(14627));
    }

    @Test
    @DisplayName("Should return false for composite numbers")
    void shouldReturnFalseForCompositeNumbers() {
        assertFalse(primeNumberUtil.isPrime(4));
        assertFalse(primeNumberUtil.isPrime(9));
        assertFalse(primeNumberUtil.isPrime(15));
        assertFalse(primeNumberUtil.isPrime(21));
        assertFalse(primeNumberUtil.isPrime(25));
        assertFalse(primeNumberUtil.isPrime(100));
    }

    @Test
    @DisplayName("Should handle large prime numbers")
    void shouldHandleLargePrimeNumbers() {
        assertTrue(primeNumberUtil.isPrime(7919));
        assertTrue(primeNumberUtil.isPrime(104729));
    }
}

