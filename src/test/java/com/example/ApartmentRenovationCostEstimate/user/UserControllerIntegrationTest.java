package com.example.ApartmentRenovationCostEstimate.user;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.containsInAnyOrder;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void testCreateUser_shouldReturnCreatedUser() throws Exception {
        String validUserJson = """
                {
                    "name": "Janko",
                    "surname": "Kowalski",
                    "nick": "Tester",
                    "password": "1234567",
                    "email": "janko.kowalski@gmail.com"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Janko"))
                .andExpect(jsonPath("$.data.surname").value("Kowalski"))
                .andExpect(jsonPath("$.data.nick").value("Tester"))
                .andExpect(jsonPath("$.data.email").value("janko.kowalski@gmail.com"));
    }


    @Test
    @Transactional
    void testCreateUser_shouldReturnBadRequestForInvalidEmail() throws Exception {
        String invalicEmailUserJson = """
                {
                    "name": "Janko",
                    "surname": "Kowalski",
                    "nick": "Tester",
                    "password": "1234567",
                    "email": "invalid-email"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalicEmailUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value("Invalid email format"));
    }


    @Test
    @Transactional
    void testCreateUser_shouldReturnConflictForDuplicateEmail() throws Exception {
        String userJson = """
                {
                    "name": "Janko",
                    "surname": "Kowalski",
                    "nick": "Tester",
                    "password": "1234567",
                    "email": "janko.kowalski@gmail.com"
                }
                """;

        // First time user creation
        mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJson))
                .andExpect(status().isCreated());

        // Trying to recreate with the same email
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exist!"));

    }


    @Test
    @Transactional
    void testCreateUser_shouldReturnBadRequestForInvalidData() throws Exception {
        String invalidUserJson = """
            {
                "name": "fg",
                "surname": "K",
                "nick": "T",
                "password": "",
                "email": "invalid-email"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("name", "surname", "password", "email")));

        long userCount = userRepository.count();
        assertEquals(5, userCount, "No users should be saved in the database");

    }


    @Test
    @Transactional
    void testCreateUser_shouldReturnBadRequestForMissingName() throws Exception {
        String userJson = """
            {
                "surname": "Kowalski",
                "nick": "Tester",
                "password": "1234567",
                "email": "janko.kowalski@gmail.com"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.errors").exists());
    }


    @Test
    void testGetUser_shouldReturnUserById() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 13)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User retrieved successfully"))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void testAdminGetAllUsers_shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Users retrieved successfully"))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    @Transactional
    void testUpdateUser_shouldReturnUpdatedUser() throws Exception {
        String validUserJson = """
                {
                    "name": "Janko_new",
                    "surname": "Kowalski_new",
                    "nick": "Tester_new",
                    "password": "1234567_new",
                    "email": "janko_new.kowalski@gmail.com"
                }
                """;

        mockMvc.perform(put("/api/users/{id}", 17)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validUserJson)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Janko_new"))
                .andExpect(jsonPath("$.data.surname").value("Kowalski_new"))
                .andExpect(jsonPath("$.data.nick").value("Tester_new"))
                .andExpect(jsonPath("$.data.email").value("janko_new.kowalski@gmail.com"));

    }

    @Test
    @Transactional
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void testDeleteUserByAdmin() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 17))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }



}
