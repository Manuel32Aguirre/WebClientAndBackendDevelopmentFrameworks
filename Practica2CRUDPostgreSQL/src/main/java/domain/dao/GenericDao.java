package domain.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<TipoEntidad, TipoId> {
    boolean create(TipoEntidad entidad) throws SQLException;

    boolean update(TipoEntidad entidad) throws SQLException;

    boolean delete(TipoEntidad entidad) throws SQLException;

    List<TipoEntidad> findAll();

    List<TipoEntidad> findById(TipoId id);
}