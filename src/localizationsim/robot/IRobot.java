package localizationsim.robot;

import java.io.PrintStream;
import java.util.List;

import localizationsim.draw.Drawable;
import localizationsim.localization.Localizationer;
import localizationsim.robot.cruisecontorl.CruiseController;
import localizationsim.robot.network.NetworkNode;
import localizationsim.robot.sensor.ILandmarkSensor;

/**
 * ロボットに標準的に搭載されるコンポーネントへのアクセスと
 * 全般的なコントロールを提供するインターフェースです。
 * 
 * 
 * @author TOkamoto
 *
 */
public interface IRobot {

  /**
   * 現在のロボットの位置を返す。
   * 
   * @return {@link RobotPosition}
   */
  public abstract RobotPosition getRobotPosition();

  /**
   * このロボットが持つセンサーを取得する
   * 
   * @return {@link ILandmarkSensor}
   */
  public abstract ILandmarkSensor getLandmarkSensor();

  /**
   * このロボットの走行制御機を取得する。
   * 
   * @return {@link CruiseController}
   */
  public abstract CruiseController getCruiseController();

  /**
   * このロボットの通信用ノードの取得を行う
   * 
   * @return {@link NetworkNode}
   */
  public abstract NetworkNode getNetworkNode();

  /**
   * このロボットの自己位置推定器の取得を行う
   * 
   * @return {@link Localizationer}
   */
  public abstract Localizationer getLocalizationer();

  /**
   * ロボットが保持しているすべてのコンポーネントを起動する。
   */
  public abstract void start();

  /**
   * ロボットが保持しているすべてのコンポーネントを停止する。
   */
  public abstract void stop();
  
  /**
   * ロボットの描画可能要素を取得する
   * @return ロボットが保持している描画可能要素
   */
  public abstract List<Drawable> getDrawableList();
  /**
   * robotの出力用ストリームを取得する。
   * ログを出力したければstreamを持たせてこのメソッドを用いて取得する
   * 
   * @return ロボットのログ出力用のストリーム
   */
  public abstract PrintStream getOutputStream();
  
  /**
   * ロボットの文字列表現を取得する。
   * @return ロボットの文字列表現
   */
  public abstract String toString();
  
  public String getName();
  public void setName(String name);

}