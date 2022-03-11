package dk.fantastiskefroe.spir.ingest.dao;

import dk.fantastiskefroe.spir.ingest.dao.mapping.OrderResultSetExtractor;
import dk.fantastiskefroe.spir.ingest.entity.FulfillmentStatus;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import dk.fantastiskefroe.spir.ingest.entity.OrderLine;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void invalidateExistingOrderByName(String name, Instant now) {
        final String sql = "UPDATE \"order\" " +
                "SET valid_to = :now " +
                "WHERE name = :name " +
                "AND valid_from <= :now " +
                "AND valid_to IS NULL";

        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", name, Types.VARCHAR)
                .addValue("now", Timestamp.from(now), Types.TIMESTAMP);

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }

    public long createOrder(Order order, Instant now) {
        final String sql = "INSERT INTO \"order\" " +
                "(name, number, status, cancel_reason, financial_status, fulfillment_status, " +
                "total_discount, subtotal_price, total_tax, total_price, total_shipping_price, " +
                "created_date_time, valid_from) VALUES " +
                "(:name, :number, :status, :cancelReason, :financialStatus, :fulfillmentStatus, " +
                ":totalDiscount, :subtotalPrice, :totalTax, :totalPrice, :totalShippingPrice, " +
                ":createdDateTime, :validFrom)";

        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", order.name(), Types.VARCHAR)
                .addValue("number", order.number(), Types.INTEGER)
                .addValue("status", order.status(), Types.VARCHAR)
                .addValue("cancelReason", order.cancelReason(), Types.VARCHAR)
                .addValue("financialStatus", order.financialStatus(), Types.VARCHAR)
                .addValue("fulfillmentStatus", order.fulfillmentStatus(), Types.VARCHAR)

                .addValue("totalDiscount", order.totalDiscount(), Types.DOUBLE)
                .addValue("subtotalPrice", order.subtotalPrice(), Types.DOUBLE)
                .addValue("totalTax", order.totalTax(), Types.DOUBLE)
                .addValue("totalPrice", order.totalPrice(), Types.DOUBLE)
                .addValue("totalShippingPrice", order.totalShippingPrice(), Types.DOUBLE)

                .addValue("createdDateTime", Timestamp.from(order.createdDateTime()), Types.TIMESTAMP)
                .addValue("validFrom", Timestamp.from(now), Types.TIMESTAMP);

        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource, generatedKeyHolder, new String[] {"id"});

        return Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();
    }

    public void createOrderLine(long orderID, OrderLine orderLine) {
        final String sql = "INSERT INTO order_line " +
                "(order_id, sku, title, variant_title, quantity, price) VALUES " +
                "(:orderID, :sku, :title, :variantTitle, :quantity, :price)";

        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("orderID", orderID, Types.BIGINT)
                .addValue("sku", orderLine.sku(), Types.VARCHAR)
                .addValue("title", orderLine.title(), Types.VARCHAR)
                .addValue("variantTitle", orderLine.variantTitle(), Types.VARCHAR)
                .addValue("quantity", orderLine.quantity(), Types.INTEGER)
                .addValue("price", orderLine.price(), Types.DOUBLE);

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }

    public List<Order> getByFulfillmentStatus(FulfillmentStatus fulfillmentStatus) {
        final String sql = "SELECT * FROM \"order\" as o " +
                "JOIN order_line as ol ON o.id = ol.order_id " +
                "WHERE o.fulfillment_status = :fulfillmentStatus";

        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("fulfillmentStatus", fulfillmentStatus, Types.VARCHAR);

        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, new OrderResultSetExtractor());
    }
}
