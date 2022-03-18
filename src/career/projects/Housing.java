package career.projects;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Housing {
    private enum Status {
        NORMAL,
        HIGHLIGHTED,
        INVISIBLE,
        HOVERED
    }

    private int id;
    private int size;
    private String state;
    private GeneralPath line;
    private List<Point2D> points;
    private boolean isDraw;
    private Status currentStatus;
    private Point2D highlightInfoPos;
    private Point2D hoverInfoPos;
    private Point2D greedingMsgPos;


    List<Integer> pricesValue;
    List<Double> xCordinates, yCordinates;
    int[] xPointsForSquare = new int[12];
    int[] yPointsForSquare = new int[12];

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

    public Housing(){
        pricesValue = new ArrayList<>();
        xCordinates = new ArrayList<>();
        yCordinates = new ArrayList<>(); // 변환된 길이값이 저장되어 있음...
        points = new ArrayList<>();
        line = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        currentStatus = Status.NORMAL;
        highlightInfoPos = new Point2D.Double(0,0);
        hoverInfoPos = new Point2D.Double(0,0);
        greedingMsgPos = new Point2D.Double(0,0);
        isDraw = false;
    }
    public Housing(int id, int size, String state){
        this.id = id;
        this.size = size;
        this.state = state;
    }

    public void pointify(){
        if(xCordinates.size() == 12 && yCordinates.size() == 12){
            for(int i=0; i<12; i++){
                Point2D point = new Point2D.Double(xCordinates.get(i), yCordinates.get(i));
                points.add(point);
            }
        }
    }

    public void drawLine(Graphics2D g){
        for(int i=0; i< points.size(); i++){
            double x = points.get(i).getX();
            double y = points.get(i).getY();

            if(i == 0){
                line.moveTo(x,y);
            }else{
                line.lineTo(x,y);
            }
        }
        if(currentStatus ==Status.NORMAL){
            g.setStroke(new BasicStroke(1.0f));
            g.setColor(Color.BLACK);
        }

        if(currentStatus ==Status.INVISIBLE){
            g.setStroke(new BasicStroke(1.0f));
            g.setColor(new Color(0,0,0, 5));
        }
        if(currentStatus == Status.HIGHLIGHTED || currentStatus == Status.HOVERED){
            g.setStroke(new BasicStroke(4.0f));
            g.setColor(new Color(5, 195, 221));
        }


        g.draw(line);
    }

    public void drawSquare(Graphics2D g){
        int rad=new Random().nextInt(255);
        int green=new Random().nextInt(255);
        int blue=new Random().nextInt(255);

        if(currentStatus != currentStatus.NORMAL){

            Color color = new Color(rad, green, blue, 50);

            if(currentStatus == Status.HOVERED){

                g.setColor(Color.BLACK);

                String greeding = "Housing ID "+id+" (State: "+state+") "+"Price Movement Trend";
                int letterSize = (greeding.length()*2)/2;
                g.drawString(greeding, (int) greedingMsgPos.getX()-letterSize, (int) greedingMsgPos.getY());

                int x = (int) hoverInfoPos.getX();
                int y = (int) hoverInfoPos.getY();
                for(int i=0; i<12; i++){
                    String data = allmonths[i] +": $"+pricesValue.get(i)+".00";
                    g.drawString(data, x, y);

                    y+=20;
                }


            }

            if(currentStatus == Status.HIGHLIGHTED){

                String statement = " - State: "+state+", HousingID: "+id;

                g.setColor(color);
                g.fillRect((int) highlightInfoPos.getX(), (int) highlightInfoPos.getY(),20, 20);
                g.setColor(Color.BLACK);
                g.drawString(statement, (int) (highlightInfoPos.getX()+30), (int) (highlightInfoPos.getY()+15));

            }
            g.setColor(color);
            g.setStroke(new BasicStroke(4.0f));
            g.fillPolygon(xPointsForSquare, yPointsForSquare, 12);
            g.drawPolygon(xPointsForSquare, yPointsForSquare, 12);
        }
    }

    public double getDistanceFromPoint(int x, int y){
        double min = Double.MAX_VALUE;
        if(points != null){
            for(int i=1;i<points.size();i++){
                Point2D p1 = points.get(i-1);
                Point2D p2 = points.get(i);

                Line2D.Double segment = new Line2D.Double(p1,p2);
                double  distance = segment.ptSegDist(x,y);
                if(distance < min){
                    min = distance;
                }
            }
        }
        return min;
    }




    public void setID(int id){
        this.id = id;
    }
    public void setSize(int size){
        this.size = size;
    }

    public void setState(String state){
        this.state = state;
    }

    public void highlight(){
        currentStatus = currentStatus.HIGHLIGHTED;
    }
    public void normalize(){
        currentStatus = currentStatus.NORMAL;
    }

    public void invisiblize(){
        currentStatus = currentStatus.INVISIBLE;
    }
    public void hover(){
        currentStatus = currentStatus.HOVERED;
    }

    public void setPointForHightlight(double x, double y){
        highlightInfoPos = new Point2D.Double(x, y);
    }
    public void setPointForHover(double x, double y){
        hoverInfoPos = new Point2D.Double(x, y);
    }

    public void setGreedingMsgPos(double x, double y){
        greedingMsgPos = new Point2D.Double(x, y);
    }

    public GeneralPath getLine(){
        return line;
    }

}
