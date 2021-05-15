package localizationsim.robot;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import localizationsim.behavior.RobotBehavior;
import localizationsim.behavior.BasicBehavior;
import localizationsim.draw.Drawable;
import localizationsim.localization.Localizationer;
import localizationsim.localization.MCLocalizationer;
import localizationsim.localization.MCLocalizationer.InitType;
import localizationsim.robot.cruisecontorl.CruiseController;
import localizationsim.robot.network.NetworkNode;
import localizationsim.robot.sensor.ILandmarkSensor;
import localizationsim.robot.sensor.impl.LimitedLandmarkSenssor;

/**
 * 
 * シミュレーションにおける標準的なロボット
 * 
 * @note シュミレーション上では1px = 1cmとする。
 * 
 * @author TOkamoto
 * 
 */
public class BasicRobot implements IRobot {

  private String name = "";

  private List<RobotCompornent> componentList;

  private final CruiseController cruiseController;// 走行制御機
  private final NetworkNode node;// 通信機構
  private ILandmarkSensor landmarkSensor;// センサー
  private Localizationer localizationer;// 自己位置推定機
  private final RobotBehavior beahavior;
  /**
   * ロボットのログ出力用のストリーム
   */
  private PrintStream out = null;

  /**
   * コンストラクタ
   * 
   * @param _pos
   *          ロボットの初期位置
   */
  public BasicRobot(RobotPosition _pos) {

    this.componentList = Collections
        .synchronizedList(new ArrayList<RobotCompornent>());

    // 走行制御機構作成
    this.cruiseController = new CruiseController(_pos);
    this.componentList.add(cruiseController);

    // 自己位置推定機作成
    this.localizationer = new MCLocalizationer(this, InitType.KNOWN_POSITION);
    this.componentList.add(localizationer);

    // 通信用ノード
    this.node = new NetworkNode(this);
    this.componentList.add(node);

    // センサー作成
    // this.landmarkSensor = new DLandmarkSensor(this);
    this.landmarkSensor = new LimitedLandmarkSenssor(this);
    this.componentList.add(landmarkSensor);

    // ロボットの行動決定部分
    this.beahavior = new BasicBehavior(this);
    this.componentList.add(beahavior);

  }

  public BasicRobot(RobotPosition _pos, String _name) {
    this(_pos);
    this.setName(_name);

  }

  public void setOut(PrintStream out) {
    this.out = out;
    getLandmarkSensor();
    out.println("Measure error distance" + ILandmarkSensor.DISTANCE_ERROR
        + "angle" + ILandmarkSensor.ANGLE_ERROR);
  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#getRobotPosition()
   */
  @Override
  public final RobotPosition getRobotPosition() {
    return this.cruiseController.getRobotPosition();
  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#getCruiseController()
   */
  @Override
  public final CruiseController getCruiseController() {
    return this.cruiseController;
  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#getNetworkNode()
   */
  @Override
  public final NetworkNode getNetworkNode() {
    return node;
  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#getLocalizationer()
   */
  @Override
  public final Localizationer getLocalizationer() {
    return this.localizationer;
  }

  /**
   * Robotの自己位置推定器を設定する
   * 
   * @param localizatiner
   */
  public final void setLocalizationer(Localizationer localizatiner) {
    this.componentList.remove(this.localizationer);
    this.localizationer = localizatiner;
    this.componentList.add(this.localizationer);
  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#getLandmarkSensor()
   */
  @Override
  public final ILandmarkSensor getLandmarkSensor() {
    return this.landmarkSensor;
  }

  /**
   * Robotの環境計測用のセンサーを設定する
   * 
   * @param sensor
   */
  protected final void setLandMarkSensor(ILandmarkSensor sensor) {
    this.componentList.remove(this.getLandmarkSensor());
    this.landmarkSensor = sensor;
    this.componentList.add(this.landmarkSensor);
  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#start()
   */
  @Override
  public final void start() {
    for (RobotCompornent comp : this.componentList) {
      comp.start();
    }

  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#stop()
   */
  @Override
  public final void stop() {
    for (RobotCompornent comp : this.componentList) {
      comp.stop();
    }

  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#getDrawableList()
   */
  @Override
  public List<Drawable> getDrawableList() {
    List<Drawable> list = new ArrayList<Drawable>();
    for (RobotCompornent comp : this.componentList)
      list.add(comp);
    return list;
  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#getOutputStream()
   */
  @Override
  public PrintStream getOutputStream() {
    return out;
  }

  /*
   * (非 Javadoc)
   * 
   * @see localizationsim.robot.IRobot#toString()
   */
  @Override
  public String toString() {
    return "{{" + "ID : " + this.node.getID() + ",,"
        + this.getRobotPosition().toString() + "}}";
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

}
