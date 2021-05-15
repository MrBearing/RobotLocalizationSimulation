package landmarkSensorTest;

import localizationsim.env.Landmark;
import localizationsim.env.SimWorld;
import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.sensor.LandmarkSensorData;
import localizationsim.robot.sensor.OneLandmarkSensorData;

public class MesureTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    SimWorld simSpace = new SimWorld(800.0D, 800.0D);
    IRobot testRobot = new BasicRobot(new RobotPosition(0.0d, 0.0d, 0.0d));

    Landmark[] marks = { new Landmark(300, 300), new Landmark( 0, 300),
        new Landmark( -300, 300), new Landmark( -300, -300),
        new Landmark( 0, -300), new Landmark( 300, -300) };

    for (Landmark m : marks)
      simSpace.addLandmark(m);

    LandmarkSensorData data = (LandmarkSensorData) testRobot
        .getLandmarkSensor().getSensorData();

    for (OneLandmarkSensorData d : data.getData().values()) {
      int id = d.getID();

      System.out.println("id :" + id + d);

    }

  }

}
