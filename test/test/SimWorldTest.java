package test;

import static org.junit.Assert.*;
import localizationsim.env.SimWorld;
import localizationsim.robot.RobotPosition;

import org.junit.Before;
import org.junit.Test;

public class SimWorldTest {

  public SimWorld world;
  
  
  @Before
  public void setUp() throws Exception {
    this.world = new SimWorld(800, 800);
  }

  @Test
  public void testContainDoubleDouble() {
    System.out.println(this.world.contain(440,40));
    
    assertTrue(this.world.contain(100.0d,100.0d));

    
    assertFalse(this.world.contain(440, 401));
  }
  
  @Test
  public void testContainRobotPosition() {
    assertTrue(this.world.contain(new RobotPosition()));
    assertFalse(this.world.contain(new RobotPosition(900,900,900)));
  }



}
