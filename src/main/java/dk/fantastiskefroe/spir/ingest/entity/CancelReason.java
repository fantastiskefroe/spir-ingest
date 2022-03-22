package dk.fantastiskefroe.spir.ingest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * customer: The customer canceled the order.
 * fraud: The order was fraudulent.
 * inventory: Items in the order were not in inventory.
 * declined: The payment was declined.
 * other: A reason not in this list.
 */
public enum CancelReason {
    @JsonProperty("customer")
    CUSTOMER,
    @JsonProperty("fraud")
    FRAUD,
    @JsonProperty("inventory")
    INVENTORY,
    @JsonProperty("declined")
    DECLINED,
    @JsonProperty("other")
    OTHER
}
