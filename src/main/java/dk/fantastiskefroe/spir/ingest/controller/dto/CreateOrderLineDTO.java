package dk.fantastiskefroe.spir.ingest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record CreateOrderLineDTO(
        @NonNull
        String sku,

        @NonNull
        String title,

        @JsonProperty("variant_title")
        String variantTitle,

        @NonNull
        Integer quantity,

        @Nullable
        Double price
) {
}
