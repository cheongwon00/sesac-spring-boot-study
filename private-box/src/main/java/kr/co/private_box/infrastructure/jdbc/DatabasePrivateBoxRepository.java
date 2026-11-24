package kr.co.private_box.infrastructure.jdbc;

import kr.co.private_box.domain.PrivateBox;
import kr.co.private_box.domain.PrivateBoxRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Primary
public class DatabasePrivateBoxRepository implements PrivateBoxRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DatabasePrivateBoxRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public PrivateBox add(PrivateBox privateBox) {
        String sql = """
                INSERT INTO private_boxes
                    (original_url, private_url, url_password, private_box_status)
                VALUES
                    (:originalUrl, :privateUrl, :urlPassword, :privateBoxStatus)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("originalUrl", privateBox.getOriginalUrl())
                .addValue("privateUrl", privateBox.getPrivateUrl())
                .addValue("urlPassword", privateBox.getUrlPassword())
                .addValue("privateBoxStatus", privateBox.getPrivateBoxStatus().name());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);
        privateBox.setId(keyHolder.getKey().longValue());
        return privateBox;
    }

    @Override
    public PrivateBox findByPrivateUrl(String privateUrl) {
        String sql = """
            SELECT *
            FROM private_boxes
            WHERE private_url = :privateUrl
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("privateUrl", privateUrl);
        List<PrivateBox> result = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(PrivateBox.class));
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public PrivateBox update(PrivateBox privateBox) {
        String sql = """
            UPDATE private_boxes
            SET private_box_status = :privateBoxStatus
            WHERE private_url = :privateUrl
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("privateBoxStatus", privateBox.getPrivateBoxStatus().name())
                .addValue("privateUrl", privateBox.getPrivateUrl());
        namedParameterJdbcTemplate.update(sql, params);
        return privateBox;
    }

    @Override
    public List<PrivateBox> findAll() {
        String sql = """
            SELECT *
            FROM private_boxes
            """;
        return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PrivateBox.class));
    }
}
