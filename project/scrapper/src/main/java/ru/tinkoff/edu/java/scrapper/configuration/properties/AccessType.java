package ru.tinkoff.edu.java.scrapper.configuration.properties;

public enum AccessType {

    JDBC,
    JPA,
    JOOQ;

    public static final AccessType DEFAULT = AccessType.JDBC;

}
