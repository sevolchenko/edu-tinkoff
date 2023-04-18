package ru.tinkoff.edu.java.scrapper.dbtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class ScrapperDBTests extends IntegrationEnvironment {

    private Connection conn;

    @BeforeEach
    public void setup() {
        try {
            conn = DriverManager.getConnection(DB_CONTAINER.getJdbcUrl(), DB_CONTAINER.getUsername(), DB_CONTAINER.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Transactional(readOnly = true)
    void tgChatMigrationsLoads() throws SQLException {
        // given
        var stmt = conn.createStatement();
        String query = """
                select EXISTS(
                    select *
                    from information_schema.tables
                    where table_name = 'tg_chat'
                );
                """;

        // when
        var resultSet = stmt.executeQuery(query);

        // then
        assertThat(resultSet, is(notNullValue()));

        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getBoolean(1), is(true));
    }


    @Test
    @Transactional(readOnly = true)
    void linkMigrationsLoads() throws SQLException {
        // given
        var stmt = conn.createStatement();
        String query = """
                select EXISTS(
                    select *
                    from information_schema.tables
                    where table_name = 'link'
                );
                """;

        // when
        var resultSet = stmt.executeQuery(query);

        // then
        assertThat(resultSet, is(notNullValue()));

        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getBoolean(1), is(true));
    }


    @Test
    @Transactional(readOnly = true)
    void subscriptionMigrationsLoads() throws SQLException {
        // given
        var stmt = conn.createStatement();
        String query = """
                select EXISTS(
                    select *
                    from information_schema.tables
                    where table_name = 'subscription'
                );
                """;

        // when
        var resultSet = stmt.executeQuery(query);

        // then
        assertThat(resultSet, is(notNullValue()));

        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getBoolean(1), is(true));
    }

}
