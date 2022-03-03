package dk.fantastiskefroe.spir.ingest.dao;

import dk.fantastiskefroe.spir.ingest.entity.OrderLine;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

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
                .addValue("name", name)
                .addValue("now", now);

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }

    public long createOrder(Order order, Instant now) {
        final String sql = "INSERT INTO \"order\" " +
                "(name, number, status, cancel_reason, financial_status, " +
                "total_discount, subtotal_price, total_tax, total_price, total_shipping_price, " +
                "created_date_time, valid_from) VALUES " +
                "(:name, :number, :status, :cancelReason, :financialStatus, " +
                ":total_discount, :subtotal_price, :total_tax, :total_price, :total_shipping_price, " +
                ":created_date_time, :valid_from)";

        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", order.name())
                .addValue("number", order.number())
                .addValue("status", order.status())
                .addValue("cancelReason", order.cancelReason())
                .addValue("financialStatus", order.financialStatus())

                .addValue("totalDiscount", order.totalDiscount())
                .addValue("subtotalPrice", order.subtotalPrice())
                .addValue("totalTax", order.totalTax())
                .addValue("totalPrice", order.totalPrice())
                .addValue("totalShippingPrice", order.totalShippingPrice())

                .addValue("createdDateTime", order.createdDateTime(), Types.TIMESTAMP_WITH_TIMEZONE)
                .addValue("validFrom", now, Types.TIMESTAMP_WITH_TIMEZONE);

        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource, generatedKeyHolder, new String[] {"id"});

        return Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();
    }

    public void createOrderLine(long orderID, OrderLine orderLine) {
        final String sql = "INSERT INTO order_line " +
                "(order_id, sku, title, variant_title, quantity, price) VALUES " +
                "(:orderID, :sku, :title, :variantTitle, :quantity, :price)";

        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("orderID", orderID)
                .addValue("sku", orderLine.sku())
                .addValue("title", orderLine.title())
                .addValue("variantTitle", orderLine.variantTitle())
                .addValue("quantity", orderLine.quantity())
                .addValue("price", orderLine.price());

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }
}
