package dk.fantastiskefroe.spir.ingest.entity;

public record Order(
        Long id,
        Long order_number,
        LineItem[] line_items
) {

}
