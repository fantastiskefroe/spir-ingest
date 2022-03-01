package dk.fantastiskefroe.spir.ingest.service;

import dk.fantastiskefroe.spir.ingest.dao.OrderDAO;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public Order createOrder(Order order) {
        orderDAO.createOrder(order);
        return order;
    }
}
