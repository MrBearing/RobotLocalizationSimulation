package localizationsim.util.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import localizationsim.robot.IRobot;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class RobotXMLWriter {
  public static void writeAsXML(File file, Collection<IRobot> robots)
      throws ParserConfigurationException, TransformerException, IOException {

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    DocumentBuilder builder = dbFactory.newDocumentBuilder();
    Document doc = builder.newDocument();
    Element root = doc.createElement(RobotXMLIO.TAG_ROBOTS.toString());
    root.setAttribute(RobotXMLIO.ATTRIBUTE_NAME.toString(), file.getName());
    int i=0;
    for (IRobot robot : robots) {
      
      Element el = createRobotElement(robot, doc);
      root.appendChild(el);

    }
    doc.appendChild(root);
    
    DOMSource xmlSource = new DOMSource(doc);
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    StreamResult outputTarget = new StreamResult(fileOutputStream);
    TransformerFactory transFactory = TransformerFactory.newInstance();
    Transformer transformer = transFactory.newTransformer();
    transformer.transform(xmlSource, outputTarget);
    fileOutputStream.close();

  }
  
  public static Element createRobotElement(IRobot robot, Document doc) {
    Element robotElement = doc.createElement(RobotXMLIO.TAG_ROBOT.toString());
    robotElement.setAttribute(RobotXMLIO.ATTRIBUTE_X.toString(),
        String.valueOf(robot.getRobotPosition().getX()));
    robotElement.setAttribute(RobotXMLIO.ATTRIBUTE_Y.toString(),
        String.valueOf(robot.getRobotPosition().getY()));
    robotElement.setAttribute(RobotXMLIO.ATTRIBUTE_THETA.toString(),
        String.valueOf(robot.getRobotPosition().getAngle()));
    
    if(robot.getName() != null)
      robotElement.setAttribute(RobotXMLIO.ATTRIBUTE_NAME.toString(),robot.getName());
    
    
    
    double v = robot.getCruiseController().getVelocity();
    double w = robot.getCruiseController().getAngularVelocity();
    Element vel = doc.createElement(RobotXMLIO.TAG_VELOCITY.toString());
    vel.setAttribute(RobotXMLIO.ATTRIBUTE_SPEED.toString(), String.valueOf(v));
    vel.setAttribute(RobotXMLIO.ATTRIBUTE_ANGULAR.toString(), String.valueOf(w));
    
    robotElement.appendChild(vel);
    
    Element gui = doc.createElement(RobotXMLIO.TAG_GUI.toString());
    robotElement.appendChild(gui);
    
    Element result = doc.createElement(RobotXMLIO.TAG_RESULT.toString());
    result.setAttribute(RobotXMLIO.ATTRIBUTE_RECORDABLE.toString(), "true");
    robotElement.appendChild(result);
    
    
    return robotElement;
  }

}
