package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.BeforeAll;
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
        String query = "SELECT COUNT(*) AS count FROM chat";

        // when
        var resultSet = stmt.executeQuery(query);

        // then
        assertThat(resultSet, is(notNullValue()));

        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt(1), is(greaterThan(0)));
    }


    @Test
    void linksMigrationsLoads() throws SQLException {
        // given
        var stmt = conn.createStatement();
        String query = "SELECT COUNT(*) AS count FROM link";

        // when
        var resultSet = stmt.executeQuery(query);

        // then
        assertThat(resultSet, is(notNullValue()));

        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt(1), is(greaterThan(0)));
    }


    @Test
    void chatLinksMigrationsLoads() throws SQLException {
        // given
        var stmt = conn.createStatement();
        String query = "SELECT COUNT(*) AS count FROM chat_links";

        // when
        var resultSet = stmt.executeQuery(query);

        // then
        assertThat(resultSet, is(notNullValue()));

        assertThat(resultSet.next(), is(true));
        assertThat(resultSet.getInt(1), is(greaterThan(0)));
    }

}
