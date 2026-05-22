package com.eventhub.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgresSchemaFixRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        fixByteaIfNeeded("categories", "name", "varchar(100)");
        fixByteaIfNeeded("categories", "description", "varchar(255)");
        fixByteaIfNeeded("events", "title", "varchar(180)");
        fixByteaIfNeeded("events", "description", "varchar(5000)");
        fixByteaIfNeeded("events", "location", "varchar(120)");
    }

    private void fixByteaIfNeeded(String table, String column, String targetType) {
        String sqlType = jdbcTemplate.query(
                """
                select data_type
                from information_schema.columns
                where table_name = ? and column_name = ?
                """,
                rs -> rs.next() ? rs.getString("data_type") : null,
                table,
                column
        );

        if (!"bytea".equalsIgnoreCase(sqlType)) {
            return;
        }

        String alterSql = "alter table " + table +
                " alter column " + column +
                " type " + targetType +
                " using convert_from(" + column + ", 'UTF8')";

        jdbcTemplate.execute(alterSql);
    }
}
