package dk.fantastiskefroe.spir.ingest.controller.dto;

import org.springframework.lang.NonNull;

import java.util.List;

public record OrderDTOList(
        @NonNull
        List<OrderDTO> orders
) {
}
