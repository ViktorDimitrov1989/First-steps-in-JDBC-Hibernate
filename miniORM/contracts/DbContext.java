package miniORM.contracts;

import java.sql.SQLException;

public interface DbContext{

    <E> boolean persist(E entity) throws SQLException;

    <E> Iterable<E> find(Class<E> table);

    <E> Iterable<E> find(Class<E> table, String whereClause);

    <E> E findFirst(Class<E> table);

    <E> E findLast(Class<E> table);
}
