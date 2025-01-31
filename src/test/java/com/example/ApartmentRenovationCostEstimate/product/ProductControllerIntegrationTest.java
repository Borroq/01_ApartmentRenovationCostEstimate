package com.example.ApartmentRenovationCostEstimate.product;


import org.junit.jupiter.api.DisplayName;
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
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;


    @Test
    @Transactional
    @DisplayName("It should add and return new product")
    void testAddProduct_shouldReturnCreatedNewProduct() throws Exception {
        String validProductJson = """               
               {
                    "name": "Piekarnik elektryczny parowy XZP500",
                    "brand": "Electrolux",
                    "link": "https://www.euro.com.pl/piekarniki-do-zabudowy/electrolux-eoc6h76z-steamcrisp-700.bhtml",
                    "category": "Piekarniki",
                    "price": 2499.00
               }
               """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProductJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Piekarnik elektryczny parowy XZP500"))
                .andExpect(jsonPath("$.data.brand").value("Electrolux"))
                .andExpect(jsonPath("$.data.link").value("https://www.euro.com.pl/piekarniki-do-zabudowy/electrolux-eoc6h76z-steamcrisp-700.bhtml"))
                .andExpect(jsonPath("$.data.category").value("Piekarniki"))
                .andExpect(jsonPath("$.data.price").value(2499.00));
    }


    @Test
    @Transactional
    @DisplayName("It should return BAD REQUEST when user try add product with invalid data")
    void testAddProduct_shouldReturnBadRequestForInvalidData() throws Exception{
        String invalidProductJson = """               
               {
                    "name": "Piekarnik elektryczny parowy XZP500",
                    "brand": "Electrolux",
                    "link": "xyz",
                    "category": "Piekarniki",
                    "price": 2499.00
               }
               """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[*].message").value("Invalid URL"));

        long productCount = productRepository.count();
        assertEquals(132, productCount, "No product should be saved in the database");
    }


    @Test
    @Transactional
    @DisplayName("It should return BAD REQUEST when product data is incomplete")
    void testAddProduct_shouldReturnBadRequestFromIncompleteData() throws Exception {
        String invalidProductJson = """               
               { 
                    "brand": "Electrolux",
                    "link": "https://www.euro.com.pl/piekarniki-do-zabudowy/electrolux-eoc6h76z-steamcrisp-700.bhtml",
                    "category": "Piekarniki",
                    "price": 2499.00
               }
               """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[*].message").value("Name cannot be null"));
    }


    @Test
    @Transactional
    @DisplayName("It should return BAD REQUEST when price is negative")
    void testAddProduct_shouldReturnBadRequestForNegativePrice() throws Exception {
        String invalidProductJson = """               
           {
                "name": "Piekarnik elektryczny",
                "brand": "Electrolux",
                "link": "https://some.link",
                "category": "Piekarniki",
                "price": -2499.00
           }
           """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.type").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors[*].message").value("Price must be greater than or equal to 0"));
    }


    @Test
    @DisplayName("It should find product by given id successfully")
    void testGetProduct_shouldReturnProductByGivenId() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 2)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product retrieved successfully"))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    @DisplayName("It should return NOT FOUND when user try find doesn't exist product")
    void testGetProduct_shouldReturnProductNotFoundWhenUserTryFindDoesNotExistProduct() throws Exception{
        mockMvc.perform(get("/api/products/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found!"));
    }


    @Test
    @DisplayName("It should return all products")
    void testGetAllProducts_shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/api/products")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.message").value("Products retrieved successfully"))
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    @DisplayName("It should return paginated products")
    void testGetAllProducts_shouldReturnPaginatedProducts() throws Exception {
        mockMvc.perform(get("/api/products?page=0&size=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("@.message").value("Products retrieved successfully"))
                .andExpect(jsonPath("$.data.page.size").value(10))
                .andExpect(jsonPath("$.data.page.totalElements").value(132))
                .andExpect(jsonPath("$.data.page.totalPages").value(14))
                .andExpect(jsonPath("$.data.content.length()").value(10));
    }


    @Test
    @Transactional
    @DisplayName("It should update Product and return updated Product")
    void testUpdateProduct_shouldReturnUpdatedProduct() throws Exception {
        String validProductJson = """               
               {
                    "name": "nowy Produkt",
                    "brand": "Nowa Marka",
                    "link": "www.NowyLinkDoProduktu@produkt.com",
                    "category": "nowaKategoria",
                    "price": 3333.00
               }
               """;

        mockMvc.perform(put("/api/products/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProductJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("nowy Produkt"))
                .andExpect(jsonPath("$.data.brand").value("Nowa Marka"))
                .andExpect(jsonPath("$.data.link").value("www.NowyLinkDoProduktu@produkt.com"))
                .andExpect(jsonPath("$.data.category").value("nowaKategoria"))
                .andExpect(jsonPath("$.data.price").value(3333.00));
    }


    @Test
    @Transactional
    @DisplayName("It should delete product by given Id")
    void testDeleteProductByGivenId() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }


    @Test
    @DisplayName("It should return filtered products by give category")
    void testGetProductByCategory_shouldReturnFilteredProductByGivenCategory() throws Exception {
        String category = "Zmywarki";
        mockMvc.perform(get("/api/products/category/{category}", category)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Zmywarka Whirlpool W7I HT58 T Maxi Space 60cm Automatyczne otwieranie drzwi Szuflada na sztućce"))
                .andExpect(jsonPath("$[4].name").value("Zmywarka Whirlpool WSIO 3O34 PFE X 44,8cm Automatyczne otwieranie drzwi Szuflada na sztućce "));
    }


    @Test
    @DisplayName("It should return NOT FOUND when product category does not exist")
    void testGetProductByCategory_shouldReturnNotFoundForInvalidCategory() throws Exception {
        String category = "NonExistentCategory";
        mockMvc.perform(get("/api/products/category/{category}", category)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product category not found!"));
    }


    @Test
    @DisplayName("It should return list of product categories")
    void testGetProductCategories_shouldReturnListOfProductCategories() throws Exception {
        mockMvc.perform(get("/api/products/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Categories retrieved successfully"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0]").value("Baterie"))
                .andExpect(jsonPath("$.data[1]").value("Bloczki betonowe"))
                .andExpect(jsonPath("$.data[2]").value("Drzwi i ościeżnice"));
    }


    @Test
    @DisplayName("It should return list of product brands")
    void testGetProductBrands_shouldReturnListOfProductBrands() throws Exception {
        mockMvc.perform(get("/api/products/brands")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Brands retrieved successfully"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[2]").value("Alsapan"))
                .andExpect(jsonPath("$.data[3]").value("Alveus"))
                .andExpect(jsonPath("$.data[4]").value("Artens"));
    }
}
