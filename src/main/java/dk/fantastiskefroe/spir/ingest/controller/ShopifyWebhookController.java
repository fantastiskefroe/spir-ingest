package dk.fantastiskefroe.spir.ingest.controller;

import dk.fantastiskefroe.spir.ingest.controller.dto.OrderDTO;
import dk.fantastiskefroe.spir.ingest.controller.mapping.OrderMapper;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import dk.fantastiskefroe.spir.ingest.entity.OrderLine;
import dk.fantastiskefroe.spir.ingest.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/webhook")
public class ShopifyWebhookController {
    private final OrderService orderService;

    public ShopifyWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order-created")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody OrderDTO orderDTO) {
        final List<OrderLine> orderLineList = orderDTO.lineItems()
                .stream()
                .map(OrderMapper::toOrderLine)
                .toList();

        final Order order = OrderMapper.toOrder(orderDTO, orderLineList);

        orderService.createOrder(order);
    }
}
