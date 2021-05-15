package test.localizationsim.robot;

import static junit.framework.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import localizationsim.robot.RobotPosition;

import org.junit.Test;

public class RobotPositionTest {

  @Test
  public void testPutMap() {
    Map<Integer,RobotPosition> map = new TreeMap<Integer , RobotPosition>();
    
    int initx = 100 , inity = 100;
    
    RobotPosition p = new RobotPosition(initx,inity,100);
    RobotPosition p1 = p.clone();
    
    map.put(1, p);
    map.put(2, p1);
    
    System.out.println(map.entrySet());
    double x = 152.0,y = 6498;
    p.setX(x);
    p.setY(y);

    System.out.println(map.entrySet());
    
    assertEquals(x, p.getX());
    assertEquals(y,p.getY());
    
    
  }
}
