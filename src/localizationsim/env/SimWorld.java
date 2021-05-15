package localizationsim.env;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import localizationsim.SimSystem;
import localizationsim.draw.CoordinatesGrid;
import localizationsim.draw.Drawable;
import localizationsim.robot.RobotPosition;

public class SimWorld implements Drawable {
  //private static final int GRID_VERNIER_INTERVAL = 100;
  final private double width;
  final private double height;

  
  final private Map<Integer, Landmark> landmarkMap;

  final private CoordinatesGrid grid;

  public SimWorld(double w, double h) {
    this.width = w;
    this.height = h;

    this.landmarkMap = Collections
        .synchronizedMap(new TreeMap<Integer, Landmark>());
    this.grid = new CoordinatesGrid(w, h);
  }
  
  public Landmark addLandmark(Landmark landmark) {
    landmark.setID(SimSystem.issueDetectableObjectID(landmark));
    return this.landmarkMap.put(landmark.getID(), landmark);
  }

  public Map<Integer, Landmark> getLandmarkList() {
    return this.landmarkMap;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return this.height;
  }

  public String toString() {
    String ret = "width:"+this.width+",height:"+this.height;

    return ret;
  }

  public boolean contain(RobotPosition pos) {
    return this.contain(pos.getX(), pos.getY());
  }

  public boolean contain(double x, double y) {
    double wid_half = this.getWidth() / 2.0D;
    double hei_half = this.getHeight() / 2.0D;
    return ((-wid_half <= x) && (x <= wid_half))
        && ((-hei_half <= y) && (y <= hei_half));
  }

  @Override
  public void draw(Graphics2D g2d) {
    this.grid.draw(g2d);

    for (Drawable d : this.getLandmarkList().values()) {
      d.draw(g2d);
    }

  }
}
