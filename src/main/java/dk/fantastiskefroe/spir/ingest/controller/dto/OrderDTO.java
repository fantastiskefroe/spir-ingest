package dk.fantastiskefroe.spir.ingest.controller.dto;

import dk.fantastiskefroe.spir.ingest.entity.CancelReason;
import dk.fantastiskefroe.spir.ingest.entity.FinancialStatus;
import dk.fantastiskefroe.spir.ingest.entity.FulfillmentStatus;
import dk.fantastiskefroe.spir.ingest.entity.OrderStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

public record OrderDTO(
        @Nullable
        Long id,

        @NonNull
        String name,

        @NonNull
        Integer number,

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
        List<OrderLineDTO> orderLines,

        @NonNull
        OffsetDateTime createdDateTime
) {
}
