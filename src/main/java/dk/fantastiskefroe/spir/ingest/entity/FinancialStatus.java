package dk.fantastiskefroe.spir.ingest.entity;

/**
 * pending: The payments are pending. Payment might fail in this state. Check again to confirm whether the payments have been paid successfully.
 * authorized: The payments have been authorized.
 * partially_paid: The order has been partially paid.
 * paid: The payments have been paid.
 * partially_refunded: The payments have been partially refunded.
 * refunded: The payments have been refunded.
 * voided: The payments have been voided.
 */
public enum FinancialStatus {
    PENDING,
    AUTHORIZED,
    PARTIALLY_PAID,
    PAID,
    PARTIALLY_REFUNDED,
    REFUNDED,
    VOIDED
}
