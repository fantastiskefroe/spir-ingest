package dk.fantastiskefroe.spir.ingest.controller;

import dk.fantastiskefroe.spir.ingest.entity.FulfillmentStatus;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import dk.fantastiskefroe.spir.ingest.service.OrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/orders")
    public List<Order> getOrders(@RequestParam(required = false) FulfillmentStatus fulfillmentStatus) {
        if (fulfillmentStatus != null) {
            return orderService.getOrdersByFulfillmentStatus(fulfillmentStatus);
        } else {
            return orderService.getOrders();
        }

    }
}
