package localizationsim.behavior;

import java.awt.Graphics2D;
import java.util.List;

import localizationsim.SimSystem;
import localizationsim.robot.IRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.network.DataFrame;
import localizationsim.robot.network.NetworkNode;
import localizationsim.util.TimerExecutor;

@Deprecated
public class TestBhavior extends RobotBehavior {
  private TimerExecutor timerExecutor;
  
  
  public TestBhavior(IRobot robot) {
    super(robot);
    this.timerExecutor = new TimerExecutor(new Runnable() {
      
      @Override
      public void run() {
        //System.out.println("start");
        
        IRobot robot = getTargetRobot();
        
        NetworkNode node = robot.getNetworkNode();
        
        List<NetworkNode> nodeList = SimSystem.getNetwork().getNeighborNode(node);
        
        
        // 他のロボットの観測データ受信
        DataFrame dataFrame = robot.getNetworkNode().getReceiveDataFrame(nodeList.get(0).getID());
        if (dataFrame != null) {
          RobotPosition pos = dataFrame.getPosition();
          robot.getLocalizationer().putDetectableToLocalMap(pos);
        }
        
        
        //推定位置を送信
        RobotPosition estPos = robot.getLocalizationer().getEstimatePosition();
        //estPos.setID(node.getID());
        
        DataFrame frame = new DataFrame(node.getID(), nodeList.get(0).getID(), estPos);
        robot.getNetworkNode().send(frame);
        
        //System.out.println("end");
        
      }
    }, 10);
    
    
  }

  @Override
  public void start() {
    this.timerExecutor.start();
  }

  @Override
  public void stop() {
    this.timerExecutor.stop();

  }

  @Override
  public void draw(Graphics2D g2d) {
    //何もしない
    return;
  }

}
