package localizationsim.robot.sensor;

import localizationsim.SimSystem;
import localizationsim.robot.IRobot;
import localizationsim.robot.RobotCompornent;

public interface ILandmarkSensor extends RobotCompornent{

  public static final double DISTANCE_ERROR = Double.valueOf(SimSystem
      .getProperty("LandmarkSensor.DistanceError"));
  public static final double ANGLE_ERROR = Double.valueOf(SimSystem
      .getProperty("LandmarkSensor.AngleError"));
  public static final boolean ERROR_FLAG = Boolean.valueOf(SimSystem
      .getProperty("LandmarkSensor.ErrorFlag"));
  
  
  public LandmarkSensorData getSensorData();

  public void setRobot(IRobot robot);

}