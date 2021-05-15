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

public class LimitedLandmarkSenssor implements ILandmarkSensor {

  public static final double SENSOR_BOUNDARY = Double.valueOf(SimSystem
      .getProperty("LimitedLandmarkSenssor.SensorBoundary"));

  private IRobot robot;// このセンサを装備しているロボット
  private MT19937 mt;// メルセンヌ・ツイスタ

  public LimitedLandmarkSenssor(IRobot robot) {
    this.mt = new MT19937();
    this.robot = robot;
  }

  public OneLandmarkSensorData calcLandmarkSensorData(RobotPosition robotPos,
      Detectable detectable) {
    // ロボットの位置とランドマークの位置からセンサの観測値を算出
    double dx = detectable.getX() - robotPos.getX();
    double dy = detectable.getY() - robotPos.getY();

    double e_distance = ERROR_FLAG ? mt.nextGaussian() * DISTANCE_ERROR : 0.0;// 擬似的に発生させる誤差
    double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) + e_distance;

    double e_angle = ERROR_FLAG ? mt.nextGaussian() * ANGLE_ERROR : 0.0;// 擬似的に発生させる誤差
    double angle = Math.atan2(dy, dx) - robotPos.getAngle() + e_angle;

    return new OneLandmarkSensorData(detectable.getID(), distance, angle);
  }

  public boolean isInSensorBoundary(OneLandmarkSensorData o_data) {

    return o_data.getDistance() <= SENSOR_BOUNDARY;
  }

  @Override
  public LandmarkSensorData getSensorData() {
    Map<Integer, Detectable> DetectableMap = SimSystem.getDetectableObjects();

    RobotPosition robotPos = this.robot.getRobotPosition();

    LandmarkSensorData lSensorData = new LandmarkSensorData();

    //System.out.println("#########################");
    
    for (Detectable detectable : DetectableMap.values()) {
      if (detectable.getID() == robotPos.getID())
        continue;// 自分だったらパス

      OneLandmarkSensorData o_data = this.calcLandmarkSensorData(robotPos,
          detectable);
/*
      System.out.printf("ID:%d[angle %.3f,distance %.3f ]%n", o_data.getID(),
          o_data.getRelativeAngleAsDegree(), o_data.getDistance());
*/
      if (! this.isInSensorBoundary(o_data) )
        continue;// 範囲外にあったら飛ばす

      lSensorData.addData(o_data);
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
    for (OneLandmarkSensorData data : this.getSensorData().getData().values()) {

      double distance = data.getDistance();
      double angle = data.getRelativeAngle()
          + robot.getRobotPosition().getAngle();

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
