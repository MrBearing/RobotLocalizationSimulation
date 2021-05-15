package localizationsim.robot.sensor.impl;

import java.awt.Graphics2D;
import java.util.Map;

import localizationsim.SimSystem;
import localizationsim.env.Landmark;
import localizationsim.env.SimWorld;
import localizationsim.robot.IRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.sensor.ILandmarkSensor;
import localizationsim.robot.sensor.LandmarkSensorData;
import localizationsim.util.MT19937;

/**
 * 全てのランドマークに対しての距離と角度を検出するセンサ
 * 
 * @author TOkamoto
 * @deprecated このクラスは非推奨クラスです計測結果が正しく反映されません。DLandMarkSensorを使用してください
 * 
 */
@Deprecated
public class LandmarkSensor implements ILandmarkSensor {

  private IRobot robot;// このセンサを装備しているロボット
  private SimWorld simSpace;
  private MT19937 mt;

  public LandmarkSensor() {
    this.simSpace = SimSystem.getSimWorld();
    this.mt = new MT19937();
  }

  @Override
  public LandmarkSensorData getSensorData() {
    Map<Integer, Landmark> landmkList = this.simSpace.getLandmarkList();

    RobotPosition robotPos = this.robot.getRobotPosition();

    LandmarkSensorData lSensorData = new LandmarkSensorData();

    for (Landmark landmk : landmkList.values()) {
      // ロボットの位置とランドマークの位置からセンサの観測値を算出
      double dx = Math.abs(landmk.getX() - robotPos.getX());
      double dy = Math.abs(landmk.getY() - robotPos.getY());

      double e_distance = mt.nextGaussian() * DISTANCE_ERROR;// 擬似的に発生させる誤差
      double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2))
          + e_distance;

      double e_angle = mt.nextGaussian() * ANGLE_ERROR;// 擬似的に発生させる誤差
      double angle = Math.atan2(dy, dx) - robotPos.getAngle() + e_angle;

      lSensorData.addData(landmk.getID(), distance, angle);
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

  }

}
