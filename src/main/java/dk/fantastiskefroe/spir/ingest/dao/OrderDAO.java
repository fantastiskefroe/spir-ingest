package dk.fantastiskefroe.spir.ingest.dao;

import dk.fantastiskefroe.spir.ingest.entity.OrderLine;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.Arrays;
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
                "(name, number, status, cancel_reason, financial_status, " +
                "total_discount, subtotal_price, total_tax, total_price, total_shipping_price, " +
                "created_date_time, valid_from) VALUES " +
                "(:name, :number, :status, :cancelReason, :financialStatus, " +
                ":totalDiscount, :subtotalPrice, :totalTax, :totalPrice, :totalShippingPrice, " +
                ":createdDateTime, :validFrom)";

        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", order.name(), Types.VARCHAR)
                .addValue("number", order.number(), Types.INTEGER)
                .addValue("status", order.status(), Types.VARCHAR)
                .addValue("cancelReason", order.cancelReason(), Types.VARCHAR)
                .addValue("financialStatus", order.financialStatus(), Types.VARCHAR)

                .addValue("totalDiscount", order.totalDiscount(), Types.INTEGER)
                .addValue("subtotalPrice", order.subtotalPrice(), Types.INTEGER)
                .addValue("totalTax", order.totalTax(), Types.INTEGER)
                .addValue("totalPrice", order.totalPrice(), Types.INTEGER)
                .addValue("totalShippingPrice", order.totalShippingPrice(), Types.INTEGER)

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
                .addValue("price", orderLine.price(), Types.INTEGER);

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }
}
