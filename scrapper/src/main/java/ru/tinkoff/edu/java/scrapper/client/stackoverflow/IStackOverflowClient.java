package ru.tinkoff.edu.java.scrapper.client.stackoverflow;

import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionResponse;

public interface IStackOverflowClient {

    StackOverflowQuestionResponse fetchQuestion(StackOverflowQuestionRequest stackOverflowQuestionRequest);

}
