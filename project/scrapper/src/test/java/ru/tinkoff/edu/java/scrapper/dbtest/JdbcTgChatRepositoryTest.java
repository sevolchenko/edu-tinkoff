package ru.tinkoff.edu.java.scrapper.dbtest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.RegisterTgChatRequest;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;

import static ru.tinkoff.edu.java.scrapper.dbtest.data.TestListLinkResponseData.randomId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class JdbcTgChatRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcTgChatRepository jdbcTgChatRepository;

    @Transactional
    @Rollback
    @Test
    void testAddValidUser() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var request = new RegisterTgChatRequest(tgChatId, username);


        // when
        var response = jdbcTgChatRepository.add(request);


        // then
        assertThat(response, is(notNullValue()));

        assertThat(response, is(equalTo(tgChatId)));
    }
}
