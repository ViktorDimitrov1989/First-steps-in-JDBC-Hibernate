package miniORM.entities;

import miniORM.contracts.DbContext;
import miniORM.persitance.Column;
import miniORM.persitance.Entity;
import miniORM.persitance.Id;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class EntityManager implements DbContext {
    private Connection conncetion;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    private Set<Object> persistedObejcts;

    public EntityManager(Connection connection) {
        this.conncetion = connection;
        this.persistedObejcts = new HashSet<>();
    }

    @Override
    public <E> boolean persist(E entity) throws SQLException {
        Field primary = this.getId(entity.getClass());
        this.doCreate(entity, primary);


        return false;
    }

    @Override
    public <E> Iterable<E> find(Class<E> table) {
        return null;
    }

    @Override
    public <E> Iterable<E> find(Class<E> table, String whereClause) {
        return null;
    }

    @Override
    public <E> E findFirst(Class<E> table) {
        return null;
    }

    @Override
    public <E> E findLast(Class<E> table) {
        return null;
    }

    private <E> boolean doCreate(E entity, Field primary) throws SQLException {
        String tableName = this.getTableName(entity.getClass());

        String sqlCreate = "CREATE TABLE IF NOT EXISTS " +  tableName + "( ";

        String columns = "";

        Field[] fields = entity.getClass().getDeclaredFields();


        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            if(field.getName().equals(primary.getName())){
                columns += "`" + this.getFieldName(field) + "` "
                        + this.getDatabaseType(field)
                        + " PRIMARY KEY AUTO_INCREMENT ";
            }else{
                columns += "`" + this.getFieldName(field) + "` "
                        + this.getDatabaseType(field);
            }

            field.setAccessible(true);

            if(i < fields.length - 1){
                columns += ", ";
            }

        }

        sqlCreate += columns + ")";
        
        return this.conncetion.prepareStatement(sqlCreate).execute();
    }

    private String getDatabaseType(Field field) {
        String type = field.getType().getSimpleName().toLowerCase();

        switch (type){
            case "int": 
                return "INT";
            case "string": 
                return "VARCHAR(50)";
            case "long": 
                return "LONG";
            case "date": 
                return "DATE";
            default: 
                return null;
        }

    }

    //Returns correct table name
    private <E> String getTableName(Class<E> entity) {
        String tableName = "";

        if (entity.isAnnotationPresent(Entity.class)) {
            Entity entityAnnotation = entity.getAnnotation(Entity.class);
            tableName = entityAnnotation.name();
        }

        if (tableName.isEmpty()) {
            tableName = entity.getSimpleName();
        }

        return tableName;
    }

    //Returns correct field name
    private String getFieldName(Field field) {
        String fieldName = "";

        if (field.isAnnotationPresent(Column.class)) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            fieldName = columnAnnotation.name();
        }

        if (fieldName.isEmpty()) {
            fieldName = field.getName();
        }

        return fieldName;
    }

    //Return id field of the class
    private Field getId(Class c) {
        return Arrays.stream(c.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(""));
    }
}
