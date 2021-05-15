package gui;

import localizationsim.gui.MCLParameterAdjustGUI;
import localizationsim.localization.MCLocalizationer;
import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;

public class MCLParameterGUITest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    IRobot test = new BasicRobot(new RobotPosition());
    
    MCLParameterAdjustGUI mclGui = new MCLParameterAdjustGUI((MCLocalizationer)test.getLocalizationer());
    mclGui.setVisible(true);
  }

}
