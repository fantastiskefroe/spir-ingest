package dk.fantastiskefroe.spir.ingest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FulfillmentStatus {

    @JsonProperty("fulfilled")
    FULFILLED,
    @JsonProperty("partial")
    PARTIAL,
    @JsonProperty("restocked")
    RESTOCKED
}
