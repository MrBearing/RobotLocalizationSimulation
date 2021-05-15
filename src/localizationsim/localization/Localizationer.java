package localizationsim.localization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import localizationsim.Detectable;
import localizationsim.SimSystem;
import localizationsim.env.Landmark;
import localizationsim.localization.MCLocalizationer.InitType;
import localizationsim.robot.IRobot;
import localizationsim.robot.RobotCompornent;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.RobotShape;
import localizationsim.util.TimerExecutor;

/**
 * 自己位置推定機の基底クラスです。 自己位置推定のアルゴリズムを実装する場合はこのクラスを継承して作成してください。
 * 
 * @author TOkamoto
 * 
 */
abstract public class Localizationer implements RobotCompornent {
  private IRobot robot;
  /**
   * 推定位置を保持する変数
   */
  protected RobotPosition estimatePosition;
  /**
   * 1ステップあたりの時間(単位:ミリ秒)
   */
  final protected static double STEP_RATE = Double.valueOf(SimSystem
      .getProperty("step_rate")); // 単位(msec)
  /**
   * ローカルで保持しているマップ
   */
  protected Map<Integer, Detectable> map = null;
  private TimerExecutor timeExecutor;

  /**
   * 
   * @param robo この自己位置推定機を保持するロボット
   */
  public Localizationer(IRobot robo) {
    this.robot = robo;
    RobotPosition pos_ini = robot.getRobotPosition();
    this.estimatePosition = pos_ini.clone();// クローン取得

    map = Collections.synchronizedMap(new HashMap<Integer, Detectable>());

    Runnable routine = new Runnable() {
      @Override
      public void run() {
        update();
        outputLog();
      }
    };

    this.timeExecutor = new TimerExecutor(routine, STEP_RATE);
  }

  /**
   * 
   */
  private void outputLog() {
    if (getTargetRobot().getOutputStream() != null) {
      IRobot robot = getTargetRobot();
      RobotPosition estimate = getEstimatePosition();
      RobotPosition actual = robot.getCruiseController().getRobotPosition();
      
      RobotPosition dif = actual.subtracte(estimate);
      
      robot.getOutputStream().print("dif,"+ dif.toCSV());
      
      robot.getOutputStream().println(
          ",estimate," + estimate.toCSV() + ",actual," + actual.toCSV());
    }
  }
  
  
  /**
   * このコンポーネントを保持しているロボットを取得する
   * 
   * @return
   */
  public IRobot getTargetRobot() {
    return this.robot;
  }

  /**
   * 推定自己位置を取得する
   * 
   * @return
   */
  public RobotPosition getEstimatePosition() {
    return this.estimatePosition;
  }

  /**
   * 
   * @param d
   */
  public void putDetectableToLocalMap(Detectable d) {
    this.map.put(d.getID(), d);
  }

  @Override
  public void start() {
    Localizationer.copyMapFromSystem(map);
    this.timeExecutor.start();
  }

  @Override
  public void stop() {
    // this.timer.cancel();

    this.timeExecutor.stop();
  }

  /**
   * 自己位置推定の処理を記述するメソッド。 ユーザーはこのメソッドをオーバライドして自己位置推定のアルゴリズムを実装してください。
   */
  abstract protected void update();

  /**
   * 地図データのディープコピーを行うメソッド
   * 
   * @param srcMap
   *          コピー元のマップ
   * @param dstMap
   * @return
   */
  public static void deepCopyMap(Map<Integer, Landmark> srcMap,
      Map<Integer, Detectable> dstMap) {

    for (Landmark mk : srcMap.values()) {
      Integer id = mk.getID();
      dstMap.put(id, (Landmark) mk.clone());
    }
  }

  /**
   * グローバルマップをローカルにコピーする。
   * 
   * @param dstMap
   *          データをコピー
   */
  public static void copyMapFromSystem(Map<Integer, Detectable> dstMap) {
    Localizationer.deepCopyMap(SimSystem.getSimWorld().getLandmarkList(),
        dstMap);
  }
  
  @Override
  public void draw(Graphics2D g2d){
    RobotPosition avg_pos = this.getEstimatePosition();
    RobotShape shape = new RobotShape(avg_pos);

    BasicStroke stroke = new BasicStroke(4.0f);
    g2d.setStroke(stroke);
    g2d.setPaint(Color.BLACK);
    g2d.draw(shape);
    g2d.setPaint(Color.blue);
    g2d.fill(shape);
  }
  
  public static Localizationer create(IRobot robot , LocalizatinerType type){
    switch (type) {
    case MCL:
      return new MCLocalizationer(robot, InitType.UNKNOWN_POSITION);
    case DEADRECKONING:
      return new DeadReckoningLocalizationer(robot);
    case EKF:
      return new EKFLocalizationer(robot);
    default:
      return null;
    }
  }

}
