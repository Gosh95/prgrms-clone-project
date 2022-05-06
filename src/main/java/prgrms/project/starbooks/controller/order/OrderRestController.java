package prgrms.project.starbooks.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prgrms.project.starbooks.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        var orderResponse = orderService.createOrder(orderCreateRequest);

        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/{orderId}/orders")
    public ResponseEntity<OrderResponse> searchById(@PathVariable UUID orderId) {
        var orderResponse = orderService.searchById(orderId);

        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/orders/{customerId}/order")
    public ResponseEntity<List<OrderDetailResponse>> searchOrderDetails(@PathVariable UUID customerId) {
        var orderDetailResponses = orderService.searchOrderDetails(customerId);

        return ResponseEntity.ok(orderDetailResponses);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> searchAllOrders() {
        var orderResponses = orderService.searchAllOrders();

        return ResponseEntity.ok(orderResponses);
    }

    @PatchMapping("/{orderId}/orders")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable UUID orderId, @RequestBody OrderUpdateRequest orderUpdateRequest) {
        var orderResponse = orderService.updateOrder(orderId, orderUpdateRequest);

        return ResponseEntity.ok(orderResponse);
    }

    @DeleteMapping("/{orderId}/orders")
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID orderId) {
        orderService.deleteById(orderId);

        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/orders")
    public ResponseEntity<Boolean> deleteAllOrders() {
        orderService.deleteAllOrders();

        return ResponseEntity.ok(true);
    }

}
