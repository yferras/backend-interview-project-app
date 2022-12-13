package com.ninjaone.backendinterviewproject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import com.ninjaone.backendinterviewproject.common.IService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        classes = {BackendInterviewProjectApplication.class}
)
@AutoConfigureMockMvc
@AllArgsConstructor
public abstract class AbstractControllerITest<I extends Serializable, D extends IBusinessDto> {

    /**
     * Pattern description.
     * Any substring:
     * <ol>
     *     <li>Containing the prefix: E::</li>
     *     <li>Followed by any word</li>
     *     <li>Then, between parenthesis a key = value combination, where the key is a word and the value anything.</li>
     *     <li>Finally, the string: Not found.</li>
     * </ol>
     */
    public static final Pattern PATTERN_MESSAGE_NOT_FOUND = Pattern.compile("E::(\\w+)\\((\\w+)\\s*=\\s*(.+)\\) Not found\\.", Pattern.CASE_INSENSITIVE);
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    protected IService<I, D> service;


    /**
     * <p>
     * Checks the validation errors.
     * </p>
     * <p>
     * When a validation error occurs the response has the status: 422-UNPROCESSABLE ENTITY.
     * </p>
     *
     * @param fieldName     the name of the field with the errors.
     * @param resultActions the instance of {@link ResultActions} to check.
     * @throws Exception if any error occurs.
     */
    protected static void checkUnprocessableEntity(String fieldName, ResultActions resultActions) throws Exception {
        resultActions
                // Checks for the correct STATUS: 422-UNPROCESSABLE ENTITY.
                .andExpect(status().isUnprocessableEntity())
                // Verifies that the field name exist.
                .andExpect(jsonPath(MessageFormat.format("$.{0}", fieldName)).exists())
                // Verifies that the field is an array.
                .andExpect(jsonPath(MessageFormat.format("$.{0}", fieldName)).isArray())
                // Verifies that the array is not empty.
                .andExpect(jsonPath(MessageFormat.format("$.{0}", fieldName)).isNotEmpty());
    }

    /**
     * <p>
     * Checks the errors relatives to data conflicts. E.g.: data duplication.
     * </p>
     * <p>
     * When a conflict occurs the response has the status: 409-CONFLICT.
     * </p>
     *
     * @param errorMessage  the error message.
     * @param resultActions the instance of {@link ResultActions} to check.
     * @throws Exception if any error occurs.
     */
    protected static void checkIsConflict(String errorMessage, ResultActions resultActions) throws Exception {
        resultActions
                // Checks for the correct STATUS: 409-CONFLICT.
                .andExpect(status().isConflict())
                // Verifies the message error.
                .andExpect(content().string(errorMessage));
    }

    protected static MessageNotFoundDescriptor checkNotFound(ResultActions resultActions) throws Exception {
        MvcResult result = resultActions.andExpect(status().isNotFound()).andReturn();
        String content = result.getResponse().getContentAsString();
        Matcher matcher = PATTERN_MESSAGE_NOT_FOUND.matcher(content);
        assertTrue(matcher.find());

        return MessageNotFoundDescriptor.builder()
                .entityName(matcher.group(1))
                .fieldName(matcher.group(2))
                .value(matcher.group(3))
                .build();
    }

    @Getter
    @Builder
    protected static class MessageNotFoundDescriptor {
        private String entityName;
        private String fieldName;
        private String value;
    }


}
