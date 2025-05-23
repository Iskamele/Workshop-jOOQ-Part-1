/*
 * This file is generated by jOOQ.
 */
package jooq.generated.tables;


import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

import jooq.generated.Keys;
import jooq.generated.Public;
import jooq.generated.tables.Address.AddressPath;
import jooq.generated.tables.records.GisRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
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
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Gis extends TableImpl<GisRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.gis</code>
     */
    public static final Gis GIS = new Gis();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GisRecord> getRecordType() {
        return GisRecord.class;
    }

    /**
     * The column <code>public.gis.id</code>.
     */
    public final TableField<GisRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("gen_random_uuid()"), SQLDataType.UUID)), this, "");

    /**
     * The column <code>public.gis.latitude</code>.
     */
    public final TableField<GisRecord, Double> LATITUDE = createField(DSL.name("latitude"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>public.gis.longitude</code>.
     */
    public final TableField<GisRecord, Double> LONGITUDE = createField(DSL.name("longitude"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>public.gis.created_at</code>.
     */
    public final TableField<GisRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field(DSL.raw("now()"), SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    private Gis(Name alias, Table<GisRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Gis(Name alias, Table<GisRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.gis</code> table reference
     */
    public Gis(String alias) {
        this(DSL.name(alias), GIS);
    }

    /**
     * Create an aliased <code>public.gis</code> table reference
     */
    public Gis(Name alias) {
        this(alias, GIS);
    }

    /**
     * Create a <code>public.gis</code> table reference
     */
    public Gis() {
        this(DSL.name("gis"), null);
    }

    public <O extends Record> Gis(Table<O> path, ForeignKey<O, GisRecord> childPath, InverseForeignKey<O, GisRecord> parentPath) {
        super(path, childPath, parentPath, GIS);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class GisPath extends Gis implements Path<GisRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> GisPath(Table<O> path, ForeignKey<O, GisRecord> childPath, InverseForeignKey<O, GisRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private GisPath(Name alias, Table<GisRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public GisPath as(String alias) {
            return new GisPath(DSL.name(alias), this);
        }

        @Override
        public GisPath as(Name alias) {
            return new GisPath(alias, this);
        }

        @Override
        public GisPath as(Table<?> alias) {
            return new GisPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<GisRecord> getPrimaryKey() {
        return Keys.GIS_PKEY;
    }

    private transient AddressPath _address;

    /**
     * Get the implicit to-many join path to the <code>public.address</code>
     * table
     */
    public AddressPath address() {
        if (_address == null)
            _address = new AddressPath(this, null, Keys.ADDRESS__FK_ADDRESS_GIS.getInverseKey());

        return _address;
    }

    @Override
    public Gis as(String alias) {
        return new Gis(DSL.name(alias), this);
    }

    @Override
    public Gis as(Name alias) {
        return new Gis(alias, this);
    }

    @Override
    public Gis as(Table<?> alias) {
        return new Gis(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Gis rename(String name) {
        return new Gis(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Gis rename(Name name) {
        return new Gis(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Gis rename(Table<?> name) {
        return new Gis(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Gis where(Condition condition) {
        return new Gis(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Gis where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Gis where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Gis where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Gis where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Gis where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Gis where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Gis where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Gis whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Gis whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
