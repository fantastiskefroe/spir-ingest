package dk.fantastiskefroe.spir.ingest.dao.mapping;

import dk.fantastiskefroe.spir.ingest.entity.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {
    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
        final Map<Long, Order> orderMap = new HashMap<>();
        while(rs.next()) {
            final long orderId = rs.getLong("id");
            orderMap.computeIfAbsent(orderId, extractOrder(rs));
            orderMap.computeIfPresent(orderId, extractOrderLine(rs));
        }
        return orderMap.values().stream().toList();
    }

    private Function<Long, Order> extractOrder(ResultSet rs) {
        return (orderId) -> {
            try {
                return new Order(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("number"),
                        OrderStatus.OK,
                        Optional.ofNullable(rs.getString("cancel_reason")).map(CancelReason::valueOf).orElse(null),
                        FinancialStatus.valueOf(rs.getString("financial_status")),
                        FulfillmentStatus.valueOf(rs.getString("fulfillment_status")),
                        rs.getDouble("total_discount"),
                        rs.getDouble("subtotal_price"),
                        rs.getDouble("total_tax"),
                        rs.getDouble("total_price"),
                        rs.getDouble("total_shipping_price"),
                        new ArrayList<>(),
                        rs.getTimestamp("created_date_time").toInstant()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private BiFunction<Long, Order, Order> extractOrderLine(ResultSet rs) {
        return (orderId, order) -> {
            try {
                order.orderLines().add(
                        new OrderLine(
                                rs.getString("sku"),
                                rs.getString("title"),
                                rs.getString("variant_title"),
                                rs.getInt("quantity"),
                                rs.getDouble("price")
                        )
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return order;
        };
    }

}
