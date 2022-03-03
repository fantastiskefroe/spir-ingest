package dk.fantastiskefroe.spir.ingest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record OrderLineDTO(
        @NonNull
        String sku,

        @NonNull
        String title,

        @NonNull
        @JsonProperty("variant_title")
        String variantTitle,

        @NonNull
        Integer quantity,

        @Nullable
        Double price
) {
}
