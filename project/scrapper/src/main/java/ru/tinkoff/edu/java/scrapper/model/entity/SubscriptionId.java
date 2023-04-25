package ru.tinkoff.edu.java.scrapper.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class SubscriptionId implements Serializable {

    @Column(name = "tg_chat_id")
    private Long tgChatId;

    @Column(name = "link_id")
    private Long linkId;

}
