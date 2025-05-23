/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import jooq.generated.Indexes;
import jooq.generated.Keys;
import jooq.generated.Public;
import jooq.generated.tables.Broker.BrokerPath;
import jooq.generated.tables.Office.OfficePath;
import jooq.generated.tables.records.PhoneNumberRecord;

import org.jooq.Check;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PhoneNumber extends TableImpl<PhoneNumberRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.phone_number</code>
     */
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PhoneNumberRecord> getRecordType() {
        return PhoneNumberRecord.class;
    }

    /**
     * The column <code>public.phone_number.id</code>.
     */
    public final TableField<PhoneNumberRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("gen_random_uuid()"), SQLDataType.UUID)), this, "");

    /**
     * The column <code>public.phone_number.number</code>.
     */
    public final TableField<PhoneNumberRecord, String> NUMBER = createField(DSL.name("number"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.phone_number.type</code>.
     */
    public final TableField<PhoneNumberRecord, String> TYPE = createField(DSL.name("type"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.phone_number.broker_id</code>.
     */
    public final TableField<PhoneNumberRecord, UUID> BROKER_ID = createField(DSL.name("broker_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>public.phone_number.office_id</code>.
     */
    public final TableField<PhoneNumberRecord, UUID> OFFICE_ID = createField(DSL.name("office_id"), SQLDataType.UUID, this, "");

    /**
     * The column <code>public.phone_number.created_at</code>.
     */
    public final TableField<PhoneNumberRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field(DSL.raw("now()"), SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    private PhoneNumber(Name alias, Table<PhoneNumberRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private PhoneNumber(Name alias, Table<PhoneNumberRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.phone_number</code> table reference
     */
    public PhoneNumber(String alias) {
        this(DSL.name(alias), PHONE_NUMBER);
    }

    /**
     * Create an aliased <code>public.phone_number</code> table reference
     */
    public PhoneNumber(Name alias) {
        this(alias, PHONE_NUMBER);
    }

    /**
     * Create a <code>public.phone_number</code> table reference
     */
    public PhoneNumber() {
        this(DSL.name("phone_number"), null);
    }

    public <O extends Record> PhoneNumber(Table<O> path, ForeignKey<O, PhoneNumberRecord> childPath, InverseForeignKey<O, PhoneNumberRecord> parentPath) {
        super(path, childPath, parentPath, PHONE_NUMBER);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class PhoneNumberPath extends PhoneNumber implements Path<PhoneNumberRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> PhoneNumberPath(Table<O> path, ForeignKey<O, PhoneNumberRecord> childPath, InverseForeignKey<O, PhoneNumberRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private PhoneNumberPath(Name alias, Table<PhoneNumberRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public PhoneNumberPath as(String alias) {
            return new PhoneNumberPath(DSL.name(alias), this);
        }

        @Override
        public PhoneNumberPath as(Name alias) {
            return new PhoneNumberPath(alias, this);
        }

        @Override
        public PhoneNumberPath as(Table<?> alias) {
            return new PhoneNumberPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.IDX_PHONE_BROKER_ID, Indexes.IDX_PHONE_OFFICE_ID);
    }

    @Override
    public UniqueKey<PhoneNumberRecord> getPrimaryKey() {
        return Keys.PHONE_NUMBER_PKEY;
    }

    @Override
    public List<ForeignKey<PhoneNumberRecord, ?>> getReferences() {
        return Arrays.asList(Keys.PHONE_NUMBER__PHONE_NUMBER_BROKER_ID_FKEY, Keys.PHONE_NUMBER__PHONE_NUMBER_OFFICE_ID_FKEY);
    }

    private transient BrokerPath _broker;

    /**
     * Get the implicit join path to the <code>public.broker</code> table.
     */
    public BrokerPath broker() {
        if (_broker == null)
            _broker = new BrokerPath(this, Keys.PHONE_NUMBER__PHONE_NUMBER_BROKER_ID_FKEY, null);

        return _broker;
    }

    private transient OfficePath _office;

    /**
     * Get the implicit join path to the <code>public.office</code> table.
     */
    public OfficePath office() {
        if (_office == null)
            _office = new OfficePath(this, Keys.PHONE_NUMBER__PHONE_NUMBER_OFFICE_ID_FKEY, null);

        return _office;
    }

    @Override
    public List<Check<PhoneNumberRecord>> getChecks() {
        return Arrays.asList(
            Internal.createCheck(this, DSL.name("phone_owner_check"), "((((broker_id IS NOT NULL) AND (office_id IS NULL)) OR ((broker_id IS NULL) AND (office_id IS NOT NULL))))", true)
        );
    }

    @Override
    public PhoneNumber as(String alias) {
        return new PhoneNumber(DSL.name(alias), this);
    }

    @Override
    public PhoneNumber as(Name alias) {
        return new PhoneNumber(alias, this);
    }

    @Override
    public PhoneNumber as(Table<?> alias) {
        return new PhoneNumber(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PhoneNumber rename(String name) {
        return new PhoneNumber(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PhoneNumber rename(Name name) {
        return new PhoneNumber(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PhoneNumber rename(Table<?> name) {
        return new PhoneNumber(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PhoneNumber where(Condition condition) {
        return new PhoneNumber(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PhoneNumber where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PhoneNumber where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PhoneNumber where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PhoneNumber where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PhoneNumber where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PhoneNumber where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PhoneNumber where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PhoneNumber whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PhoneNumber whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
