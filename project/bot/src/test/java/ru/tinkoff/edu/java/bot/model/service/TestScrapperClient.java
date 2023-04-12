package ru.tinkoff.edu.java.bot.model.service;

import ru.tinkoff.edu.java.bot.client.impl.ScrapperClient;

public class TestScrapperClient extends ScrapperClient {

    public TestScrapperClient() {
        super("http://localhost:8082");
    }
}
