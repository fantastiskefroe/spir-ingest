package dk.fantastiskefroe.spir.ingest.controller;

import dk.fantastiskefroe.spir.ingest.entity.FulfillmentStatus;
import dk.fantastiskefroe.spir.ingest.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;


    @Test
    @DisplayName("getOrders - No fulfillment status")
    void getOrders1() {
        // Act
        orderController.getOrders(null);

        // Assert
        verify(orderService, times(1)).getAllOrders();
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @DisplayName("getOrders - Fulfillment status PARTIAL")
    void getOrders2() {
        // Act
        orderController.getOrders(FulfillmentStatus.PARTIAL);

        // Assert
        verify(orderService, times(1)).getOrdersByFulfillmentStatus(eq(FulfillmentStatus.PARTIAL));
        verifyNoMoreInteractions(orderService);
    }
}