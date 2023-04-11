package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ScrapperDBTests extends IntegrationEnvironment {

    @Value("${spring.datasource.username}")
    private String username;

    @Test
    void envLoads() {

        System.out.println(username);
    }

}
