package com.ninjaone.backendinterviewproject.common.validation;

import com.ninjaone.backendinterviewproject.common.exception.ValidationException;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class IGroupValidationTest {

    @Getter
    private static class Person {
        private String name;

        private String lastName;
    }

    @Test
    void of() {
        IGroupValidation<Object> group = IGroupValidation.of("test-group", false);
        assertNotNull(group);
    }

    @Test
    void getSeparator() {
        IGroupValidation<Object> group = IGroupValidation.of("test-group", false);
        assertEquals(" & ", group.getSeparator());
    }

    @Test
    void checkValidations() {
        Group<String> group = new Group<>("test-group");
        group
                .addValidation(IValidation.requireNonNull("A", false))
                .addValidation(IValidation.requireNonEmpty("B", true))
                .addValidation(IValidation.requireNonBlank("C", true))
        ;

        assertThrows(ValidationException.class, () -> IGroupValidation.checkValidations(null, group));
        assertThrows(ValidationException.class, () -> IGroupValidation.checkValidations("", group));
        assertThrows(ValidationException.class, () -> IGroupValidation.checkValidations("   ", group));
    }

    @Test
    void checkGroups() {
        Person person = new Person();
        Group<Person> groupForPersonName = new Group<>("person.name");
        groupForPersonName
                .addValidation(IValidation.requireNonNull(Person::getName, "non-null", false))
                .addValidation(IValidation.requireNonEmpty(Person::getName, "non-empty", true))
                .addValidation(IValidation.requireNonBlank(Person::getName, "non-blank", true))
        ;
        Group<Person> groupForPersonLastName = new Group<>("person.lastName");
        groupForPersonLastName
                .addValidation(IValidation.of(Person::getLastName, Objects::isNull, "non-null", false))
                .addValidation(IValidation.of(Person::getLastName, String::isEmpty, "non-empty", true))
                .addValidation(IValidation.of(Person::getLastName, String::isBlank, "non-blank", true))
        ;

        List<Group<Person>> groups = List.of(groupForPersonName, groupForPersonLastName);

        // Both properties are null
        assertThrows(ValidationException.class, () -> IGroupValidation.checkGroups(person, groups));
        person.name = "James"; // OK
        person.lastName = "";  // empty
        assertThrows(ValidationException.class, () -> IGroupValidation.checkGroups(person, groups));
        person.lastName = "    "; // blank
        assertThrows(ValidationException.class, () -> IGroupValidation.checkGroups(person, groups));
        person.lastName = "Webb"; // OK
        IGroupValidation.checkGroups(person, groups);
    }
}