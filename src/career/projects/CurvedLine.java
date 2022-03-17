package career.projects;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CurvedLine {
    private GeneralPath line;
    private List<Point2D> points = new ArrayList<>();

    CurvedLine(){
        line = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    }


}
