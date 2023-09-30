package com.test_assigment.restful_users.controller;

import com.test_assigment.restful_users.AbstractTest;
import com.test_assigment.restful_users.JsonUtil;
import com.test_assigment.restful_users.entity.User;
import com.test_assigment.restful_users.error_handling.exception.ErrorInfo;
import com.test_assigment.restful_users.error_handling.exception.NotFoundException;
import com.test_assigment.restful_users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static com.test_assigment.restful_users.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Value("${user.age.restriction}")
    private int ageRestriction;

    @Test
    void create() throws Exception {
        User newUser = getNew();
        MvcResult result = mockMvc.perform(post(USERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(newUser)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        User actual = JsonUtil.readValueFromJson(getContentAsString(result), User.class);

        assertEquals(newUser, actual);
    }

    @Test
    void createNotNew() throws Exception {
        User newUser = getNew();
        newUser.setId(1);
        MvcResult result = mockMvc.perform(post(USERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        ErrorInfo actual = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);

        assertEquals(ErrorMessage.NOT_NEW.getTitle(), actual.details()[0]);
    }

    @Test
    void createRestrictedAge() throws Exception {
        User newUser = getNew();
        LocalDate age17 = LocalDate.of(LocalDate.now().getYear() - 17, 1, 1);
        newUser.setBirthdate(age17);
        MvcResult result = mockMvc.perform(post(USERS_REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(newUser)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        ErrorInfo actual = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);
        String expected = ErrorMessage.AGE_VIOLATION.getTitle() + ageRestriction;

        assertEquals(expected, actual.details()[0]);
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        mockMvc.perform(put(USERS_REST_URL + "/" + updated.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(updated, userRepository.findById(updated.getId()).orElse(null));
    }

    @Test
    void updateInconsistentId() throws Exception {
        User updated = getUpdated();
        int invalidId = 88;
        MvcResult result = mockMvc.perform(put(USERS_REST_URL + "/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(updated)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        ErrorInfo actual = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);
        String expected = ErrorMessage.ID_INCONSISTENT.getTitle() + invalidId;

        assertEquals(expected, actual.details()[0]);
    }

    @Test
    void patchUpdate() throws Exception {
        User patch = getPatch();
        mockMvc.perform(patch(USERS_REST_URL + "/" + patch.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(patch)))
                .andDo(print())
                .andExpect(status().isNoContent());

        User actual = userRepository.findById(patch.getId()).orElseThrow(() -> new NotFoundException(""));

        assertEquals(patch.getAddress(), actual.getAddress());
    }

    @Test
    void patchUpdateInvalidDate() throws Exception {
        User patch = getPatch();
        patch.setBirthdate(LocalDate.MAX);
        MvcResult result = mockMvc.perform(patch(USERS_REST_URL + "/" + patch.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(patch)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        ErrorInfo actual = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);

        assertEquals(ErrorMessage.INVALID_DATE.getTitle(), actual.details()[0]);
    }

    @Test
    void patchUpdateInvalidEmail() throws Exception {
        User patch = getPatch();
        patch.setEmail("asdas.@dadcom...");
        MvcResult result = mockMvc.perform(patch(USERS_REST_URL + "/" + patch.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(patch)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        ErrorInfo actual = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);

        assertEquals(ErrorMessage.INVALID_EMAIL.getTitle(), actual.details()[0]);
    }

    @Test
    void patchUpdateBlankAddress() throws Exception {
        User patch = getPatch();
        patch.setAddress("    ");
        MvcResult result = mockMvc.perform(patch(USERS_REST_URL + "/" + patch.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValueToJson(patch)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        ErrorInfo actual = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);
        String expected = "Address" + ErrorMessage.BLANK.getTitle();

        assertEquals(expected, actual.details()[0]);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USERS_REST_URL + "/" + bob.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> userRepository.findById(bob.getId())
                .orElseThrow(() -> new NotFoundException("")));
    }

    @Test
    void deleteNotFound() throws Exception {
        int invalidId = 88;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(USERS_REST_URL + "/" + invalidId))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        ErrorInfo actual = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);
        String expected = ErrorMessage.NOT_FOUND.getTitle() + invalidId;

        assertEquals(expected, actual.details()[0]);
    }

    @Test
    void getByBirthdateRange() throws Exception {
        MvcResult result = mockMvc.perform(get(USERS_REST_URL + "?from=" + FROM +
                        "&to=" + TO))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<User> actual = JsonUtil.readValuesFromJson(getContentAsString(result), User.class);

        assertIterableEquals(FILTERED, actual);
    }

    @Test
    void getByBirthdateRangeInvalidFilter() throws Exception {
        MvcResult result = mockMvc.perform(get(USERS_REST_URL + "?from=" + TO +
                        "&to=" + FROM))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        ErrorInfo actual = JsonUtil.readValueFromJson(getContentAsString(result), ErrorInfo.class);

        assertEquals(ErrorMessage.INVALID_FILTER.getTitle(), actual.details()[0]);
    }
}