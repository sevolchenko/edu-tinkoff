package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.dto.response.StackOverflowQuestionsResponse;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.client.dto.request.StackOverflowQuestionRequest;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(ScrapperApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        System.out.println(config);

//        StackOverflowClient stackOverflowClient = ctx.getBean(StackOverflowClient.class);
//        Mono<StackOverflowQuestionsResponse> response =
//                stackOverflowClient.getQuestionInfoFromClient(new StackOverflowQuestionRequest(30917782));
//        System.out.println(response.block());
    }
}
