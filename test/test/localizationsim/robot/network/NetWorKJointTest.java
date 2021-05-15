package test.localizationsim.robot.network;

import java.util.List;

import localizationsim.SimSystem;
import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.network.DataFrame;
import localizationsim.robot.network.NetworkNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NetWorKJointTest {

  @Before
  public void setUp() {
    for (int i = 0; i < 50; i++) {
      SimSystem.addRobot(new BasicRobot(new RobotPosition(i * 10, i * 10, i * 10)));
    }
  }

  @Test
  public void neighborTest() {
    for (IRobot robot : SimSystem.getRobotList()) {
      NetworkNode target_node = robot.getNetworkNode();
      List<NetworkNode> l = SimSystem.getNetwork().getNeighborNode(target_node);

      for (NetworkNode n : l) {
        if (n.getID() == target_node.getID()) {
          Assert.fail("Fail : Same id exist: n is" + n.getID() + "target "
              + target_node.getID());
        }
      }
    }
  }

  @Test
  public void networkIDTest() {

    for (IRobot robot1 : SimSystem.getRobotList()) {
      // System.out.println(robot1);
      Assert.assertTrue(robot1.getNetworkNode().getID() == robot1
          .getRobotPosition().getID());
    }
  }

  @Test
  public void networkAddressTest() {
    System.out.println("");

    for (int i = 0; i < SimSystem.getRobotList().size(); i++) {
      for (int j = i + 1; j < SimSystem.getRobotList().size(); j++) {
        IRobot robot1 = SimSystem.getRobotList().get(i);
        IRobot robot2 = SimSystem.getRobotList().get(j);
        // System.out.println(robot1);
        // System.out.println(robot2);

        Assert.assertFalse(robot1.getNetworkNode().getID() == robot2
            .getNetworkNode().getID());

      }
    }
  }

  @Test
  public void staticMessageSendingTest() {

    for (int i = 0; i < SimSystem.getRobotList().size(); i++) {
      for (int j = i + 1; j < SimSystem.getRobotList().size(); j++) {
        IRobot robot1 = SimSystem.getRobotList().get(i);
        IRobot robot2 = SimSystem.getRobotList().get(j);

        DataFrame df = new DataFrame(robot1.getNetworkNode().getID(), robot2
            .getNetworkNode().getID(), robot1.getRobotPosition());
        robot1.getNetworkNode().send(df);

        DataFrame recDf = robot2.getNetworkNode().getReceiveDataFrame(
            robot1.getNetworkNode().getID());

        RobotPosition actual = recDf.getPosition();
        RobotPosition expected = robot1.getRobotPosition();

        assertPositionEquals(actual, expected);

        // ############################
        DataFrame df2 = new DataFrame(robot2.getNetworkNode().getID(), robot1
            .getNetworkNode().getID(), robot2.getRobotPosition());
        robot2.getNetworkNode().send(df2);

        DataFrame recDf2 = robot1.getNetworkNode().getReceiveDataFrame(
            robot2.getNetworkNode().getID());

        RobotPosition actual2 = recDf2.getPosition();
        RobotPosition expected2 = robot2.getRobotPosition();

        assertPositionEquals(actual2, expected2);

      }
    }

  }

  public void assertPositionEquals(RobotPosition actual, RobotPosition expected) {
    Assert.assertEquals(expected.getX(), actual.getX(), 0.0d);
    Assert.assertEquals(expected.getY(), actual.getY(), 0.0d);
    Assert.assertEquals(expected.getAngle(), actual.getAngle(), 0.0d);
  }

  @Test
  public void multiMessageRecieveTest() {

  }

}
