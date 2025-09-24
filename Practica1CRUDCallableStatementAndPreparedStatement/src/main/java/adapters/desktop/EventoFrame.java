package adapters.desktop;

import com.toedter.calendar.JDateChooser;
import domain.dao.CategoriaDAO;
import domain.dto.EventoDTO;
import domain.pojo.Categoria;
import domain.pojo.Evento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EventoFrame extends JFrame {

    JTextField txtNombre = new JTextField(20);
    JTextField txtDescripcion = new JTextField(20);
    JDateChooser dateChooser = new JDateChooser();


    JButton btnCrear = new JButton("Crear");
    JButton btnActualizar = new JButton("Actualizar");
    JButton btnEliminar = new JButton("Eliminar");

    JLabel lblEvento = new JLabel("Eventos");

    JComboBox comboCategoria = new JComboBox();

    //Tabla
    JTable tabla;
    DefaultTableModel modelo;

    public EventoFrame() {
        super("Eventos");


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        String[] columnas = {"ID", "Nombre", "Descripcion", "Fecha", "Categoria"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("Nombre: "));
        form.add(txtNombre);
        form.add(new JLabel("Descripcion: "));
        form.add(txtDescripcion);
        form.add(new JLabel("Fecha (yyyy-mm-dd):  "));
        form.add(dateChooser);

        form.add(new JLabel("Categoria: "));
        form.add(comboCategoria);

        JPanel header = new JPanel(new FlowLayout());
        lblEvento.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(lblEvento);

        JPanel acciones = new JPanel();
        acciones.add(btnCrear);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);

        JPanel contenedorCentro = new JPanel(new BorderLayout());
        contenedorCentro.add(form, BorderLayout.NORTH);
        contenedorCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);


        add(header, BorderLayout.NORTH);
        add(contenedorCentro, BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);

    }

    public void setData(List<EventoDTO> eventos, CategoriaDAO dao) {
        modelo.setRowCount(0);
        for (EventoDTO e : eventos) {
            Object[] fila = {
                    e.getIdEvento(),
                    e.getNombre(),
                    e.getDescripcion(),
                    e.getFechaEvento(),
                    e.getNombreCategoria()
            };
            modelo.addRow(fila);
        }
        comboCategoria.removeAllItems();

        List<Categoria> categorias = new ArrayList<>();
        categorias = dao.findAll();
        for (Categoria c : categorias) {
            comboCategoria.addItem(c);
        }
    }

    public Evento getFormEvento(Integer idIfSelectedOrNull) {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();


        int idEvento = (idIfSelectedOrNull == null) ? 0 : idIfSelectedOrNull;

        LocalDate fecha = null;
        if (dateChooser.getDate() != null) {
            fecha = dateChooser.getDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
        }

        Categoria selected = (Categoria) comboCategoria.getSelectedItem();
        int idCategoria = (selected != null) ? selected.getIdCategoria() : 0;

        return new Evento(idEvento, nombre, descripcion, fecha, idCategoria);
    }


    public Integer getSelectedId() {
        int row = tabla.getSelectedRow();
        if (row == -1) return null;
        return (Integer) modelo.getValueAt(row, 0);
    }


}
