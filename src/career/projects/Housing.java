package career.projects;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Housing {
    private int id;
    private int size;
    private String state;
    private GeneralPath line;
    private List<Point2D> points;
    private boolean isDraw;

    List<Integer> pricesValue;
    List<Double> xCordinates, yCordinates;
    int[] xPointsForSquare = new int[12];
    int[] yPointsForSquare = new int[12];

    public Housing(){
        pricesValue = new ArrayList<>();
        xCordinates = new ArrayList<>();
        yCordinates = new ArrayList<>(); // 변환된 길이값이 저장되어 있음...
        points = new ArrayList<>();
        line = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
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
//        if(currentState == State.HIGHLIGHTED){
//            g.setStroke(new BasicStroke(2.0f));
//            g.setColor(new Color(5, 195, 221));
//        }
//        if(currentState == State.INVISIBLE){
//            g.setColor(new Color(0,0,0, 20));
//        }
        g.draw(line);
    }

    public void drawSquare(Graphics2D g){
        if(isDraw){
            g.drawPolygon(xPointsForSquare, yPointsForSquare, 12);
        }
    }




    public void setID(int id){
        this.id = id;
    }

    public int getID(){
        return id;
    }

    public void setSize(int size){
        this.size = size;
    }

    public int getSize(){
        return size;
    }

    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return state;
    }
}
