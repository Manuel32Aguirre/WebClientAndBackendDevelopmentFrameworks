package adapters.desktop;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainFrame extends JFrame {
    private final Connection conn;
    private final JPanel panelContenido;

    public MainFrame(Connection conn) {
        super("Gestión de Categorías y Eventos");
        this.conn = conn;


        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Gestionar");

        JMenuItem menuCategorias = new JMenuItem("Categorías");
        JMenuItem menuEventos = new JMenuItem("Eventos");

        menu.add(menuCategorias);
        menu.add(menuEventos);
        menuBar.add(menu);
        setJMenuBar(menuBar);


        panelContenido = new JPanel(new BorderLayout());
        setContentPane(panelContenido);

        mostrarEventos();
        menuCategorias.addActionListener(e -> mostrarCategorias());
        menuEventos.addActionListener(e -> mostrarEventos());
    }

    private void mostrarCategorias() {
        panelContenido.removeAll();
        CategoriaFrame catFrame = new CategoriaFrame();
        new CategoriaController(catFrame, conn);
        panelContenido.add(catFrame.getContentPane(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarEventos() {
        panelContenido.removeAll();
        EventoFrame evtFrame = new EventoFrame();
        new EventoController(evtFrame, conn);
        panelContenido.add(evtFrame.getContentPane(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
