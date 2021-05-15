package landmarktest;

import localizationsim.SimSystem;
import localizationsim.env.Landmark;
import localizationsim.gui.SimGUI;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;

public class LandmarkMesureTest {

  public static void main(String[] args) {

    //SimWorld simSpace = new SimWorld(800, 800);

    BasicRobot testRobot = new BasicRobot(new RobotPosition(0.0d, -100.0d, 0.0d));
    Landmark mark = new Landmark(0.0d, 0.0d);
    
    
    SimSystem.getSimWorld().addLandmark(mark);
    SimSystem.addRobot(testRobot);
    //simSpace.addRobot(testRobot);
    //simSpace.addLandmark(mark);
    
    //ILandmarkSensor lsensor = new LandmarkSensor();

    //testRobot.setLandmarkSensor(lsensor);
    
    
    SimGUI gui = new SimGUI();
    gui.setVisible(true);
    
    double v = 1.00d;
    double w = 1.00d;

    
    testRobot.getCruiseController().setVelocity(v);
    testRobot.getCruiseController().setAngularVelocity(w);
    
    /*
    RobotControllerGUI rcGui = new RobotControllerGUI(testRobot);
    Point point = gui.getLocation();
    rcGui.setLocation((int)point.getX()+gui.getWidth(), (int)point.getY());
    rcGui.setVisible(true);
    */
  }
}
