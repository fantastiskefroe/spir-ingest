package dk.fantastiskefroe.spir.ingest.entity;

public record LineItem(
        Long id,
        Long product_id,
        Long variant_id,
        String sku,
        String name
) {
}
