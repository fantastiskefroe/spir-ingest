package dk.fantastiskefroe.spir.ingest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FulfillmentStatusDTO {
    @JsonProperty("null")
    NULL,
    @JsonProperty("fulfilled")
    FULFILLED,
    @JsonProperty("partial")
    PARTIAL,
    @JsonProperty("restocked")
    RESTOCKED
}
