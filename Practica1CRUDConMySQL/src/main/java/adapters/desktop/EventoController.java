    package adapters.desktop;

    import domain.dao.CategoriaDAO;
    import domain.dao.EventoDAO;
    import domain.dto.EventoDTO;
    import domain.pojo.Evento;

    import javax.swing.*;
    import java.sql.Connection;
    import java.time.LocalDate;
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

            // Al seleccionar fila, llenar formulario
            view.tabla.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int row = view.tabla.getSelectedRow();
                    if (row != -1) {
                        view.txtNombre.setText(view.modelo.getValueAt(row, 1).toString());
                        view.txtDescripcion.setText(view.modelo.getValueAt(row, 2).toString());
                        view.txtFecha.setText(view.modelo.getValueAt(row, 3).toString());
                        view.txtCategoria.setText(view.modelo.getValueAt(row, 4).toString());
                    }
                }
            });

            // Botón CREAR
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

            // Botón ACTUALIZAR
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

            // Botón ELIMINAR
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
            view.txtFecha.setText("");
            view.txtCategoria.setText("");
        }

        private void showError(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
