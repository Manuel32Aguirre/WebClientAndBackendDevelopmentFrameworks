package adapters.desktop;

import domain.dao.CategoriaDAO;
import domain.pojo.Categoria;

import javax.swing.*;
import java.sql.Connection;

public class CategoriaController {
    private final CategoriaFrame view;
    private final CategoriaDAO dao;
    private final Connection conn;

    public CategoriaController(CategoriaFrame view, Connection conn) {
        this.view = view;
        this.conn = conn;
        this.dao = new CategoriaDAO(conn);
        init();
    }

    private void init() {
        refresh();

        view.tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) view.fillFormFromSelection();
        });

        view.btnCrear.addActionListener(e -> {
            try {
                Categoria c = view.getFormCategoria(null);
                if (c.getNombreCategoria().isEmpty() || c.getDescripcion().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Completa nombre y descripción");
                    return;
                }
                boolean ok = dao.create(c);
                JOptionPane.showMessageDialog(view, ok ? "Creado (id=" + c.getIdCategoria() + ")" : "No se pudo crear");
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
                    JOptionPane.showMessageDialog(view, "Selecciona una fila");
                    return;
                }
                Categoria c = view.getFormCategoria(id);
                boolean ok = dao.update(c);
                JOptionPane.showMessageDialog(view, ok ? "Actualizado" : "No se actualizó");
                refresh();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        view.btnEliminar.addActionListener(e -> {
            try {
                Integer id = view.getSelectedId();
                if (id == null) {
                    JOptionPane.showMessageDialog(view, "Selecciona una fila");
                    return;
                }
                Categoria c = new Categoria(id, "", "");
                boolean ok = dao.delete(c);
                JOptionPane.showMessageDialog(view, ok ? "Eliminado" : "No se eliminó");
                refresh();
                clearForm();
            } catch (Exception ex) {
                showError(ex);
            }
        });
    }

    private void refresh() {
        view.setData(dao.findAll());
    }

    private void clearForm() {
        view.txtNombre.setText("");
        view.txtDesc.setText("");
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
