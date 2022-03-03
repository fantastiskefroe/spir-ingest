package dk.fantastiskefroe.spir.ingest.entity;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;

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
        @Nullable
        Integer totalDiscount,
        @Nullable
        Integer subtotalPrice,
        @Nullable
        Integer totalTax,
        @Nullable
        Integer totalPrice,
        @Nullable
        Integer totalShippingPrice,
        @NonNull
        Instant createdDateTime) {
}
