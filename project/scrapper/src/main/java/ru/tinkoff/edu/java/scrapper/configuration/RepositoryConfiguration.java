package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

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

}
