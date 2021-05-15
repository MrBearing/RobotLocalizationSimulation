package test.localizationsim.robot;

import static org.junit.Assert.*;
import localizationsim.localization.EKFLocalizationer;
import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.sensor.impl.DLandmarkSensor;

import org.junit.Test;

public class RobotExtentionTest {

  public class TestRobot extends BasicRobot {

    public TestRobot(RobotPosition _pos) {
      super(_pos);
      this.setLocalizationer(new EKFLocalizationer(this));
      this.setLandMarkSensor(new DLandmarkSensor(this));
    }

  }

  @Test
  public void extentionTest() {
    // fail("まだ実装されていません");

    IRobot robot = new BasicRobot(new RobotPosition());

    IRobot test = new TestRobot(new RobotPosition());

    assertTrue(test.getLocalizationer() instanceof EKFLocalizationer);
    assertTrue(test.getLandmarkSensor() instanceof DLandmarkSensor);


    assertFalse(test.getLocalizationer().getClass() == robot
        .getLocalizationer().getClass());
    assertFalse(test.getLandmarkSensor().getClass() == robot
        .getLandmarkSensor().getClass());

  }

}
