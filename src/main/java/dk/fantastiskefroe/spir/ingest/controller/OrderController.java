package dk.fantastiskefroe.spir.ingest.controller;

import dk.fantastiskefroe.spir.ingest.controller.dto.OrderDTO;
import dk.fantastiskefroe.spir.ingest.controller.dto.OrderDTOList;
import dk.fantastiskefroe.spir.ingest.controller.dto.OrderLineDTO;
import dk.fantastiskefroe.spir.ingest.controller.mapping.OrderMapper;
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
    public OrderDTOList getOrders(@RequestParam(required = false) FulfillmentStatus fulfillmentStatus) {
        final List<Order> orderList;
        if (fulfillmentStatus != null) {
            orderList = orderService.getOrdersByFulfillmentStatus(fulfillmentStatus);
        } else {
            orderList = orderService.getAllOrders();
        }

        List<OrderDTO> orderDTOList = orderList
                .stream()
                .map(order -> {
                    final List<OrderLineDTO> orderLineDTOList = order.orderLines()
                            .stream()
                            .map(OrderMapper::toOrderLineDTO)
                            .toList();
                    return OrderMapper.toOrderDTO(order, orderLineDTOList);
                })
                .toList();

        return new OrderDTOList(orderDTOList);
    }
}
