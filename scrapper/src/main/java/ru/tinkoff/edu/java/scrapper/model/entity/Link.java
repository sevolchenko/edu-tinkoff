package ru.tinkoff.edu.java.scrapper.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnTransformer;
import ru.tinkoff.edu.java.scrapper.model.entity.converter.LinkStateConverter;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;

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

    @Column(name = "state", columnDefinition = "json")
    @Convert(converter = LinkStateConverter.class)
    @ColumnTransformer(write = "?::json")
    private ILinkState state;

    @Column(name = "last_scanned_at")
    private Instant lastScannedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "subscriptionId.link", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

}
