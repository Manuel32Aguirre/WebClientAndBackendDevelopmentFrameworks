package adapters.desktop;

import domain.pojo.Evento;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EventoFrame extends JFrame {
    //Agregamos los campos de texto
    JTextField txtNombre = new JTextField(20);
    JTextField txtDescripcion = new JTextField(20);
    JTextField txtFecha = new JTextField(10);
    JTextField txtCategoria = new JTextField(5);

    //Agregamos los botones
    JButton btnCrear = new JButton("Crear");
    JButton btnActualizar = new JButton("Actualizar");
    JButton btnEliminar = new JButton("Eliminar");

    JLabel lblEvento = new JLabel("Eventos");

    //Tabla
    JTable tabla;
    DefaultTableModel modelo;

    public EventoFrame() {
        super("Eventos");

        //Configuracion basica
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
        form.add(txtFecha);
        form.add(new JLabel("Categoria: "));
        form.add(txtCategoria);

        JPanel header = new JPanel(new FlowLayout());
        lblEvento.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(lblEvento);

        //Panel de los botones
        JPanel acciones = new JPanel();
        acciones.add(btnCrear);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);

        JPanel contenedorCentro = new JPanel(new BorderLayout());
        contenedorCentro.add(form, BorderLayout.NORTH);
        contenedorCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        //Agregamos todo al frame
        add(header, BorderLayout.NORTH);
        add(contenedorCentro, BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);

    }

    public void setData(List<Evento> eventos) {
        modelo.setRowCount(0);
        for (Evento e : eventos) {
            Object[] fila = {
                    e.getIdEvento(),
                    e.getNombre(),        // asegúrate que aquí va el nombre
                    e.getDescripcion(),   // aquí la descripción
                    e.getFechaEvento(),   // aquí la fecha
                    e.getIdCategoria()    // aquí la categoría
            };
            modelo.addRow(fila);
        }
    }
    public Evento getFormEvento(Integer idIfSelectedOrNull) {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String fechaTexto = txtFecha.getText().trim();
        String idCatTexto = txtCategoria.getText().trim();

        // idEvento = 0 si es nuevo, o el que venga cuando sea actualización
        int idEvento = (idIfSelectedOrNull == null) ? 0 : idIfSelectedOrNull;

        // convertir fecha
        LocalDate fecha = null;
        if (!fechaTexto.isEmpty()) {
            fecha = LocalDate.parse(fechaTexto); // formato yyyy-MM-dd
        }

        // convertir idCategoria
        int idCategoria = 0;
        if (!idCatTexto.isEmpty()) {
            idCategoria = Integer.parseInt(idCatTexto);
        }

        return new Evento(idEvento, nombre, descripcion, fecha, idCategoria);
    }

    public Integer getSelectedId() {
        int row = tabla.getSelectedRow();
        if (row == -1) return null;
        return (Integer) modelo.getValueAt(row, 0);
    }


}
