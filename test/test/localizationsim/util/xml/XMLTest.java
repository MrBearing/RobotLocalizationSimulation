package test.localizationsim.util.xml;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import localizationsim.SimSystem;
import localizationsim.gui.SimGUI;
import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.util.xml.RobotXMLReader;
import localizationsim.util.xml.RobotXMLWriter;

import org.junit.Test;
import org.xml.sax.SAXException;

public class XMLTest {

  private static final File FILE = new File("./conf/test.xml");

  @Test
  public void writeTest() {
    List<IRobot> list = new ArrayList<IRobot>();
    for (int i = 0; i < 5; i++) {
      BasicRobot robot = new BasicRobot(new RobotPosition());
      list.add(robot);
    }

    try {
      RobotXMLWriter.writeAsXML(new File("./conf/test3.xml"), list);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void readTest() throws ParserConfigurationException, SAXException,
      IOException, Exception {

    List<IRobot> robots = null;

    robots = RobotXMLReader.createRobotList(FILE);

    assertTrue(robots.size() > 0);
    
    robots.get(0);
    
    System.out.println(robots.get(0).toString());
    
    
    
    SimSystem.addAllRobot(robots);
    SimGUI gui = new SimGUI();

    for (IRobot robo : SimSystem.getRobotList())
      gui.getDrawableDrawPanel().addDrawableObject(robo.getDrawableList());

    gui.setVisible(true);

  }

}
