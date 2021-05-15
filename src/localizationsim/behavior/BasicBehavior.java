package localizationsim.behavior;

import java.awt.Graphics2D;

import localizationsim.robot.IRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.network.DataFrame;
import localizationsim.robot.network.NetworkNode;
import localizationsim.util.TimerExecutor;

public class BasicBehavior extends RobotBehavior {
  private TimerExecutor timerExecutor;

  public BasicBehavior(IRobot robot) {
    super(robot);
    this.timerExecutor = new TimerExecutor(new Runnable() {

      @Override
      public void run() {

        IRobot robot = getTargetRobot();

        NetworkNode node = robot.getNetworkNode();

        // 他のロボットの観測データ受信
        for (DataFrame dataFrame : robot.getNetworkNode()
            .getReceiveDataFrames().values()) {
          RobotPosition pos = dataFrame.getPosition();
          robot.getLocalizationer().putDetectableToLocalMap(pos);
        }

        // 推定位置を送信
        RobotPosition estPos = robot.getLocalizationer().getEstimatePosition();
        estPos.setID(node.getID());

        DataFrame frame = new DataFrame(node.getID(), -1, estPos);//マルチキャスト
        robot.getNetworkNode().send(frame);


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
    // 何もしない
    return;
  }
}
