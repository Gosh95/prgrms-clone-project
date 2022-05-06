package prgrms.project.starbooks.controller.customer;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import prgrms.project.starbooks.config.TestConfig;
import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.Email;
import prgrms.project.starbooks.domain.customer.Wallet;
import prgrms.project.starbooks.service.CustomerService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CustomerRestControllerTest extends TestConfig {

    @Autowired
    CustomerService customerService;

    @Autowired
    MockMvc mvc;

    final Gson gson = new Gson();

    CustomerRequest registerRequest = new CustomerRequest("test-customer", new Email("test@gmail.com"), new Address("seoul", "12345"), new Wallet(10000L));
    String registerJson = gson.toJson(registerRequest);

    CustomerRequest updateRequest = new CustomerRequest("updated-customer", new Email("updated@gmail.com"), new Address("tokyo", "99999"), new Wallet(20000L));
    String updateJson = gson.toJson(updateRequest);

    static List<CustomerRequest> getCustomerRequest() {
        return List.of(
                new CustomerRequest("test-customer1", new Email("test1@gmail.com"), new Address("seoul", "12345"), new Wallet(10000L)),
                new CustomerRequest("test-customer2", new Email("test2@gmail.com"), new Address("seoul", "12635"), new Wallet(20000L)),
                new CustomerRequest("test-customer3", new Email("test3@gmail.com"), new Address("jeju", "12355"), new Wallet(30000L)),
                new CustomerRequest("test-customer4", new Email("test4@gmail.com"), new Address("busan", "12235"), new Wallet(40000L)),
                new CustomerRequest("test-customer5", new Email("test5@gmail.com"), new Address("seoul", "12311"), new Wallet(50000L))
        );
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new CustomerRestController(customerService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @AfterEach
    public void cleanup() {
        customerService.deleteAllCustomers();
    }

    @Test
    @DisplayName("Customer 를 생성한다.")
    void registerCustomer() throws Exception {
        mvc.perform(post("/api/v1/customers").contentType(APPLICATION_JSON).content(registerJson))
                .andExpect(jsonPath("customerName").value("test-customer"))
                .andExpect(jsonPath("email.address").value("test@gmail.com"))
                .andExpect(jsonPath("address.city").value("seoul"))
                .andExpect(jsonPath("address.zipcode").value("12345"))
                .andExpect(jsonPath("wallet.money").value(10000));
    }

    @Test
    @DisplayName("Customer 를 조회한다.")
    void searchById() throws Exception {
        var response = customerService.registerCustomer(registerRequest);

        mvc.perform(get("/api/v1/{customerId}/customers", response.customerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("customerName").value("test-customer"))
                .andExpect(jsonPath("email.address").value("test@gmail.com"))
                .andExpect(jsonPath("address.city").value("seoul"))
                .andExpect(jsonPath("address.zipcode").value("12345"))
                .andExpect(jsonPath("wallet.money").value(10000));
    }

    @Test
    @DisplayName("모든 Customer 를 조회한다.")
    void searchAllCustomers() throws Exception {
        getCustomerRequest().forEach(customerService::registerCustomer);

        mvc.perform(get("/api/v1/customers").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Customer 를 업데이트한다.")
    void updateCustomer() throws Exception {
        var response = customerService.registerCustomer(registerRequest);

        mvc.perform(patch("/api/v1/{customerId}/customers", response.customerId()).contentType(APPLICATION_JSON).content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("customerName").value("updated-customer"))
                .andExpect(jsonPath("email.address").value("test@gmail.com")) //이메일은 바꿀 수 없다.
                .andExpect(jsonPath("address.city").value("tokyo"))
                .andExpect(jsonPath("address.zipcode").value("99999"))
                .andExpect(jsonPath("wallet.money").value(20000));
    }

    @Test
    @DisplayName("Customer 를 삭제한다.")
    void deleteById() throws Exception {
        var response = customerService.registerCustomer(registerRequest);

        mvc.perform(delete("/api/v1/{customerId}/customers", response.customerId()));

        Assertions.assertThrows(NoSuchElementException.class, () -> customerService.searchById(response.customerId()));
    }

    @Test
    @DisplayName("모든 Customer 를 삭제한다.")
    void deleteAllCustomers() throws Exception {
        getCustomerRequest().forEach(customerService::registerCustomer);
        var beforeDelete = customerService.searchAllCustomers();

        assertThat(beforeDelete.size()).isEqualTo(5);

        mvc.perform(delete("/api/v1/customers"))
                .andExpect(status().isOk());

        var afterDelete = customerService.searchAllCustomers();

        assertThat(afterDelete.size()).isEqualTo(0);
    }
}