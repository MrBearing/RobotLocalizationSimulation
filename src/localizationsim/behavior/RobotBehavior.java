package localizationsim.behavior;

import localizationsim.robot.IRobot;
import localizationsim.robot.RobotCompornent;

/**
 * ロボットの行動を規定する抽象クラス
 * @author TOkamoto
 *
 */
public abstract class RobotBehavior implements RobotCompornent{
  
  private IRobot targetRobot;
  
  public RobotBehavior(IRobot robot){
    this.targetRobot = robot;
  }
  
  public IRobot getTargetRobot() {
    return targetRobot;
  }

  public void setTargetRobot(IRobot targetRobot) {
    this.targetRobot = targetRobot;
  }
  
  abstract public void start();
  abstract public void stop();
  
  
}
