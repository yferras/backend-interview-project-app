package com.ninjaone.backendinterviewproject.common.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {


    @Test
    void addValidation() {
        Group<Object> group = new Group<>("test-group");
        assertTrue(group.isEmpty());
        group.addValidation(IValidation.requireNonNull("non-null", false));
        assertFalse(group.isEmpty());
    }

    @Test
    void testCondition() {
        Group<String> group = new Group<>("test-group");
        assertTrue(group.isEmpty());
        group.addValidation(IValidation.requireNonNull("non-null", false));
    }

    @Test
    void getMessage() {
        Group<String> group = new Group<>("test-group");
        group
                .addValidation(IValidation.requireNonNull("A", false))
                .addValidation(IValidation.requireNonEmpty("B", true))
                .addValidation(IValidation.requireNonBlank("C", true))
        ;
        assertTrue(group.testCondition(null));
        assertEquals("A", group.getMessage());
        assertEquals(1, group.getMessages().size());

        assertTrue(group.testCondition(""));
        assertEquals("B & C", group.getMessage());
        assertEquals(2, group.getMessages().size());

        assertTrue(group.testCondition("   "));
        assertEquals("C", group.getMessage());
        assertEquals(1, group.getMessages().size());
    }

    @Test
    void getName() {
        assertEquals("test-group", new Group<>("test-group").getName());
    }

    @Test
    void evalNext() {
        assertTrue(new Group<>("test-group").evalNext());
        assertTrue(new Group<>("test-group", true).evalNext());
        assertFalse(new Group<>("test-group", false).evalNext());
    }
}