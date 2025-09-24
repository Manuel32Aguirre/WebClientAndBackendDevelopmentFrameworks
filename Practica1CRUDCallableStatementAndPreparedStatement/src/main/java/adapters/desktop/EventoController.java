package adapters.desktop;

import domain.dao.CategoriaDAO;
import domain.dao.EventoDAO;
import domain.dto.EventoDTO;
import domain.pojo.Evento;

import javax.swing.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class EventoController {

    private final EventoFrame view;
    private final EventoDAO dao;
    private final CategoriaDAO categoriaDao;

    public EventoController(EventoFrame view, Connection conn) {
        this.view = view;
        this.dao = new EventoDAO(conn);
        this.categoriaDao = new CategoriaDAO(conn);
        init();
    }

    private void init() {
        refresh();

        view.tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.tabla.getSelectedRow();
                if (row != -1) {
                    view.txtNombre.setText(view.modelo.getValueAt(row, 1).toString());
                    view.txtDescripcion.setText(view.modelo.getValueAt(row, 2).toString());

                    Object valorFecha = view.modelo.getValueAt(row, 3);
                    if (valorFecha != null) {
                        try {
                            Date fecha;
                            if (valorFecha instanceof Date) {
                                fecha = (Date) valorFecha;
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                fecha = sdf.parse(valorFecha.toString());
                            }
                            view.dateChooser.setDate(fecha);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    String categoriaNombre = view.modelo.getValueAt(row, 4).toString();
                    view.comboCategoria.setSelectedItem(categoriaNombre);

                }
            }
        });

        view.btnCrear.addActionListener(e -> {
            try {
                Evento ev = view.getFormEvento(null);
                boolean ok = dao.create(ev);
                JOptionPane.showMessageDialog(view,
                        ok ? "Evento creado con id=" + ev.getIdEvento() : "No se pudo crear");
                refresh();
                clearForm();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        view.btnActualizar.addActionListener(e -> {
            try {
                Integer id = view.getSelectedId();
                if (id == null) {
                    JOptionPane.showMessageDialog(view, "Selecciona un evento de la tabla");
                    return;
                }
                Evento ev = view.getFormEvento(id);
                boolean ok = dao.update(ev);
                JOptionPane.showMessageDialog(view,
                        ok ? "Evento actualizado" : "No se pudo actualizar");
                refresh();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        view.btnEliminar.addActionListener(e -> {
            try {
                Integer id = view.getSelectedId();
                if (id == null) {
                    JOptionPane.showMessageDialog(view, "Selecciona un evento de la tabla");
                    return;
                }
                Evento ev = new Evento(id, "", "", LocalDate.now(), 0);
                boolean ok = dao.delete(ev);
                JOptionPane.showMessageDialog(view,
                        ok ? "Evento eliminado" : "No se pudo eliminar");
                refresh();
                clearForm();
            } catch (Exception ex) {
                showError(ex);
            }
        });
    }

    private void refresh() {
        List<EventoDTO> eventos = dao.findAllWithCategoria();
        view.setData(eventos, categoriaDao);
    }

    private void clearForm() {
        view.txtNombre.setText("");
        view.txtDescripcion.setText("");
        view.dateChooser.setDate(null); // 👈 limpiar bien el calendar
        view.comboCategoria.setSelectedIndex(-1);
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
