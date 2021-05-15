package localizationsim.robot.network;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import localizationsim.robot.IRobot;
import localizationsim.robot.RobotCompornent;

public class NetworkNode implements RobotCompornent {

  // private NetworkNodeID nodeID = null;
  private Integer nodeID = null;
  private Network network = null;

  //private DataFrame receiveDataFrame = null;

  // private List<DataFrame> receiveDataFrameBuffer;
  private Map<Integer, DataFrame> receiveDataFrameMap;

  private IRobot robot;

  public NetworkNode(IRobot robot) {
    this.robot = robot;
    this.nodeID = null;
    this.network = null;
    //this.receiveDataFrame = null;

    this.receiveDataFrameMap = Collections
        .synchronizedMap(new HashMap<Integer, DataFrame>());

  }

  /**
   * 受信したDataFrameをバッファから取り出す。
   * 
   * @param id 対象のロボットのネットワークID
   * @return 対象のロボットが送信してきた最新のデータフレーム 
   */
  public DataFrame getReceiveDataFrame(int id) {
    return this.receiveDataFrameMap.get(id);
  }
  
  /**
   * 各ノードについて受信したデータが格納されたマップを返す
   *
   * @return
   */
  public Map<Integer, DataFrame> getReceiveDataFrames(){
    return this.receiveDataFrameMap;
  }

  
  /**
   * DataFrameを送信します。
   * 
   * @param dataFrame
   */
  public final void send(DataFrame dataFrame) {
    dataFrame.getPosition().setID(this.nodeID);

    this.network.putDataFrame(dataFrame);
  }

  /**
   * 
   * @return
   */
  protected IRobot getTargetRobot() {
    return robot;
  }

  /**
   * NetworkクラスはこのメソッドをコールしDataFrameを NetworkNodeクラスに渡します。
   * 
   * @param dataFrame
   */
  protected final void receive(DataFrame dataFrame) {
    this.receiveDataFrameMap.put(dataFrame.getDestinationID(), dataFrame);
    //this.receiveDataFrame = dataFrame;
  }

  /**
   * このノードが使用するネットワークを設定する。 Networkクラスからコールされるメソッドです。
   * 
   * @param net
   */
  protected final void setNetwork(Network net) {
    this.network = net;
  }

  public Integer getID() {
    return nodeID;
  }

  protected void setID(Integer id) {
    this.nodeID = id;
  }

  @Override
  public String toString() {
    String BR = System.getProperty("line.separator");
    return "ID:" + ((this.nodeID == null) ? "null" : "" + this.nodeID) + BR
        + "network:"
        + ((this.network == null) ? "no network" : "" + this.network.getName())
        + BR;
  }

  @Override
  public void draw(Graphics2D g2d) {
    // 何もしない
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

}
