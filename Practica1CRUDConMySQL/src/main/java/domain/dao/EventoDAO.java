package domain.dao;

import domain.dto.EventoDTO;
import domain.pojo.Evento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventoDAO implements GenericDao<Evento, Integer> {

    private static final String SQL_INSERT = "{ call create_evento(?, ?, ?, ?, ?) }";
    private static final String SQL_UPDATE = "{ call update_evento(?, ?, ?, ?, ?) }";
    private static final String SQL_DELETE = "{ call delete_evento(?) }";
    private static final String SQL_SELECT_BY_ID = "{ call find_evento_by_id(?) }";
    private static final String SQL_SELECT_ALL = "{ call select_all_eventos() }";
    private static final String SQL_SELECT_ALL_WITH_CATEGORIA = "{ call select_all_eventos_with_categoria() } ";


    private final Connection conexion;

    public EventoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean create(Evento evt) throws SQLException {
        if (evt == null) throw new IllegalArgumentException("Evento null");

        try (CallableStatement cs = conexion.prepareCall(SQL_INSERT)) {
            cs.setString(1, evt.getNombre());
            cs.setString(2, evt.getDescripcion());
            cs.setDate(3, Date.valueOf(evt.getFechaEvento()));
            cs.setInt(4, evt.getIdCategoria());
            cs.registerOutParameter(5, Types.INTEGER);

            boolean ok = cs.executeUpdate() == 1;
            evt.setIdEvento(cs.getInt(5)); // recuperamos el ID generado
            return ok;
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, "Error al crear evento", ex);
            throw ex;
        }
    }

    @Override
    public boolean update(Evento evt) throws SQLException {
        if (evt == null) throw new IllegalArgumentException("Evento null");
        if (evt.getIdEvento() <= 0) throw new IllegalArgumentException("idEvento inválido");

        try (CallableStatement cs = conexion.prepareCall(SQL_UPDATE)) {
            cs.setInt(1, evt.getIdEvento());
            cs.setString(2, evt.getNombre());
            cs.setString(3, evt.getDescripcion());
            cs.setDate(4, Date.valueOf(evt.getFechaEvento()));
            cs.setInt(5, evt.getIdCategoria());

            return cs.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, "Error al actualizar evento", ex);
            throw ex;
        }
    }

    @Override
    public boolean delete(Evento evt) throws SQLException {
        if (evt == null) throw new IllegalArgumentException("Evento null");
        if (evt.getIdEvento() <= 0) throw new IllegalArgumentException("idEvento inválido");

        try (CallableStatement cs = conexion.prepareCall(SQL_DELETE)) {
            cs.setInt(1, evt.getIdEvento());
            return cs.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, "Error al eliminar evento", ex);
            throw ex;
        }
    }

    @Override
    public List<Evento> findAll() {
        List<Evento> list = new ArrayList<>();

        try (CallableStatement cs = conexion.prepareCall(SQL_SELECT_ALL);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                Evento ev = new Evento();
                ev.setIdEvento(rs.getInt("idEvento"));
                ev.setNombre(rs.getString("nombre"));
                ev.setDescripcion(rs.getString("descripcion"));
                ev.setFechaEvento(rs.getDate("fechaEvento").toLocalDate());
                ev.setIdCategoria(rs.getInt("idCategoria"));
                list.add(ev);

            }
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, "Error al consultar todos los eventos", ex);
        }
        return list;
    }

    @Override
    public List<Evento> findById(Integer id) {
        List<Evento> lista = new ArrayList<>();
        if (id == null || id <= 0) return lista;

        try (CallableStatement cs = conexion.prepareCall(SQL_SELECT_BY_ID)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Evento e = new Evento(
                            rs.getInt("idEvento"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDate("fechaEvento").toLocalDate(),
                            rs.getInt("idCategoria")
                    );
                    lista.add(e);

                    String catNombre = rs.getString("nombreCategoria");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, "Error al consultar evento por id", ex);
        }
        return lista;
    }

    public List<EventoDTO> findAllWithCategoria() {
        List<EventoDTO> lista = new ArrayList<>();
        try (CallableStatement cs = conexion.prepareCall(SQL_SELECT_ALL_WITH_CATEGORIA)) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                EventoDTO ev = new EventoDTO(
                        rs.getInt("idEvento"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fechaEvento").toLocalDate(),
                        rs.getString("nombreCategoria")
                );
                lista.add(ev);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}