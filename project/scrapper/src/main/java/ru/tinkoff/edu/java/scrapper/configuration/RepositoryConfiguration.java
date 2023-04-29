package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public DSLContext dslContext(JdbcTemplate jdbcTemplate) {
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            Connection connection = dataSource.getConnection();
            return DSL.using(connection, SQLDialect.POSTGRES, new Settings().withRenderNameCase(RenderNameCase.LOWER));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
