package ru.tinkoff.edu.java.scrapper.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class SubscriptionId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "tg_chat_id")
    private TgChat tgChat;

    @ManyToOne
    @JoinColumn(name = "link_id")
    private Link link;

}
