package adapters.desktop;

import domain.pojo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaFrame extends JFrame {

    // Campos de texto
    JTextField txtNombre = new JTextField(20);
    JTextField txtDesc = new JTextField(20);

    // Botones
    JButton btnCrear = new JButton("Crear");
    JButton btnActualizar = new JButton("Actualizar");
    JButton btnEliminar = new JButton("Eliminar");
    JLabel lblCategoria = new JLabel("Categorías");

    // Tabla y su modelo
    JTable tabla;
    DefaultTableModel modelo;

    public CategoriaFrame() {
        super("Categorías"); // título de la ventana

        // Configuración básica de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null); // centrar en pantalla

        // Definir columnas de la tabla
        String[] columnas = {"ID", "Nombre", "Descripción"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);

        // Solo una fila seleccionada
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel header =  new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblCategoria.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(lblCategoria);
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(header);
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Descripción:"));
        form.add(txtDesc);

        // Panel de botones
        JPanel acciones = new JPanel();
        acciones.add(btnCrear);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(form, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);      // botones abajo
    }

    // Cargar datos a la tabla
    public void setData(List<Categoria> categorias) {
        modelo.setRowCount(0); // limpiar tabla
        for (Categoria c : categorias) {
            Object[] fila = {c.getIdCategoria(), c.getNombreCategoria(), c.getDescripcion()};
            modelo.addRow(fila); // agregar cada fila
        }
    }

    // Crear un objeto Categoria con los datos escritos en la UI
    public Categoria getFormCategoria(Integer id) {
        String nombre = txtNombre.getText().trim();
        String desc = txtDesc.getText().trim();
        int categoriaId = (id == null) ? 0 : id;
        return new Categoria(categoriaId, nombre, desc);
    }

    // Obtener el ID de la fila seleccionada en la tabla
    public Integer getSelectedId() {
        int row = tabla.getSelectedRow();
        if (row == -1) return null; // nada seleccionado
        return (Integer) modelo.getValueAt(row, 0);
    }

    // Rellenar el formulario con lo que hay en la fila seleccionada
    public void fillFormFromSelection() {
        int row = tabla.getSelectedRow();
        if (row != -1) {
            txtNombre.setText(modelo.getValueAt(row, 1).toString());
            txtDesc.setText(modelo.getValueAt(row, 2).toString());
        }
    }
}
