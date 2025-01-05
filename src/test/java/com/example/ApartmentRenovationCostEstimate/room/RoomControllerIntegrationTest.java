package com.example.ApartmentRenovationCostEstimate.room;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @Transactional
    void testCreateRoom_shouldReturnCreatedRoom() throws Exception {
        String validRoomJson = """
                {
                    "name": "Sypialnia",
                    "floorArea": 44.0,
                    "wallArea": 22.5
                }
                """;

        mockMvc.perform(post("/api/rooms")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validRoomJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Sypialnia"))
                .andExpect(jsonPath("$.data.floorArea").value(44.0))
                .andExpect(jsonPath("$.data.wallArea").value(22.5));

    }


    @Test
    @Transactional
    void testCreateRoom_shouldReturnBadRequestForInvalidData() throws Exception {
        String invalidRoomJson = """
                {
                    "name": "Sypialnia",
                    "floorArea": -44.0,
                    "wallArea": -22.5
                }
                """;

        mockMvc.perform(post("/api/rooms")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRoomJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[*].field", Matchers.containsInAnyOrder("floorArea", "wallArea")));

        long roomCount = roomRepository.count();
        assertEquals(3, roomCount, "No room should be saved in the database");
    }


    @Test
    void testGetRoom_shouldReturnRoomById() throws Exception{
        mockMvc.perform(get("/api/rooms/{id}", 2)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Room retrieved successfully"))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    void testGetAllRooms_shouldReturnAllRooms() throws Exception {
        mockMvc.perform(get("/api/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rooms retrieved successfully"))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    @Transactional
    void testUpdateRoom_shouldReturnUpdateRoom() throws Exception {
        String validRoomJson = """
                {
                    "id": 2,
                    "name": "Łazienka",
                    "floorArea": 4.0,
                    "wallArea": 2.5
                }
                """;

        mockMvc.perform(put("/api/rooms/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRoomJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Łazienka"))
                .andExpect(jsonPath("$.data.floorArea").value(4.0))
                .andExpect(jsonPath("$.data.wallArea").value(2.5));
    }


    @Test
    @Transactional
    void testDeleteRoom() throws Exception {
        mockMvc.perform(delete("/api/rooms/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }


}
