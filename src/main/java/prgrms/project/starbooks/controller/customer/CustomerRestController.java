package prgrms.project.starbooks.controller.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prgrms.project.starbooks.service.CustomerService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerRestController {
    
    private final CustomerService customerService;

    @PostMapping("/customers")
    public ResponseEntity<CustomerResponse> registerCustomer(@RequestBody CustomerRequest customerRequest) {
        var customerResponse = customerService.registerCustomer(customerRequest);

        return ResponseEntity.ok(customerResponse);
    }

    @GetMapping("/{customerId}/customers")
    public ResponseEntity<CustomerResponse> searchById(@PathVariable UUID customerId) {
        var customerResponse = customerService.searchById(customerId);


        return ResponseEntity.ok(customerResponse);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> searchAllCustomers() {
        var customerResponses = customerService.searchAllCustomers();

        return ResponseEntity.ok(customerResponses);
    }

    @PatchMapping("/{customerId}/customers")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID customerId, @RequestBody CustomerRequest customerRequest) {
        var customerResponse = customerService.updateCustomer(customerId, customerRequest);

        return ResponseEntity.ok(customerResponse);
    }

    @DeleteMapping("/{customerId}/customers")
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID customerId) {
        customerService.deleteById(customerId);

        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/customers")
    public ResponseEntity<Boolean> deleteAllCustomers() {
        customerService.deleteAllCustomers();

        return ResponseEntity.ok(true);
    }
}
