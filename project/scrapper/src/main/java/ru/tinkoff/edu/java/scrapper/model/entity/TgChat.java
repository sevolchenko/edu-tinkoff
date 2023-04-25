package ru.tinkoff.edu.java.scrapper.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

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

}
