package dk.fantastiskefroe.spir.ingest.controller.mapping;

import dk.fantastiskefroe.spir.ingest.controller.dto.*;
import dk.fantastiskefroe.spir.ingest.entity.*;

import java.time.ZoneOffset;
import java.util.List;

public abstract class OrderMapper {

    public static Order toOrder(CreateOrderDTO source, List<OrderLine> orderLineList) {
        return new Order(
                null,
                source.name(),
                source.number(),
                OrderStatus.OK,
                toCancelReason(source.cancelReasonDTO()),
                toFinancialStatus(source.financialStatusDTO()),
                toFulfillmentStatus(source.fulfillmentStatusDTO()),
                source.totalDiscount(),
                source.subtotalPrice(),
                source.totalTax(),
                source.totalPrice(),
                source.totalShippingPrice(),
                orderLineList,
                source.createdAt().toInstant()
        );
    }

    private static CancelReason toCancelReason(CancelReasonDTO source) {
        if (source == null) {
            return null;
        }

        return switch (source) {
            case CUSTOMER -> CancelReason.CUSTOMER;
            case FRAUD -> CancelReason.FRAUD;
            case INVENTORY -> CancelReason.INVENTORY;
            case DECLINED -> CancelReason.DECLINED;
            case OTHER -> CancelReason.OTHER;
        };
    }

    private static FinancialStatus toFinancialStatus(FinancialStatusDTO source) {
        if (source == null) {
            return null;
        }

        return switch (source) {
            case PENDING -> FinancialStatus.PENDING;
            case AUTHORIZED -> FinancialStatus.AUTHORIZED;
            case PARTIALLY_PAID -> FinancialStatus.PARTIALLY_PAID;
            case PAID -> FinancialStatus.PAID;
            case PARTIALLY_REFUNDED -> FinancialStatus.PARTIALLY_REFUNDED;
            case REFUNDED -> FinancialStatus.REFUNDED;
            case VOIDED -> FinancialStatus.VOIDED;
        };
    }


    public static FulfillmentStatus toFulfillmentStatus(FulfillmentStatusDTO source) {
        if (source == null) {
            return FulfillmentStatus.NULL;
        }

        return switch (source) {
            case NULL -> FulfillmentStatus.NULL;
            case FULFILLED -> FulfillmentStatus.FULFILLED;
            case PARTIAL -> FulfillmentStatus.PARTIAL;
            case RESTOCKED -> FulfillmentStatus.RESTOCKED;
        };
    }

    public static OrderLine toOrderLine(CreateOrderLineDTO source) {
        return new OrderLine(
                source.sku(),
                source.title(),
                source.variantTitle(),
                source.quantity(),
                source.price()
        );
    }

    public static OrderDTO toOrderDTO(Order source, List<OrderLineDTO> orderLineList) {
        return new OrderDTO(
                source.id(),
                source.name(),
                source.number(),
                OrderStatus.OK,
                source.cancelReason(),
                source.financialStatus(),
                source.fulfillmentStatus(),
                source.totalDiscount(),
                source.subtotalPrice(),
                source.totalTax(),
                source.totalPrice(),
                source.totalShippingPrice(),
                orderLineList,
                source.createdDateTime().atOffset(ZoneOffset.UTC)
        );
    }

    public static OrderLineDTO toOrderLineDTO(OrderLine source) {
        return new OrderLineDTO(
                source.sku(),
                source.title(),
                source.variantTitle(),
                source.quantity(),
                source.price()
        );
    }
}
