package ru.tinkoff.edu.java.bot.metric;

import io.micrometer.core.instrument.Metrics;

public class ProcessedMessagesMetric {

    private ProcessedMessagesMetric() {}

    public static void incrementProcessedMessagesCounter() {
        Metrics.counter("processed_messages").increment();
    }

}
