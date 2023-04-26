package ru.tinkoff.edu.java.scrapper.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "link")
@Getter
@Setter
@Accessors(chain = true)
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "url", unique = true)
    private String url;

    @Column(name = "last_scanned_at")
    private Instant lastScannedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "subscriptionId.link", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

}
