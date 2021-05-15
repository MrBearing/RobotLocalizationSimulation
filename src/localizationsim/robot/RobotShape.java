package localizationsim.robot;

import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class RobotShape extends Area {
  private static final double POS_MARK_WIDTH = 60.0d;
  
  public static final double ROBOT_DIAMETER = 30.0d;
  private static final double PADDING = ROBOT_DIAMETER * 0.1d;
  private RobotPosition position;

  // RobotPositionクラスを用いてこのクラスの値を変更できるように。
  
  /**
   * 初期位置、角度を指定して形状を生成する。
   * 
   * @param x
   * @param y
   * @param angle
   */
  public RobotShape(double x, double y, double angle) {
    this(new RobotPosition(x, y, angle));
  }
  
   
   
  /**
   * 
   * @param pos
   */
  public RobotShape(RobotPosition pos) {
    super();
    this.position = pos;
    
    double x = this.position.getX() - (ROBOT_DIAMETER/2.0d);
    double y = this.position.getY() - (ROBOT_DIAMETER/2.0d);
    
    Ellipse2D ellipse = new Ellipse2D.Double(x,
        y, ROBOT_DIAMETER, ROBOT_DIAMETER);
    Area ellipseArea = new Area(ellipse);
    this.add(ellipseArea);

    Arc2D arc = new Arc2D.Double(
        x + PADDING,y + PADDING,
        ROBOT_DIAMETER - (PADDING * 2),ROBOT_DIAMETER - (PADDING * 2),
        this.position.getAngle() - (POS_MARK_WIDTH/2.0d) - this.position.getAngleAsDegree() ,
        POS_MARK_WIDTH, Arc2D.PIE);
    Area arcArea = new Area(arc);
    this.subtract(arcArea);
  }
  /**
   * 
   * @param pos
   * @param dia
   */
  public RobotShape(RobotPosition pos , double diameter) {
    super();
    this.position = pos;
    
    double x = this.position.getX() - (diameter/2.0d);
    double y = this.position.getY() - (diameter/2.0d);
    
    Ellipse2D ellipse = new Ellipse2D.Double(x,
        y, diameter, diameter);
    Area ellipseArea = new Area(ellipse);
    this.add(ellipseArea);

    Arc2D arc = new Arc2D.Double(
        x + PADDING,y + PADDING,
        diameter - (PADDING * 2),diameter - (PADDING * 2),
        this.position.getAngle() - (POS_MARK_WIDTH/2.0d) - this.position.getAngleAsDegree() ,
        POS_MARK_WIDTH, Arc2D.PIE);
    Area arcArea = new Area(arc);
    this.subtract(arcArea);
  }
  

}
