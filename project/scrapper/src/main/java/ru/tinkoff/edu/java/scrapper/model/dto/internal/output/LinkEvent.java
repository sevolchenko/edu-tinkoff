package ru.tinkoff.edu.java.scrapper.model.dto.internal.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LinkEvent {

    UPDATED(1, "Link content updated"),

    BRANCHES_COUNT_INCREASED(2, "New branch on GitHub"),
    BRANCHES_COUNT_DECREASED(3, "Branch deleted on GitHub"),

    ANSWERS_COUNT_INCREASED(4, "New answer on StackOverflow");

    private final Integer code;
    private final String description;

}
