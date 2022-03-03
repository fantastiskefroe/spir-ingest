package dk.fantastiskefroe.spir.ingest.service;

import dk.fantastiskefroe.spir.ingest.dao.OrderDAO;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Transactional
    public Order createOrder(Order order) {
        final Instant now = Instant.now();
        orderDAO.invalidateExistingOrderByName(order.name(), now);

        final long orderID = orderDAO.createOrder(order, now);
        order.orderLines()
                .forEach(orderLine -> orderDAO.createOrderLine(orderID, orderLine));

        return order.withID(orderID);
    }
}
