package career.projects;

import javax.swing.*;

public class Main extends JFrame {

    private Visual contents;


    public Main(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setTitle("Final Project");
        contents = new Visual();
        setContentPane(contents);
        JMenuBar mb = createMenu();
        setJMenuBar(mb);
        setVisible(true);

    }

    private JMenuBar createMenu(){
        JMenuBar mb = new JMenuBar();
        JMenu years = new JMenu("DataTable");
        JMenu x = new JMenu("xAxis Options");
        JMenu y = new JMenu("yAxis Options");


        JMenuItem query1 = new JMenuItem("2019 Housing Data");
        query1.addActionListener(contents);
        query1.setActionCommand("SELECT * FROM hdb2019");
        years.add(query1);

        JMenuItem query2 = new JMenuItem("2020 Housing Data");
        query2.addActionListener(contents);
        query2.setActionCommand("SELECT * FROM hdb2020");
        years.add(query2);

        JMenuItem query3 = new JMenuItem("2021 Housing Data");
        query3.addActionListener(contents);
        query3.setActionCommand("SELECT * FROM hdb2021");
        years.add(query3);

//        for(int i=0; i<12; i++){
//            x.add(menuXitem(months[i]));
//            y.add(menuYitem(months[i]));
//        }
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
