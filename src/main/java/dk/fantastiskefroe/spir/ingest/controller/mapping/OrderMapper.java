package dk.fantastiskefroe.spir.ingest.controller.mapping;

import dk.fantastiskefroe.spir.ingest.controller.dto.OrderDTO;
import dk.fantastiskefroe.spir.ingest.controller.dto.OrderLineDTO;
import dk.fantastiskefroe.spir.ingest.entity.*;

import java.util.List;

public abstract class OrderMapper {

    public static Order toOrder(OrderDTO source, List<OrderLine> orderLineList) {
        return new Order(
                null,
                source.name(),
                source.number(),
                OrderStatus.OK,
                source.cancelReason(),
                source.financialStatus(),
                source.fulfillmentStatus() == null ? FulfillmentStatus.NULL : source.fulfillmentStatus(),
                source.totalDiscount(),
                source.subtotalPrice(),
                source.totalTax(),
                source.totalPrice(),
                source.totalShippingPrice(),
                orderLineList,
                source.createdAt()
        );
    }

    public static OrderLine toOrderLine(OrderLineDTO source) {
        return new OrderLine(
                source.sku(),
                source.title(),
                source.variantTitle(),
                source.quantity(),
                source.price()
        );
    }
}
