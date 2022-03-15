package career.projects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Housing {
    private int id;
    private int size;
    private String state;
    List<Integer> pricesValue;
    private Point p;

    public Housing(){}
    public Housing(int id, int size, String state){
        this.id = id;
        this.size = size;
        this.state = state;

        pricesValue = new ArrayList<>();
        p = new Point();
    }

//    public void setPoint(){
//
//    }
//    public Point getPoint(){
//        return p;
//    }

}
