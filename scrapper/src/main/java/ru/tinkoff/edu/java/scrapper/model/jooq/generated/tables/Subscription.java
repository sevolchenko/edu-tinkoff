/*
 * This file is generated by jOOQ.
 */
package ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.DefaultSchema;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.Keys;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables.records.SubscriptionRecord;

import javax.annotation.processing.Generated;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "https://www.jooq.org",
                "jOOQ version:3.18.3"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Subscription extends TableImpl<SubscriptionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>SUBSCRIPTION</code>
     */
    public static final Subscription SUBSCRIPTION = new Subscription();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<SubscriptionRecord> getRecordType() {
        return SubscriptionRecord.class;
    }

    /**
     * The column <code>SUBSCRIPTION.TG_CHAT_ID</code>.
     */
    public final TableField<SubscriptionRecord, Long> TG_CHAT_ID = createField(DSL.name("TG_CHAT_ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>SUBSCRIPTION.LINK_ID</code>.
     */
    public final TableField<SubscriptionRecord, Long> LINK_ID = createField(DSL.name("LINK_ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>SUBSCRIPTION.CREATED_AT</code>.
     */
    public final TableField<SubscriptionRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    private Subscription(Name alias, Table<SubscriptionRecord> aliased) {
        this(alias, aliased, null);
    }

    private Subscription(Name alias, Table<SubscriptionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>SUBSCRIPTION</code> table reference
     */
    public Subscription(String alias) {
        this(DSL.name(alias), SUBSCRIPTION);
    }

    /**
     * Create an aliased <code>SUBSCRIPTION</code> table reference
     */
    public Subscription(Name alias) {
        this(alias, SUBSCRIPTION);
    }

    /**
     * Create a <code>SUBSCRIPTION</code> table reference
     */
    public Subscription() {
        this(DSL.name("SUBSCRIPTION"), null);
    }

    public <O extends Record> Subscription(Table<O> child, ForeignKey<O, SubscriptionRecord> key) {
        super(child, key, SUBSCRIPTION);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public Identity<SubscriptionRecord, Long> getIdentity() {
        return (Identity<SubscriptionRecord, Long>) super.getIdentity();
    }

    @Override
    @NotNull
    public UniqueKey<SubscriptionRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_9E7;
    }

    @Override
    @NotNull
    public List<ForeignKey<SubscriptionRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_9, Keys.CONSTRAINT_9E);
    }

    private transient TgChat _tgChat;
    private transient Link _link;

    /**
     * Get the implicit join path to the <code>PUBLIC.TG_CHAT</code> table.
     */
    public TgChat tgChat() {
        if (_tgChat == null)
            _tgChat = new TgChat(this, Keys.CONSTRAINT_9);

        return _tgChat;
    }

    /**
     * Get the implicit join path to the <code>PUBLIC.LINK</code> table.
     */
    public Link link() {
        if (_link == null)
            _link = new Link(this, Keys.CONSTRAINT_9E);

        return _link;
    }

    @Override
    @NotNull
    public Subscription as(String alias) {
        return new Subscription(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public Subscription as(Name alias) {
        return new Subscription(alias, this);
    }

    @Override
    @NotNull
    public Subscription as(Table<?> alias) {
        return new Subscription(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Subscription rename(String name) {
        return new Subscription(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Subscription rename(Name name) {
        return new Subscription(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Subscription rename(Table<?> name) {
        return new Subscription(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, Long, OffsetDateTime> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Long, ? super Long, ? super OffsetDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Long, ? super Long, ? super OffsetDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}