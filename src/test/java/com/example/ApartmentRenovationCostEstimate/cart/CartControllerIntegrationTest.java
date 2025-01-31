package com.example.ApartmentRenovationCostEstimate.cart;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Mock
    private CartItemRepository cartItemRepository;


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
        assertEquals(2, cartCount, "No cart should be saved in the database");
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


/*    @Test
    @Transactional
    @DisplayName("It should return BAD REQUEST when user tries add product with invalid data")
    void testAddProductToCartWithInvalidData_shouldReturnBadRequest() throws Exception {
        String invalidCartJson = """
                {
                    "cartId": 11,
                    "productId": 11,
                    "quantity": -1
                }
                """;

        mockMvc.perform(post("/api/carts/{cartId}/products", 11)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCartJson))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.errors[*].field").value("quantity"))
                .andExpect(jsonPath("$.errors[*].message").value("Quantity must be greater than zero"));
    }*/


    @Test
    @Transactional
    @DisplayName("It should update quantity id product already exists in the cart")
    void testUpdateProductQuantityInCartWhenProductExistsInTheCart() throws Exception{
        String validCartJson = """
            {
                "cartId": 4,
                "productId": 2,
                "quantity": 5
            }
            """;

        mockMvc.perform(post("/api/carts/{cartId}/products", 4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCartJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product added to cart"))
                .andExpect(jsonPath("$.data.cartItems[0].product.id").value(2))
                .andExpect(jsonPath("$.data.cartItems[0].quantity").value(7));
    }


    @Test
    @Transactional
    @DisplayName("It should return NotFoundException when user tries add product to doesn't exist cart")
    void testAddProductToCartWhenCartDoesNotExist_shouldReturnNotFound() throws Exception {
        String invalidCartJson = """
                {
                    "cartId": 1,
                    "productId": 1,
                    "quantity": 2
                }
                """;

        mockMvc.perform(post("/api/carts/{cartId}/products", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCartJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cart not found!"));
    }


    @Test
    @Transactional
    @DisplayName("It should return NotFoundException when user tries add product to cart when product doesn't exist")
    void testAddProductToCartWhenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        String invalidCartJson = """
                {
                    "cartId": 11,
                    "productId": 1,
                    "quantity": 0
                }
                """;

        mockMvc.perform(post("/api/carts/{cartId}/products", 11)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCartJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found!"));
    }


    @Test
    @Transactional
    @DisplayName("It should calculate total cost of cart correctly after adding product")
    void testCalculateTotalCostAfterAddingProduct() throws Exception {
        String validCartJson = """
                {
                    "cartId": 11,
                    "productId": 15,
                    "quantity": 5
                }
                """;

        BigDecimal expectedTotalCost = new BigDecimal(7560.0).setScale(1);

        mockMvc.perform(post("/api/carts/{cartId}/products", 11)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCartJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCost").value(expectedTotalCost));
    }


    @Test
    @DisplayName("It should find cart by given id successfully - with checking pagination")
    void testGetCart_shouldReturnCartByGivenId() throws Exception {
        mockMvc.perform(get("/api/carts/{cartId}?page=0&size=10", 4)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cart retrieved successfully"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.cartItemsPageMetadata.pageSize").value(10))
                .andExpect(jsonPath("$.data.cartItemsPageMetadata.totalElements").value(58))
                .andExpect(jsonPath("$.data.cartItemsPageMetadata.totalPages").value(6))
                .andExpect(jsonPath("$.data.cartItems.length()").value(10));
    }


    @Test
    @DisplayName("It should return NOT FOUND when user find doesn't exist cart")
    void testGetCart_shouldReturnCartNotFoundExceptionWhenUserTryFindDoesNotExistCart() throws Exception {
        mockMvc.perform(get("/api/carts/{cartId}", 7)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cart not found!"));
    }


    @Test
    @DisplayName("It should return all carts - with checking pagination")
    void testGetAllCarts_shouldReturnAllCarts() throws Exception {
        mockMvc.perform(get("/api/carts?page=0&size=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Carts retrieved successfully"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.page.size").value(10))
                .andExpect(jsonPath("$.data.page.totalElements").value(2))
                .andExpect(jsonPath("$.data.page.totalPages").value(1))
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }


    @Test
    @DisplayName("It should return all carts by User ID - with checking pagination")
    void testGetAllCartsByUserID_shouldReturnAllCartsByUserID() throws Exception {
        mockMvc.perform(get("/api/carts/user/{userId}?page=0&size=10", 13)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Carts retrieved successfully"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.page.size").value(10))
                .andExpect(jsonPath("$.data.page.totalElements").value(2))
                .andExpect(jsonPath("$.data.page.totalPages").value(1))
                .andExpect(jsonPath("$.data.content.length()").value(2));

    }


    @Test
    @Transactional
    @DisplayName("It should remove product from cart by given Id")
    void testRemoveProductFormCartByGivenId() throws Exception {
        mockMvc.perform(delete("/api/carts/{cartId}/products/{productId}", 4, 20))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }


    @Test
    @Transactional
    @DisplayName("It should delete cart by given Id")
    void testDeleteCartByGivenId() throws Exception {
        mockMvc.perform(delete("/api/carts/{cartId}", 11))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }


    @Test
    @DisplayName("It should return list of products categories from cart")
    void testGetProductCategoriesFromCart_shouldReturnListOfProductCategoriesFromCart() throws Exception {
        mockMvc.perform(get("/api/carts/{cartId}/products/categories", 4)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0]").value("Baterie"))
                .andExpect(jsonPath("$[1]").value("Bloczki betonowe"))
                .andExpect(jsonPath("$[2]").value("Drzwi i ościeżnice"));
    }


    @Test
    @DisplayName("It should return filtered products by given category in cart")
    void testGetProductsByCategory_shouldReturnProductsByGivenCategory() throws Exception {
        String category = "Zmywarki";
        mockMvc.perform(get("/api/carts/{cartId}/products/category/{category}", 4, category)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].product.name").value("Zmywarka Electrolux 600 SatelliteClean EEM48321L 59,6cm Automatyczne otwieranie drzwi Szuflada na sztućce"));

    }
}
