package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionIdOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;
import ru.tinkoff.edu.java.scrapper.util.ObjectMapperUtil;

import javax.sql.DataSource;
import java.time.OffsetDateTime;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider
                (new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DefaultDSLContext dslContext(DataSourceConnectionProvider connectionProvider) {

        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();

        jooqConfiguration.set(connectionProvider);

        jooqConfiguration.setSQLDialect(SQLDialect.POSTGRES);

        var settings = new Settings().withRenderNameCase(RenderNameCase.LOWER);
        jooqConfiguration.setSettings(settings);

        return new DefaultDSLContext(jooqConfiguration);
    }

    @Bean
    public RowMapper<TgChatOutput> tgChatRowMapper() {
        return new BeanPropertyRowMapper<>(TgChatOutput.class);
    }

    @Bean
    public RowMapper<LinkOutput> linkRowMapper() {
        return (rs, rn) -> {
            LinkOutput linkOutput = new LinkOutput();
            linkOutput.setLinkId(rs.getLong("link_id"));
            linkOutput.setUrl(rs.getString("url"));
            var json = rs.getString("state");
            linkOutput.setState(ObjectMapperUtil.readValue(json, ILinkState.class));
            linkOutput.setLastScannedAt(rs.getObject("last_scanned_at", OffsetDateTime.class));
            linkOutput.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
            return linkOutput;
        };
    }

    @Bean
    public RowMapper<SubscriptionOutput> subscriptionRowMapper() {
        return new BeanPropertyRowMapper<>(SubscriptionOutput.class);
    }

    @Bean
    public RowMapper<SubscriptionIdOutput> subscriptionIdRowMapper() {
        return new BeanPropertyRowMapper<>(SubscriptionIdOutput.class);
    }

}
