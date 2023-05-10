package ru.tinkoff.edu.java.scrapper.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@Accessors(chain = true)
public class Subscription {

    @EmbeddedId
    private SubscriptionId subscriptionId;

    @Column(name = "created_at")
    private Instant createdAt;

}
