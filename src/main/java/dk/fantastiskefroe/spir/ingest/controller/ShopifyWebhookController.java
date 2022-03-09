package dk.fantastiskefroe.spir.ingest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dk.fantastiskefroe.spir.ingest.controller.dto.OrderDTO;
import dk.fantastiskefroe.spir.ingest.controller.mapping.OrderMapper;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import dk.fantastiskefroe.spir.ingest.entity.OrderLine;
import dk.fantastiskefroe.spir.ingest.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@RestController
@Validated
@RequestMapping
public class ShopifyWebhookController {
    private final OrderService orderService;

    public ShopifyWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    private static String calcHMAC(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
    }

    private void validateWebhook(String body, String hmac) {
        try {
            String calcedHMAC = calcHMAC(body, "testkey");
            if (!calcedHMAC.equals(hmac)) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid HMAC"
                );
            }
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Invalid HMAC key"
            );
        }
    }

    private <T> T parseBody(String body, Class<T> DTOClass) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final T DTO;
        try {
            DTO = objectMapper.readValue(body, DTOClass);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid JSON body"
            );
        }
        return DTO;
    }

    @PostMapping(value = "/order-created"/*, consumes = MediaType.APPLICATION_JSON_VALUE*/)
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody String body, @RequestHeader("X-Shopify-Hmac-Sha256") String hmac) {
        validateWebhook(body, hmac);
        final OrderDTO orderDTO = parseBody(body, OrderDTO.class);

        final List<OrderLine> orderLineList = orderDTO.lineItems()
                .stream()
                .map(OrderMapper::toOrderLine)
                .toList();

        final Order order = OrderMapper.toOrder(orderDTO, orderLineList);

        orderService.createOrder(order);
    }
}
