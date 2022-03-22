package dk.fantastiskefroe.spir.ingest.service;

import dk.fantastiskefroe.spir.ingest.dao.OrderDAO;
import dk.fantastiskefroe.spir.ingest.entity.FulfillmentStatus;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Transactional
    public Order createOrder(Order order) {
        return createOrUpdateOrder(order);
    }

    @Transactional
    public Order updateOrder(Order order) {
        return createOrUpdateOrder(order);
    }

    private Order createOrUpdateOrder(Order order) {
        final Instant now = Instant.now();
        orderDAO.invalidateExistingOrderByName(order.name(), now);

        final long orderID = orderDAO.createOrder(order, now);
        order.orderLines()
                .forEach(orderLine -> orderDAO.createOrderLine(orderID, orderLine));

        return order.withID(orderID);
    }

    public List<Order> getOrdersByFulfillmentStatus(FulfillmentStatus fulfillmentStatus) {
        return orderDAO.getByFulfillmentStatus(fulfillmentStatus);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAll();
    }
}
