package localizationsim.localization;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import localizationsim.SimSystem;
import localizationsim.draw.Drawable;
import localizationsim.robot.RobotPosition;

class Particle implements Cloneable, Drawable {
// private static final double ROBOT_DIAMETER = 5.0;
  private static final double DIAMETER = Double.valueOf(SimSystem
      .getProperty("Particle.ROBOT_DIAMETER"));
  RobotPosition position;
  private double weight;

  public Particle(RobotPosition pos, double _weight) {
    this.setPossition(pos.clone());
    this.setWeight(_weight);
  }

  public RobotPosition getPosition() {
    return position;
  }

  public void setPossition(RobotPosition pos) {
    this.position = pos;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public String toString() {
    return this.position.toString() + "weight" + this.weight;
  }

  @Override
  protected Particle clone() {
    return new Particle(this.position.clone(), this.weight);
  }

  @Override
  public void draw(Graphics2D g2d) {
    Shape shape = null;
    // RobotShape shape = new RobotShape(position, ROBOT_DIAMETER);
    double x = this.position.getX() - (DIAMETER / 2.0d);
    double y = this.position.getY() - (DIAMETER / 2.0d);

    shape = new Ellipse2D.Double(x, y, DIAMETER, DIAMETER);
    double line_length = DIAMETER * 1.5;

    Point2D point = this.position.getPoint2D();
    double dx = point.getX() + line_length * Math.cos(this.position.getAngle());
    double dy = point.getY() + line_length * Math.sin(this.position.getAngle());
    Point2D.Double p_dst = new Point2D.Double(dx, dy);

    Line2D.Double direcLine = new Line2D.Double(point, p_dst);

    g2d.setPaint(Color.BLACK);
    g2d.draw(shape);
    g2d.draw(direcLine);
  }

  public static void nomalize(List<Particle> list) {
    double w_sum = getWeightSum(list);
    for (Particle pt : list) {
      pt.setWeight(pt.getWeight() / w_sum);
    }
  }

  public static double getWeightSum(List<Particle> list) {
    double ret = 0.0D;
    for (Particle pt : list) {
      ret += pt.getWeight();
    }
    return ret;
  }

  public static double getWeightAverage(List<Particle> list) {
    return getWeightSum(list) / (double) list.size();
  }

  public static RobotPosition getPositionAverage(List<Particle> list) {
    double x = 0.0;
    double y = 0.0;
    double th = 0.0;
  
    for (Particle pt : list) {
      RobotPosition pos = pt.getPosition();
      x += pos.getX();
      y += pos.getY();
      th += pos.getAngle();
    }
  
    x = x / list.size();
    y = y / list.size();
    th = th / list.size();
  
    return new RobotPosition(x, y, th);
  }
  
  
  
  /**
   * 
   * @param pList
   * @return
   */
  public static double getVariance(List<Particle> pList){
    
    return 0.0;
  }
  /**
   * 
   * @param pList
   * @return
   */
  public static double getMode(List<Particle> pList){
    for(Particle p : pList){
      
    }
    return 0.0;
  }
  

  public static List<Particle> cloneParticleList(List<Particle> list) {
    List<Particle> cl = Collections.synchronizedList(new ArrayList<Particle>());
  
    for (Particle pt : list) {
      cl.add(pt.clone());
    }
  
    return cl;
  }
}