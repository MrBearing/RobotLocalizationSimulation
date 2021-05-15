package localizationsim;

import localizationsim.gui.SimGUI;
import localizationsim.robot.IRobot;

public class Main {
	public static void main(String[] args) {

    SimGUI gui = new SimGUI();
    
    
    for(IRobot robo : SimSystem.getRobotList())
      gui.getDrawableDrawPanel().addDrawableObject(robo.getDrawableList());
    
    gui.setVisible(true);
	  
	 
	}
}
