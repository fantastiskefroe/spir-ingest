package dk.fantastiskefroe.spir.ingest.entity;

/**
 * customer: The customer canceled the order.
 * fraud: The order was fraudulent.
 * inventory: Items in the order were not in inventory.
 * declined: The payment was declined.
 * other: A reason not in this list.
 */
public enum CancelReason {
    CUSTOMER,
    FRAUD,
    INVENTORY,
    DECLINED,
    OTHER
}
