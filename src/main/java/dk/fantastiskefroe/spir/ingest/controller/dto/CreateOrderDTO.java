package dk.fantastiskefroe.spir.ingest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

public record CreateOrderDTO(
        @NonNull
        String name,

        @NonNull
        Integer number,

        @Nullable
        @JsonProperty("cancel_reason")
        CancelReasonDTO cancelReasonDTO,

        @NonNull
        @JsonProperty("financial_status")
        FinancialStatusDTO financialStatusDTO,

        @NonNull
        @JsonProperty("fulfillment_status")
        FulfillmentStatusDTO fulfillmentStatusDTO,

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
        List<CreateOrderLineDTO> lineItems,

        @NonNull
        @JsonProperty("created_at")
        OffsetDateTime createdAt
) {
}
