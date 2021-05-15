package localizationsim.util.xml.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.util.xml.RobotXMLWriter;

public class RobotXMLMaker {

  public static List<IRobot> getCircleArray(int num, double radius,
      double velocity) {
    List<IRobot> list = new ArrayList<IRobot>();
    double angle = (2 * Math.PI) / num;
    
    for (int i = 0; i < num; i++) {
      double x = radius * Math.cos(angle * i);
      double y = radius * Math.sin(angle * i);
      double th = (Math.PI / 2.0d)+ (angle * i);

      RobotPosition pos = new RobotPosition();
      
      pos.setXAsMeter(x);
      pos.setYAsMeter(y);
      pos.setAngle(th);
      
      BasicRobot robot = new BasicRobot(pos);

      robot.getCruiseController().setVelocity(velocity);
      robot.getCruiseController().setAngularVelocity(velocity / radius);
      
      char name =(char)( i + ((int)'A') );
      
      robot.setName("" + name);
      
      list.add(robot);
    }
    return list;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

    List<IRobot> list = getCircleArray(1, 1.5, 3.0);

    try {
      RobotXMLWriter.writeAsXML(new File("./conf/1robots.xml"), list);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
