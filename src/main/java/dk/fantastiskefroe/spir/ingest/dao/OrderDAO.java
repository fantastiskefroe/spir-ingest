package dk.fantastiskefroe.spir.ingest.dao;

import dk.fantastiskefroe.spir.ingest.entity.LineItem;
import dk.fantastiskefroe.spir.ingest.entity.Order;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Objects;

@Repository
public class OrderDAO {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private long createLineItem(Long orderID, LineItem lineItem) {
        final String sql = "INSERT INTO line_items(id, order_id, product_id, variant_id) VALUES (:id, :order_id, :product_id, :variant_id);";
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", lineItem.id())
                .addValue("order_id", orderID)
                .addValue("product_id", lineItem.product_id())
                .addValue("variant_id", lineItem.variant_id());

        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource, generatedKeyHolder, new String[] {"id"});
        return Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();
    }

    public long createOrder(Order order) {
        final String sql = "INSERT INTO orders(id, order_number) VALUES (:id, :order_number);";
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", order.id())
                .addValue("order_number", order.order_number());

        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource, generatedKeyHolder, new String[] {"id"});

        Arrays.stream(order.line_items()).map(line_item -> createLineItem(order.id(), line_item));

        return Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();
    }
}
