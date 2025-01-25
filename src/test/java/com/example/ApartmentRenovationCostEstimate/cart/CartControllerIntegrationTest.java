package com.example.ApartmentRenovationCostEstimate.cart;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;


    @Test
    @Transactional
    @DisplayName("It should add and return new cart")
    void testAddNewCart_shouldReturnCreatedNewCart() throws Exception {
        String validCartJson = """
                {
                    "userId": 13,
                    "name": "Test Cart"
                }
                """;

        mockMvc.perform(post("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCartJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Cart created successfully."))
                .andExpect(jsonPath("$.data.name").value("Test Cart"))
                .andExpect(jsonPath("$.data.user.id").value(13));
    }


    @Test
    @Transactional
    @DisplayName("It should return BAD REQUEST when user try add cart with invalid data")
    void testAddNewCartWithInvalidData_shouldReturnBadRequest() throws Exception {
        String invalidCartJson = """
                {
                    "userId": 13,
                    "name": ""
                }
                """;

        mockMvc.perform(post("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCartJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[*].field").value("name"))
                .andExpect(jsonPath("$.errors[*].message").value("Cart name is required"));

        long cartCount = cartRepository.count();
        Assertions.assertEquals(2, cartCount, "No cart should be saved in the database");
    }


    @Test
    @Transactional
    @DisplayName("It should add product to cart and return added product")
    void testAddProductToCart_shouldReturnAddedProduct() throws Exception{
        String validCartJson = """
                {
                    "cartId": 11,
                    "productId": 15,
                    "quantity": 5
                }
                """;

        mockMvc.perform(post("/api/carts/{cartId}/products", 11)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCartJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product added to cart"))
                .andExpect(jsonPath("$.data.id").value(11))
                .andExpect(jsonPath("$.data.cartItems[*].product.id").value(15));
    }

}
