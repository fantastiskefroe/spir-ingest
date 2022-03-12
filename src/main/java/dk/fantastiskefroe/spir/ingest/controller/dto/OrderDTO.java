package dk.fantastiskefroe.spir.ingest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.fantastiskefroe.spir.ingest.entity.CancelReason;
import dk.fantastiskefroe.spir.ingest.entity.FinancialStatus;
import dk.fantastiskefroe.spir.ingest.entity.FulfillmentStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;

public record OrderDTO(
        @NonNull
        String name,

        @NonNull
        Integer number,

        @Nullable
        @JsonProperty("cancel_reason")
        CancelReason cancelReason,

        @NonNull
        @JsonProperty("financial_status")
        FinancialStatus financialStatus,

        @Nullable
        @JsonProperty("fulfillment_status")
        FulfillmentStatus fulfillmentStatus,

        @Nullable
        @JsonProperty("total_discount")
        Double totalDiscount,

        @Nullable
        @JsonProperty("subtotal_price")
        Double subtotalPrice,

        @Nullable
        @JsonProperty("total_tax")
        Double totalTax,

        @Nullable
        @JsonProperty("total_price")
        Double totalPrice,

        @Nullable
        @JsonProperty("total_shipping_price")
        Double totalShippingPrice,

        @NonNull
        @JsonProperty("line_items")
        List<OrderLineDTO> lineItems,

        @NonNull
        @JsonProperty("created_at")
        Instant createdAt
) {
}
