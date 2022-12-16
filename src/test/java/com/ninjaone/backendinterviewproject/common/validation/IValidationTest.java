package com.ninjaone.backendinterviewproject.common.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class IValidationTest {



    @ParameterizedTest
    @CsvSource(value = {
            "TEST,true",
            "test,true",
            "abcd,false",
    })
    void of(String str, boolean expected) {
        String validationMessage = "The string must be equals to 'TEST' or 'test' (case insensitive)";
        IValidation<String> validation = IValidation.of("TEST"::equalsIgnoreCase, validationMessage, false);
        assertFalse(validation.evalNext());
        assertEquals(validationMessage, validation.getMessage());
        assertEquals(expected, validation.testCondition(str));
    }

    @Test
    void requireNonNull() {
        IValidation<Object> validation = IValidation.requireNonNull("message", false);
        assertTrue(validation.testCondition(null));
        assertFalse(validation.testCondition(10));
    }

    @Test
    void requireNonEmpty() {
        IValidation<String> validation = IValidation.requireNonEmpty("message", false);
        assertTrue(validation.testCondition(""));
        assertFalse(validation.testCondition("      ")); // is blank but not empty.
        assertFalse(validation.testCondition("text"));
    }

    @Test
    void requireNonBlank() {
        IValidation<String> validation = IValidation.requireNonBlank("message", false);
        assertTrue(validation.testCondition(""));
        assertTrue(validation.testCondition("      ")); // is blank but not empty.
        assertFalse(validation.testCondition("text"));
    }
}