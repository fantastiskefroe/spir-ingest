package dk.fantastiskefroe.spir.ingest.entity;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;

public record Order(
        @Nullable
        Long id,
        @NonNull
        String name,
        @NonNull
        int number,
        @NonNull
        OrderStatus status,
        @Nullable
        CancelReason cancelReason,
        @NonNull
        FinancialStatus financialStatus,
        @NonNull
        FulfillmentStatus fulfillmentStatus,
        @Nullable
        Double totalDiscount,
        @Nullable
        Double subtotalPrice,
        @Nullable
        Double totalTax,
        @Nullable
        Double totalPrice,
        @Nullable
        Double totalShippingPrice,
        @NonNull
        List<OrderLine> orderLines,
        @NonNull
        Instant createdDateTime) {

    public Order withID(long orderID) {
        return new Order(orderID, name, number, status, cancelReason, financialStatus, fulfillmentStatus, totalDiscount, subtotalPrice, totalTax, totalPrice, totalShippingPrice, orderLines, createdDateTime);
    }
}
