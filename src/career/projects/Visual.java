package career.projects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Visual extends JPanel implements ActionListener {

    List<Housing> housings;
    List<Column> datum;
    int max, min;
    public String[] allmonths = {
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
        datum.clear();

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
            //Getter the data and save to the right list...
            int numOfculumn = datum.size(); // 15
            int numOfrow = datum.get(0).intData.size(); //94

            for(int i=0; i<numOfrow;i++){
                Housing h = new Housing();
                housings.add(h);
            }

            for(int i=0; i<numOfculumn; i++){
                Column target = datum.get(i);

                for(int j=0; j<numOfrow; j++){
                    Housing targetHousing = housings.get(j);
                    if(target.culName.equals("HOUSING_ID")){
                        targetHousing.setID(target.intData.get(j));

                    }else if(target.culName.equals("SIZE")){
                        targetHousing.setSize(target.intData.get(j));
                    }else if(target.culName.equals("STATE")){
                        targetHousing.setState(target.stringData.get(j));
                    }else{
                        if(targetHousing.pricesValue != null){
                            targetHousing.pricesValue.add(target.intData.get(j));
                        }
                    }
                }
            }

            //get the max, min value.
            max = Integer.MIN_VALUE;
            min = Integer.MAX_VALUE;

            for(int i= 3;i<numOfculumn;i++){
                Column col = datum.get(i);
                for(int j=0;j<numOfrow;j++){
                    int data = col.intData.get(j);
                    if(data>max){
                        max = data;
                    }
                    if(data<min){
                        min = data;
                    }
                }
            }

            repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g1){

        Graphics2D g = (Graphics2D) g1;

        List<Double> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        //draw Files....
        int space = 10;
        double winW= getWidth()-(space*2);
        double winH = (getHeight()-(space*2))/2;

        Point sp = new Point(space, space);
        Point sp2 = new Point(space, (int) winH+space);
        g.drawRect(sp.x, sp.y, (int) winW, (int) winH);
        g.drawRect(sp2.x, sp2.y, (int) winW, (int) winH);

        // DRAW ASIX..

        int months = 12;
        int spaceForData = 80;
        double xAiseIncreament = (winW - 60)/months;
        int lengthForChar = 2;

        for(int i=0; i<months; i++){
            Point sp3 = new Point(spaceForData, space*2);
            Point ep3 = new Point(spaceForData, (int) (winH-20));

            String month = allmonths[i];
            double lableSize = month.length()*lengthForChar;
            g.drawLine(sp3.x, sp3.y, ep3.x, ep3.y);
            g.drawString(month,(int)(ep3.x-lableSize*2), ep3.y+space*2);

            xValues.add((double)spaceForData);
            spaceForData += xAiseIncreament;
        }

        double hight = winH-40;

        for(Housing ele:housings){
            int size = ele.pricesValue.size();
            List<Integer> temp = ele.pricesValue;

            int degree = 0;
            for(int i=0; i<size; i++){

                double ratio = hight - (numberTransform(temp.get(i),max, min)*hight-20);
                ele.xCordinates.add(xValues.get(i));
                ele.yCordinates.add(ratio);

                //Get the data for sqare...
                double x = spareNumberTransform("cos", ratio, degree);
                double y = spareNumberTransform("sin", ratio, degree);

                ele.xPointsForSquare[i] = (int)x;
                ele.yPointsForSquare[i] = (int)y;

                degree +=30;
            }
        }

        housings.forEach(ele->{
            ele.drawSquare(g);
            ele.pointify();
            ele.drawLine(g);

//            System.out.println(ele.xCordinates);
//            System.out.println(ele.yCordinates);
//            System.out.println(Arrays.toString(ele.xPointsForSquare));
//            System.out.println(Arrays.toString(ele.yPointsForSquare));

        });

    }

    public Double numberTransform(int data, int max, int min){
        double ratio = (double)(data-min)/(double)(max - min);
        return ratio;
    }

    public double spareNumberTransform(String trig, double length, int degree){

        double x = getWidth()* 0.5;
        double y = getHeight()*0.75;

        double result = 0;
        if(trig.equals("sin")){
            result = y-(Math.sin(Math.toRadians(degree))*length/2);
        }

        if(trig.equals("cos")){
            result = x-(Math.cos(Math.toRadians(degree))*length/2);
        }

        return result;
    }



//    public void numberTransform(List<Integer> data,List<Double> result, int max, int min){
//
//        for(int ele:data){
//            double ratio = (double)(ele-min)/(double)(max - min);
//            result.add(ratio);
//        }
//    }
//



}
