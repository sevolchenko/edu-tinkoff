/*
 * This file is generated by jOOQ.
 */
package ru.tinkoff.edu.java.scrapper.model.jooq.generated;


import javax.annotation.processing.Generated;

import org.jooq.Sequence;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;


/**
 * Convenience access to all sequences in the default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>LINK_SEQUENCE</code>
     */
    public static final Sequence<Long> LINK_SEQUENCE = Internal.createSequence("LINK_SEQUENCE", DefaultSchema.DEFAULT_SCHEMA, SQLDataType.BIGINT, null, null, null, null, false, null);
}
