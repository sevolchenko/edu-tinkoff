package ru.tinkoff.edu.java.scrapper.dbtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.RegisterTgChatRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.SubscriptionRequest;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.edu.java.scrapper.dbtest.data.TestListLinkResponseData.randomId;

@SpringBootTest
@ActiveProfiles("test")
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Transactional
    @Rollback
    @Test
    void testAddValidUser() {
        // given
        var tgChatId = randomId();
        var url = "https://localhost:8081";
        var request = new SubscriptionRequest(tgChatId, url);


        // when
        jdbcLinkRepository.add(request);
        var response = jdbcLinkRepository.add(request);


        // then
        assertThat(response, is(notNullValue()));

        assertThat(response, is(equalTo(tgChatId)));
    }
}
