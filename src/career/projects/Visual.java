package career.projects;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Visual extends JPanel implements ActionListener, MouseInputListener {

    List<Housing> housings;
    List<Column> datum;
    int max, min;
    public String[] allmonths = {
            "JAN",
            "FEB",
            "MAR",
            "APR",
            "MAY",
            "JUN",
            "JUL",
            "AUG",
            "SEP",
            "OCT",
            "NOV",
            "DEC"
    };

    private Rectangle box;
    private Point mouseDown;

    double w, h;


    Visual(){
        datum = new ArrayList<>();
        housings = new ArrayList<>();

        addMouseListener(this);
        addMouseMotionListener(this);
        w = getWidth();
        h = getHeight();

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
        //draw Fieles....
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
//        int spaceForData = (int) (winW*0.2);
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

        //Draw Label for the YAxise..
        int labelDecreament = (max-min)/3;
        int maxLabel= max;
        int yLabelPos = 25;
        double labelPosIncreament = (winH-40)/3;
        for(int i=0;i<4;i++){
            String label = String.valueOf(maxLabel);

            g.drawString(label,15,yLabelPos);

            yLabelPos += labelPosIncreament;
            maxLabel -= labelDecreament;
        }


        double hight = winH-40;

        for(Housing ele:housings){
            int size = ele.pricesValue.size();
            List<Integer> temp = ele.pricesValue;

            int degree = 0;
            for(int i=0; i<size; i++){

                double ratio = (numberTransform(temp.get(i),max, min)*hight-20);
                ele.xCordinates.add(xValues.get(i));
                ele.yCordinates.add(hight - ratio);

                //Get the data for sqare...
                double x = sqareNumberTransform("cos", ratio, degree);
                double y = sqareNumberTransform("sin", ratio, degree);

                ele.xPointsForSquare[i] = (int)x;
                ele.yPointsForSquare[i] = (int)y;

                degree +=30;
            }
        }

        final int fixedLen = 400;
        Point2D.Double sPos = new Point2D.Double(getWidth()*0.5, getHeight()*0.75);
        int degree2 = 0;
        for(int i=0;i<12;i++){
            g.setColor(new Color(0,0,0, 30));
            g.setStroke(new BasicStroke(3.0f));
            Point2D.Double ePos = new Point2D.Double(sqareNumberTransform("cos",fixedLen, degree2),sqareNumberTransform("sin",fixedLen, degree2));
            g.drawLine((int) sPos.x, (int) sPos.y, (int) ePos.x, (int) ePos.y);

            g.setColor(new Color(255,0,0));
            g.drawString(allmonths[i], (int) ePos.x-10, (int) ePos.y);

            degree2 += 30;
        }

        housings.forEach(ele->{
            ele.drawSquare(g);
            ele.pointify();
            ele.drawLine(g);

        });

        if(box != null){
            g.setColor(new Color(255,0,0,20));
            g.fill(box);
        }
    }

    public Double numberTransform(int data, int max, int min){
        double ratio = (double)(data-min)/(double)(max - min);
        return ratio;
    }

    public double sqareNumberTransform(String trig, double length, int degree){

        double x = getWidth()*0.5;
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mouseDown = new Point(x,y);
        box = new Rectangle();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        List<Housing> selected = new ArrayList<>();
        double testX = getWidth()*0.75;
        double testY = getHeight()*0.5+100;

        for(Housing ele:housings){
            ele.normalize();
            if(ele.getLine().intersects(box)){
                selected.add(ele);
            }
        }

        for(Housing ele:selected) {
            ele.setPointForHightlight(testX, testY);
            ele.highlight();

            testY += 30;
        }

        box = null;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        box.setFrameFromDiagonal(mouseDown.x, mouseDown.y, e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        double minDist = 1;
        Housing clostestLine = null;

        for(int i=0;i< housings.size();i++){
            housings.get(i).normalize();
            double distance = housings.get(i).getDistanceFromPoint(x,y);
            if(distance < minDist){
                minDist = distance;
                clostestLine = housings.get(i);

            }
        }
        if(clostestLine !=null){
            double testX = getWidth()*0.1;
            double testY = getHeight()*0.5+100;
            clostestLine.setPointForHover(testX, testY);
            clostestLine.setGreedingMsgPos(testX-30, testY-30);
            clostestLine.hover();
            repaint();
        }

    }
}
