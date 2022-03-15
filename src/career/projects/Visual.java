package career.projects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Visual extends JPanel implements ActionListener {

    List<Housing> housings;
    List<Column> datum;
    Column[] dataTOcompare = new Column[2];

    private String[] months = {
            "JANUARY",
            "FEBRUARY",
            "MARCH",
            "APRIL",
            "MAY",
            "JUNE",
            "JULY",
            "AUGUST",
            "SEPTEMBER",
            "OCTOBER",
            "NOVEMBER",
            "DECEMBER"
    };

    Visual(){
        datum = new ArrayList<>();
        housings = new ArrayList<>();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        housings.clear();
        String sql = e.getActionCommand();

        try {
            Connection con = DriverManager.getConnection("jdbc:derby:housingDB");
            Statement statement =con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();

            // SQL API를 모아서.. 알맞게 클레스 생성해한단다..
            int dataSize = metaData.getColumnCount();
            for(int i=1; i<=dataSize; i++){
                Column cul;
                String name = metaData.getColumnLabel(i);
                int type = metaData.getColumnType(i);
                if(type == Types.VARCHAR){
                    cul = new Column(name, Column.Type.STRING);
                }else{
                    cul = new Column(name,Column.Type.INTEGER);
                }
                datum.add(cul);
            }

            while(rs.next()) {
                for (Column ele : datum) {
                    if (ele.type == Column.Type.STRING) {
                        ele.stringData.add(rs.getString(ele.culName));
                    } else {
                        ele.intData.add(rs.getInt(ele.culName));
                    }
                }
            }

            String xAxis = (String) JOptionPane.showInputDialog(null,
                    "Please choose X axis for the chart.", "X AXIS", JOptionPane.QUESTION_MESSAGE, null, months,months[0]);
            String yAxis = (String) JOptionPane.showInputDialog(null,
                    "Please choose Y axis for the chart.", "Y ASIS", JOptionPane.QUESTION_MESSAGE, null, months,months[0]);

            for(Column ele:datum){
                if(ele.culName.equals(xAxis)){
                    dataTOcompare[0] = ele;
                }
                if(ele.culName.equals(yAxis)){
                    dataTOcompare[1] = ele;
                }
            }

            System.out.println(dataTOcompare[0].intData);
            System.out.println(dataTOcompare[1].intData);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void paintComponent(Graphics g1){

        Graphics2D g = (Graphics2D) g1;
        //draw paralell Coordinate filde...
        int space = 20;
        double winW= getWidth();
        double winH = getHeight();
    }
}
