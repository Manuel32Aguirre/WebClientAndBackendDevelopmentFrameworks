package domain.dao;

import domain.pojo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaDAO implements GenericDao<Categoria, Integer> {
    private static final String SQL_INSERT = "INSERT INTO Categoria(nombreCategoria, descripcion) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE Categoria SET nombreCategoria = ?, descripcion = ? WHERE idCategoria = ?";
    private static final String SQL_DELETE = "DELETE FROM Categoria WHERE idCategoria = ?";
    private static final String SQL_SELECT = "SELECT * FROM Categoria WHERE idCategoria = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM Categoria";

    private final Connection conexion;

    public CategoriaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean create(Categoria c) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            int affected = ps.executeUpdate();
            if (affected == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setIdCategoria(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, "Error al insertar la categoria", ex);
            throw ex;
        }
        return false;
    }

    @Override
    public boolean update(Categoria c) throws SQLException {
        if (c == null) throw new IllegalArgumentException("categoria null");
        if (c.getIdCategoria() <= 0) throw new IllegalArgumentException("idCategoria invalido");

        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getIdCategoria());
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, "Error al actualizar la categoria", ex);
            throw ex;
        }
    }

    @Override
    public boolean delete(Categoria c) throws SQLException {
        if (c == null) throw new IllegalArgumentException("categoria null");
        if (c.getIdCategoria() <= 0) throw new IllegalArgumentException("idCategoria invalido");

        try (PreparedStatement ps = conexion.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, c.getIdCategoria());
            int affected = ps.executeUpdate();
            return affected == 1;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, "Error al eliminar la categoria", ex);
            throw ex;
        }
    }

    @Override
    public List<Categoria> findAll() {
        List<Categoria> list = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("idCategoria"));
                c.setNombreCategoria(rs.getString("nombreCategoria"));
                c.setDescripcion(rs.getString("descripcion"));
                list.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, "Error al consultar todas las categorias", ex);
        }
        return list;
    }

    @Override
    public List<Categoria> findById(Integer id) {
        List<Categoria> list = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria c = new Categoria();
                    c.setIdCategoria(rs.getInt("idCategoria"));
                    c.setNombreCategoria(rs.getString("nombreCategoria"));
                    c.setDescripcion(rs.getString("descripcion"));
                    list.add(c);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, "Error al consultar categoria por id", ex);
        }
        return list;
    }
}