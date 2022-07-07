package dk.fantastiskefroe.spir.ingest.controller;

import dk.fantastiskefroe.spir.ingest.controller.dto.CreateOrderDTO;
import dk.fantastiskefroe.spir.ingest.controller.mapping.OrderMapper;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import dk.fantastiskefroe.spir.ingest.entity.OrderLine;
import dk.fantastiskefroe.spir.ingest.service.OrderService;
import dk.fantastiskefroe.spir.ingest.util.StringMapMessageWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/webhook")
public class ShopifyWebhookController {

    private static final Logger log = LogManager.getLogger(ShopifyWebhookController.class);

    private final OrderService orderService;

    public ShopifyWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order-created")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        final List<OrderLine> orderLineList = createOrderDTO.lineItems()
                .stream()
                .map(OrderMapper::toOrderLine)
                .toList();

        final Order order = OrderMapper.toOrder(createOrderDTO, orderLineList);

        log.info(new StringMapMessageWrapper()
                .withNullable("event", "order created")
                .withNullable("order name", order.name())
                .withNullable("order lines", order.orderLines().size())
                .withNullable("cancel", order.cancelReason())
                .withNullable("financial", order.financialStatus())
                .withNullable("fulfillment", order.fulfillmentStatus()));

        orderService.createOrder(order);
    }

    @PostMapping("/order-updated")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        final List<OrderLine> orderLineList = createOrderDTO.lineItems()
                .stream()
                .map(OrderMapper::toOrderLine)
                .toList();

        final Order order = OrderMapper.toOrder(createOrderDTO, orderLineList);

        log.info(new StringMapMessageWrapper()
                .withNullable("event", "order updated")
                .withNullable("order name", order.name())
                .withNullable("order lines", order.orderLines().size())
                .withNullable("cancel", order.cancelReason())
                .withNullable("financial", order.financialStatus())
                .withNullable("fulfillment", order.fulfillmentStatus()));

        orderService.updateOrder(order);
    }
}
