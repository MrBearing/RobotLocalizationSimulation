package localizationsim.robot.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import localizationsim.SimSystem;

/**
 * 
 * @author TOkamoto
 * 
 * @note 負の数のアドレスは送信可能なノード全てに送信
 * 
 * TODO 独立したスレッドを持たせる
 * 
 */
public class Network {
  private String name;
  private Map<Integer, NetworkNode> nodeMap;// 登録されたノードのマップ

  public Network(String name) {
    this.name = name;
    nodeMap = Collections
        .synchronizedSortedMap(new TreeMap<Integer, NetworkNode>());
  }

  /**
   * ネットワークにノードを登録する
   * 
   * @param node
   */
  public void registerNode(NetworkNode node) {
    node.setNetwork(this);// 所属ネットワークをセット
    node.setID(this.issueNetworkID(node));// IDを発行
    nodeMap.put(node.getID(), node);// マップに追加
  }

  /**
   * ネットワークに登録されているノードのIDを取得する
   * 
   * @return {@link Set} IDが格納されたノード
   */
  public Set<Integer> getIDSet() {
    return nodeMap.keySet();
  }

  /**
   * DataFrameを該当するノードに送信する
   * 
   * @param dataFrame 送信対象のノード
   */
  protected final void putDataFrame(DataFrame dataFrame) {
    // dataFrameをdestに配送する

    if(dataFrame.isMulticast()){//マルチキャストの場合
      for(NetworkNode node:this.nodeMap.values())
        node.receive(dataFrame);
      
      return;
    }
    
    NetworkNode dst = this.nodeMap.get(dataFrame.getDestinationID());
    if (isSendable(dataFrame))
      dst.receive(dataFrame);

  }

  /**
   * ネットワーク中のネイバーを取得する
   * 
   * @param target_node 
   * @return ノードのリスト
   */
  public List<NetworkNode> getNeighborNode(NetworkNode target_node) {
    ArrayList<NetworkNode> ret = new ArrayList<NetworkNode>();

    for (NetworkNode node : this.nodeMap.values()) {
      if (node.getID() == target_node.getID())
        continue;
      
      ret.add(node);
    }

    return ret;
  }

  /**
   * そのNetworkNodeに対してユニークなIDを発行する
   * 
   * @return {@link Integer} ユニークなID
   */
  protected final Integer issueNetworkID(NetworkNode node) {

    Integer value = SimSystem.issueDetectableObjectID(node.getTargetRobot()
        .getRobotPosition());

    return value;
  }

  /**
   * 送信可能であるかを判断する
   * @param dataFrame
   * @return
   */
  private boolean isSendable(DataFrame dataFrame) {
    // XXX ただのモックなので実装の必要あり
    return true;
  }
  /**
   * ネットワークの名前を取得する
   * @return
   */
  public String getName() {
    return name;
  }

}
