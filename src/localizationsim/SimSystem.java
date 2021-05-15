package localizationsim;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import localizationsim.env.Landmark;
import localizationsim.env.SimWorld;
import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.network.Network;
import localizationsim.util.DifferentFileTypeException;
import localizationsim.util.xml.RobotXMLReader;

public final class SimSystem {
  private static final String PROPERTIES_PATH = "./conf/exp1_2LM.properties";
  private static final Network network;
  private static final SimWorld simWorld;
  private static final Properties properties;
  private static final List<IRobot> robotList;
  private static final Map<Integer, Detectable> detectables;
  private static final File confFile;

  static {
    // シミュレーション条件読み込み外部ファイル化
    confFile = new File(PROPERTIES_PATH);
    properties = new Properties();

    loadProperties(getConfigFile());

    int width = Integer
        .valueOf(SimSystem.properties.getProperty("space_width"));
    int height = Integer.valueOf(SimSystem.properties
        .getProperty("space_height"));
    simWorld = new SimWorld(width, height);

    robotList = Collections.synchronizedList(new ArrayList<IRobot>());
    network = new Network("test");
    detectables = Collections
        .synchronizedMap(new TreeMap<Integer, Detectable>());

    loadMap(new File(SimSystem.getProperty("map_file")));

    loadRobots(new File(SimSystem.getProperty("robot_conf")));

  }

  /**
   * ロボットの読み込み
   * 
   * @param file
   */
  public static void loadRobots(File file) {
    List<IRobot> robots = null;

    try {
      robots = RobotXMLReader.createRobotList(file);

    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    SimSystem.addAllRobot(robots);
  }

  /**
   * マップファイルを読み込み環境を構築する
   * 
   * @param file
   */
  public static void loadMap(File file) {
    try {
      List<Landmark> marks;
      marks = Landmark.create(file);
      for (Landmark m : marks)
        simWorld.addLandmark(m);
    } catch (FileNotFoundException e) {
      String errMessage
        = "マップファイルが見つかりません。設定ファイルを確認してください";

      System.err.println(errMessage);
      JOptionPane.showMessageDialog(null, errMessage);
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (DifferentFileTypeException e) {
      System.err.println(e.getMessage());
      JOptionPane.showMessageDialog(null, e.getMessage());
      e.printStackTrace();
      System.exit(-1);
    }
  }

  /**
   * 
   */
  public SimSystem() {

  }

  public static SimWorld getSimWorld() {
    return simWorld;
  }

  public static Network getNetwork() {
    return network;
  }

  /**
   * シミュレーション対象のロボットを追加する
   * 
   * @param robot
   *          シミュレーション対象のロボット
   * @return
   */
  public static boolean addRobot(IRobot robot) {
    // robot.setSimspace(this);
    network.registerNode(robot.getNetworkNode());// IDの発行と登録
    int id = robot.getNetworkNode().getID();
    robot.getRobotPosition().setID(id);
    RobotPosition pos = robot.getRobotPosition();

    detectables.put(pos.getID(), pos);
    return robotList.add(robot);
  }

  /**
   * シュミレーション対象のロボットを追加する
   * 
   * @param robots
   *          シュミレーション対象のロボット
   */
  public static void addRobot(IRobot... robots) {
    for (IRobot r : robots) {
      addRobot(r);
    }
  }

  /**
   * シミュレーション対象のロボットを追加する
   * 
   * @param robots
   */
  public static void addAllRobot(Collection<IRobot> robots) {
    for (IRobot robo : robots)
      addRobot(robo);
  }

  /**
   * システム上で必要なプロパティをロードする
   */
  private static void loadProperties(File file) {

    try {
      properties.load(new BufferedInputStream(new FileInputStream(file)));
    } catch (FileNotFoundException e) {

      String errMessage = "プロパティファイルが見つかりません";
      System.err.println(errMessage);
      JOptionPane.showMessageDialog(null, errMessage);
      e.printStackTrace();
      System.exit(-1);
    } catch (IOException e) {
      String errMessage = "IOエラーが発生しました";
      System.err.println(errMessage);
      JOptionPane.showMessageDialog(null, errMessage);
      e.printStackTrace();
    }
  }

  /**
   * キーからプロパティを得る、キーに対応する値がない場合nullを返す
   * 
   * @param key
   *          対象プロパティのキー
   * @return プロパティの値 対象の値が存在しない場合null
   */
  public static String getProperty(String key) {
    return SimSystem.properties.getProperty(key);
  }

  public static Properties getProperties() {
    return SimSystem.properties;
  }

  /**
   * プロパティを設定する
   * 
   * @param key
   *          プロパティのキー
   * @param value
   *          キーに対して設定する値
   */
  public static void setProperty(String key, String value) {
    SimSystem.properties.setProperty(key, value);
    return;
  }

  /**
   * プロパティの保存を行う
   * 
   * @param file
   *          プロパティを保存するファイル
   * @throws IOException
   *           書き込みに失敗した場合にスローされる
   */
  public static void storeProperties(File file) throws IOException {
    PrintWriter pwriter;
    pwriter = new PrintWriter(new FileWriter(file));
    SimSystem.properties.store(pwriter, "");
  }

  /**
   * プロパティのリストを渡す。
   * 
   * @return プロパティのエントリーセット
   */
  public static Set<?> getPropertiesEntry() {
    return SimSystem.properties.entrySet();
  }

  /**
   * シュミレーションのプロパティが保存されていたファイルを返す
   * 
   * @return シミュレーションのプロパティファイル
   */
  public static File getConfigFile() {
    return confFile;
  }

  /**
   * シミュレーションを開始する
   */
  public static void startSimulation() {
    for (IRobot robot : SimSystem.robotList) {
      robot.start();
    }
  }

  /**
   * シミュレーションを一時停止する。
   */
  public static void stopSimulation() {
    for (IRobot robot : SimSystem.robotList) {
      robot.stop();
    }
  }

  /**
   * 登録されているロボットのリストを取得する
   * 
   * @return ロボットのリスト
   */
  public static List<IRobot> getRobotList() {
    return robotList;
  }

  /**
   * フィールド上のDetectableな物を全て返す
   * 
   * @return シミュレーション領域内に存在する探査可能なオブジェクトを返す
   */
  public static Map<Integer, Detectable> getDetectableObjects() {

    return SimSystem.detectables;
  }

  /**
   * Detectableに対してユニークなIDを発行する
   * 
   * @return IDを返す
   */
  public static Integer issueDetectableObjectID(Detectable detectable) {
    int ret = 0;
    while (true) {
      boolean flag = true;
      for (int i : SimSystem.getIDSet()) {
        if (i == ret) {
          flag = false;
          break;
        }
      }
      if (flag)
        break;
      ret++;
    }
    Integer ID = Integer.valueOf(ret);
    detectables.put(ID, detectable);

    return ID;
  }

  private static Set<Integer> getIDSet() {
    return SimSystem.getDetectableObjects().keySet();
  }

}
