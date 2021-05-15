package localizationsim.robot.sensor.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Map;

import localizationsim.Detectable;
import localizationsim.SimSystem;
import localizationsim.robot.IRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.sensor.ILandmarkSensor;
import localizationsim.robot.sensor.LandmarkSensorData;
import localizationsim.robot.sensor.OneLandmarkSensorData;
import localizationsim.util.MT19937;

/**
 * ランドマークに加え、ロボットも検出可能なセンサ Detectable Object Observe Sensor
 * 
 * @author TOkamoto
 * 
 */
public class DLandmarkSensor implements ILandmarkSensor {

  private IRobot robot;// このセンサを装備しているロボット
  private MT19937 mt;// メルセンヌ・ツイスタ乱数発生器

  public DLandmarkSensor(IRobot robot) {
    this.mt = new MT19937();
    this.robot = robot;
  }

  public OneLandmarkSensorData calcLandmarkSensorData(RobotPosition robotPos,
      Detectable detectable) {
    // ロボットの位置とランドマークの位置からセンサの観測値を算出

    double e_distance = ERROR_FLAG ? mt.nextGaussian() * DISTANCE_ERROR : 0.0;// 擬似的に発生させる誤差
    double distance = robotPos.mesureDistanceFrom(detectable) + e_distance;

    double e_angle = ERROR_FLAG ? mt.nextGaussian() * ANGLE_ERROR : 0.0;// 擬似的に発生させる誤差
    double angle = robotPos.mesureRelativeAngle(detectable) + e_angle;

    return new OneLandmarkSensorData(detectable.getID(), distance, angle);
  }

  @Override
  public LandmarkSensorData getSensorData() {
    Map<Integer, Detectable> DetectableMap = SimSystem.getDetectableObjects();

    RobotPosition robotPos = this.robot.getRobotPosition();

    LandmarkSensorData lSensorData = new LandmarkSensorData();

    for (Detectable detectable : DetectableMap.values()) {
      if (detectable.getID() == robotPos.getID())
        continue;

      OneLandmarkSensorData o_data = this.calcLandmarkSensorData(robotPos,
          detectable);

      lSensorData.addData(o_data);
      // lSensorData.addData(detectable.getID(), distance, angle);
    }

    return lSensorData;
  }

  @Override
  public void setRobot(IRobot robot) {
    this.robot = robot;
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  @Override
  public void draw(Graphics2D g2d) {
    System.out.println("##############");
    System.out.println(robot.getRobotPosition());
    for (OneLandmarkSensorData data : this.getSensorData().getData().values()) {

      double distance = data.getDistance();
      double angle = data.getRelativeAngle()
          + robot.getRobotPosition().getAngle();
      System.out.printf("ID:%d[angle %.3f ]%n", data.getID(), angle
          * (180.0d / Math.PI));

      double x = this.robot.getRobotPosition().getX() + distance
          * Math.cos(angle);

      double y = this.robot.getRobotPosition().getY() + distance
          * Math.sin(angle);

      Point2D.Double dstPoint = new Point2D.Double(x, y);

      Line2D.Double line = new Line2D.Double(this.robot.getRobotPosition()
          .getPoint2D(), dstPoint);
      g2d.setPaint(Color.RED);
      g2d.draw(line);

    }
  }

}
