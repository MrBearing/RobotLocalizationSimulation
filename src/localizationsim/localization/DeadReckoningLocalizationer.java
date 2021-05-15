package localizationsim.localization;

import localizationsim.robot.IRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.cruisecontorl.CruiseController;

public class DeadReckoningLocalizationer extends Localizationer {

  public DeadReckoningLocalizationer(IRobot robo) {
    super(robo);
  }


  @Override
  protected void update() {
    CruiseController cc = this.getTargetRobot().getCruiseController();
    RobotPosition pos = this.estimatePosition.clone();
    
    pos.move(cc.getVelocity(), cc.getAngularVelocity(), Localizationer.STEP_RATE);
    
    this.estimatePosition = pos;
  }

}
