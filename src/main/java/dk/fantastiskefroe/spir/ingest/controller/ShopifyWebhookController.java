package dk.fantastiskefroe.spir.ingest.controller;

import dk.fantastiskefroe.spir.ingest.entity.Order;
import dk.fantastiskefroe.spir.ingest.service.OrderService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class ShopifyWebhookController {
    private final OrderService orderService;

    public ShopifyWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order-created")
    Order createEvent(@RequestBody Order order) {
        return orderService.createOrder(order);
    }


}
