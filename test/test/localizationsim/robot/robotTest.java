package test.localizationsim.robot;

import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.cruisecontorl.CruiseController;

public class robotTest {
  public static void main(String[] args) {
    IRobot robot = new BasicRobot(new RobotPosition(0.0d, 0.0d, 0.0d));
    CruiseController cController = robot.getCruiseController();
    robot.start();
    
    cController.setVelocity(5.0d);
    cController.setAngularVelocity(0.0);

    //boolean flag = true;
    int i = 0;
    while (true) {
      
      if (10 == i) {// 10step = 1sec
        cController.setVelocity(0.0d);
        cController.setAngularVelocity(Math.PI/2);//1秒間　PI/2(rad/s)で回転
      }
      else if (19 == i) {
        cController.setVelocity(5.0d);//2秒間 5(m/s)で直進 = 1m =100cm=100px
        cController.setAngularVelocity(0.00d);
        System.out.println("");
      }
      else if (40 == i) {
        cController.setVelocity(0.0d);
        cController.setAngularVelocity(0.00d);
        break;
      }

//      System.out.println("step"+i+" :"+robot.getRobotPosition().toString());
      System.out.println(i+","+cController.getRobotPosition().toCSV());
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      i++;
    }
    System.exit(0);
  }
}
