package ru.tinkoff.edu.java.scrapper.reposotory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@ActiveProfiles("test")
public class ScrapperDBTests extends IntegrationEnvironment {

    private static Connection conn;

    @BeforeAll
    public static void setup() {
        try {
            conn = DriverManager.getConnection(DB_CONTAINER.getJdbcUrl(), DB_CONTAINER.getUsername(), DB_CONTAINER.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void chatMigrationsLoads() throws SQLException {
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
    void linksMigrationsLoads() throws SQLException {
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
    void chatLinksMigrationsLoads() throws SQLException {
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
