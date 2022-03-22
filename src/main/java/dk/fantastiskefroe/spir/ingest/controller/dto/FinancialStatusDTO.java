package dk.fantastiskefroe.spir.ingest.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * pending: The payments are pending. Payment might fail in this state. Check again to confirm whether the payments have been paid successfully.
 * authorized: The payments have been authorized.
 * partially_paid: The order has been partially paid.
 * paid: The payments have been paid.
 * partially_refunded: The payments have been partially refunded.
 * refunded: The payments have been refunded.
 * voided: The payments have been voided.
 */
public enum FinancialStatusDTO {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("authorized")
    AUTHORIZED,
    @JsonProperty("partially_paid")
    PARTIALLY_PAID,
    @JsonProperty("paid")
    PAID,
    @JsonProperty("partially_refunded")
    PARTIALLY_REFUNDED,
    @JsonProperty("refunded")
    REFUNDED,
    @JsonProperty("voided")
    VOIDED
}
