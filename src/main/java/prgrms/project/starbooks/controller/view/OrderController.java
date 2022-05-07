package prgrms.project.starbooks.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import prgrms.project.starbooks.service.OrderService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{customerId}/order-detail")
    public String orderDetail(@PathVariable UUID customerId, Model model) {
        var orderDetail = orderService.searchOrderDetails(customerId);
        model.addAttribute("orderDetail", orderDetail);

        return "/order-detail";
    }

}
