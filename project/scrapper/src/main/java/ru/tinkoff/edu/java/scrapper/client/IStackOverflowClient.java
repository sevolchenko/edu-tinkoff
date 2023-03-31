package ru.tinkoff.edu.java.scrapper.client;

import ru.tinkoff.edu.java.scrapper.client.dto.request.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.client.dto.response.StackOverflowQuestionResponse;

public interface IStackOverflowClient {

    StackOverflowQuestionResponse fetchQuestion(StackOverflowQuestionRequest stackOverflowQuestionRequest);

}
