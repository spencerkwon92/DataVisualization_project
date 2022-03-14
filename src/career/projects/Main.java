package career.projects;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private Visual contents;

    public Main(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 1200);
        setTitle("Final Project");
        contents = new Visual();
        setContentPane((Container) contents);
        JMenuBar mb = createMenu();
        setJMenuBar(mb);
        setVisible(true);

    }

    private JMenuBar createMenu(){
        JMenuBar mb = new JMenuBar();
        JMenu years = new JMenu("DATUME");

        JMenuItem query1 = new JMenuItem("2019 Housing Data");
        query1.addActionListener(contents);
        query1.setActionCommand("SELECT * FROM hdb2019");
        years.add(query1);

        JMenuItem query2 = new JMenuItem("SELECT * FROM hdb2019");
        query2.addActionListener(contents);
        query2.setActionCommand("");
        years.add(query2);

        JMenuItem query3 = new JMenuItem("SELECT * FROM hdb2019");
        query3.addActionListener(contents);
        query3.setActionCommand("");
        years.add(query3);

        mb.add(years);
        return mb;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
