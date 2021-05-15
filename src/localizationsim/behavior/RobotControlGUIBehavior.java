package localizationsim.behavior;

import java.awt.Graphics2D;

import localizationsim.robot.IRobot;

/**
 * 
 * @author TOkamoto
 *
 */
public class RobotControlGUIBehavior extends RobotBehavior {

  public RobotControlGUIBehavior(IRobot robot) {
    super(robot);
  }

  public void stopMove() {
    this.getTargetRobot().getCruiseController().setAngularVelocity(0.0d);
    this.getTargetRobot().getCruiseController().setVelocity(0.0d);
  }

  public void speedUp(double velocity) {
    double v = this.getTargetRobot().getCruiseController().getVelocity();
    this.getTargetRobot().getCruiseController().setVelocity(velocity + v);
  }

  public void rotateSpeedUp(double angularVelocity) {
    double w = this.getTargetRobot().getCruiseController().getAngularVelocity();

    this.getTargetRobot().getCruiseController().setAngularVelocity(angularVelocity + w);
  }

  @Override
  public void start() {
    
  }

  @Override
  public void stop() {
    
  }

  @Override
  public void draw(Graphics2D g2d) {
    
  }
  
  
}
