package prgrms.project.starbooks.controller.order;

import prgrms.project.starbooks.domain.order.OrderStatus;

public record OrderUpdateRequest(
        OrderStatus orderStatus
) {

}
