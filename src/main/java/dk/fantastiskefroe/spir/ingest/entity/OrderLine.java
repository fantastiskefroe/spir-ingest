package dk.fantastiskefroe.spir.ingest.entity;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record OrderLine(
        @NonNull
        String sku,
        @NonNull
        String title,
        @NonNull
        String variantTitle,
        @NonNull
        int quantity,
        @Nullable
        Double price) {
}
