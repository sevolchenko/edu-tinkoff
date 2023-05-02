package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.entity.Subscription;
import ru.tinkoff.edu.java.scrapper.model.entity.SubscriptionId;

@Repository
public interface JpaSubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    boolean existsBySubscriptionId(SubscriptionId subscriptionId);

}
