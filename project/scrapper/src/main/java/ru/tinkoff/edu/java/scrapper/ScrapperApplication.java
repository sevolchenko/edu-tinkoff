package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.scrapper.configuration.AccessType;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.model.entity.Link;
import ru.tinkoff.edu.java.scrapper.model.entity.Subscription;
import ru.tinkoff.edu.java.scrapper.model.entity.SubscriptionId;
import ru.tinkoff.edu.java.scrapper.model.entity.TgChat;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;

import java.time.Instant;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(ScrapperApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        System.out.println(config);
//
//        var linkRepo = ctx.getBean(JpaLinkRepository.class);
//        var tgChatRepo = ctx.getBean(JpaTgChatRepository.class);
//        var subRepo = ctx.getBean(JpaSubscriptionRepository.class);
//
//        AccessType.JPA.name().toLowerCase();
//
//        var chat = new TgChat()
//                .setTgChatId(0L)
//                .setRegisteredAt(Instant.now())
//                .setUsername("aaa");
//        chat = tgChatRepo.save(chat);
//
//        tgChatRepo.save(new TgChat()
//                        .setTgChatId(0L)
//                        .setUsername("bbb")
//                .setRegisteredAt(Instant.now()));
//
//        var link = new Link()
//                .setLinkId(2L)
//                .setUrl("http://ssss.com")
//                .setCreatedAt(Instant.now())
//                .setLastScannedAt(Instant.now());
//        link = linkRepo.save(link);
//
//        var sub = new Subscription()
//                .setSubscriptionId(new SubscriptionId()
//                        .setTgChat(chat)
//                        .setLink(link))
//                .setCreatedAt(Instant.now());
//
//        sub = subRepo.save(sub);
//        subRepo.flush();
//
//        System.out.println(link.getSubscriptions());
//
//        System.out.println();
    }
}
