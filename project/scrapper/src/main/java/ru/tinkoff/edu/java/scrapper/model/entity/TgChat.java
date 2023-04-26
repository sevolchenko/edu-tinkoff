package ru.tinkoff.edu.java.scrapper.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tg_chat")
@Getter
@Setter
@Accessors(chain = true)
public class TgChat {

    @Id
    @Column(name = "tg_chat_id")
    private Long tgChatId;

    @Column(name = "username")
    private String username;

    @Column(name = "registered_at")
    private Instant registeredAt;

    @OneToMany(mappedBy = "subscriptionId.tgChat", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

}
