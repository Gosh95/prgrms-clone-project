package prgrms.project.starbooks.controller.product;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import prgrms.project.starbooks.config.TestConfig;
import prgrms.project.starbooks.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static prgrms.project.starbooks.domain.product.Category.*;

@WebMvcTest
class ProductRestControllerTest extends TestConfig {

    @Autowired
    ProductService productService;

    @Autowired
    MockMvc mvc;

    final Gson gson = new Gson();

    ProductRequest registerRequest = new ProductRequest("test-book", IT, 10000L, "this is test book");
    String registerJson = gson.toJson(registerRequest);

    ProductRequest updateRequest = new ProductRequest("updated-book", ECONOMY, 30000L, "this is updated book");
    String updateJson = gson.toJson(updateRequest);

    static List<ProductRequest> getProductRequests() {
        return List.of(
                new ProductRequest("test-book1", IT, 10000L, "this is test book1"),
                new ProductRequest("test-book2", ECONOMY, 20000L, "this is test book2"),
                new ProductRequest("test-book3", IT, 1033300L, "this is test book3"),
                new ProductRequest("test-book4", FICTION, 11234000L, "this is test book4"),
                new ProductRequest("test-book5", IT, 101230L, "this is test book5")
        );
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new ProductRestController(productService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @AfterEach
    public void cleanup() {
        productService.deleteAllProducts();
    }

    @Test
    @DisplayName("Product 를 생성한다.")
    void registerProduct() throws Exception {
        mvc.perform(post("/api/v1/products").contentType(APPLICATION_JSON).content(registerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productName").value("test-book"))
                .andExpect(jsonPath("category").value("IT"))
                .andExpect(jsonPath("price").value("10000"))
                .andExpect(jsonPath("description").value("this is test book"));
    }

    @Test
    @DisplayName("Product 를 조회한다.")
    void searchById() throws Exception {
        var response = productService.registerProduct(registerRequest);

        mvc.perform(get("/api/v1/{productId}/products", response.productId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productName").value("test-book"))
                .andExpect(jsonPath("category").value("IT"))
                .andExpect(jsonPath("price").value("10000"))
                .andExpect(jsonPath("description").value("this is test book"));
    }

    @Test
    @DisplayName("모든 Product 를 조회한다.")
    void searchAllProducts() throws Exception {
        getProductRequests().forEach(productService::registerProduct);

        mvc.perform(get("/api/v1/products").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        var productResponses = productService.searchAllProducts();

        assertThat(productResponses.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Product 를 업데이트한다.")
    void updateProduct() throws Exception {
        var response = productService.registerProduct(registerRequest);

        mvc.perform(patch("/api/v1/{productId}/products", response.productId()).contentType(APPLICATION_JSON).content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productName").value("updated-book"))
                .andExpect(jsonPath("category").value("ECONOMY"))
                .andExpect(jsonPath("price").value("30000"))
                .andExpect(jsonPath("description").value("this is updated book"));
    }

    @Test
    @DisplayName("Product 를 삭제한다.")
    void deleteById() throws Exception {
        var response = productService.registerProduct(registerRequest);

        mvc.perform(delete("/api/v1/{productId}/products", response.productId()));

        assertThrows(NoSuchElementException.class, () -> productService.searchById(response.productId()));
    }

    @Test
    @DisplayName("모든 Product 를 삭제한다.")
    void deleteAllProducts() throws Exception {
        getProductRequests().forEach(productService::registerProduct);
        var beforeDelete = productService.searchAllProducts();

        assertThat(beforeDelete.size()).isEqualTo(5);

        mvc.perform(delete("/api/v1/products"))
                .andExpect(status().isOk());

        var afterDelete = productService.searchAllProducts();

        assertThat(afterDelete.size()).isEqualTo(0);
    }
}