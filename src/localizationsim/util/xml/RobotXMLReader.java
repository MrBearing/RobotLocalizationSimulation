package localizationsim.util.xml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import localizationsim.SimSystem;
import localizationsim.gui.RobotControllerGUI;
import localizationsim.localization.CreateException;
import localizationsim.localization.LocalizatinerType;
import localizationsim.localization.Localizationer;
import localizationsim.robot.IRobot;
import localizationsim.robot.BasicRobot;
import localizationsim.robot.RobotPosition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RobotXMLReader {

  public static List<IRobot> createRobotList(File file)
      throws ParserConfigurationException, SAXException, IOException, Exception {
    DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;

    builder = dbfactory.newDocumentBuilder();

    Document doc = builder.parse(file);

    return createRobotList(doc);
  }

  private static  String removeFileExtension(String filename) {
    int lastDotPos = filename.lastIndexOf('.');

    if (lastDotPos == -1) {
      return filename;
    } else if (lastDotPos == 0) {
      return filename;
    } else {
      return filename.substring(0, lastDotPos);
    }
  }
  
  
  public static List<IRobot> createRobotList(Document doc)
      throws FileNotFoundException {
    List<IRobot> robotList = new ArrayList<IRobot>();
    // rootElementの取得
    Element root = doc.getDocumentElement();

    SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mmssSS");
    String date = format.format(new Date());

    String expName = removeFileExtension(SimSystem.getConfigFile().getName());
    String robotsType = root.getAttribute(RobotXMLIO.ATTRIBUTE_NAME.toString());
    File foulder = new File("./result/" +expName+"&"+ robotsType+"&" + date + "/");
    foulder.mkdirs();

    NodeList robotNodeList = root.getElementsByTagName(RobotXMLIO.TAG_ROBOT
        .toString());

    for (int i = 0; i < robotNodeList.getLength(); i++) {
      Element element = (Element) robotNodeList.item(i);
      if (checkTagName(element, RobotXMLIO.TAG_ROBOT)) {
        IRobot robot = null;
        robot = createRobot(element, foulder);

        robotList.add(robot);
      }

    }

    return robotList;
  }

  private static IRobot createRobot(Element el, File resultfoulder)
      throws FileNotFoundException {

    // robot作成
    double x = Double.parseDouble(el.getAttribute(RobotXMLIO.ATTRIBUTE_X
        .toString()));
    double y = Double.parseDouble(el.getAttribute(RobotXMLIO.ATTRIBUTE_Y
        .toString()));
    double th = Double.parseDouble(el.getAttribute(RobotXMLIO.ATTRIBUTE_THETA
        .toString()));
    String name = el.getAttribute(RobotXMLIO.ATTRIBUTE_NAME.toString());

    RobotPosition pos = new RobotPosition(x, y, th);
    BasicRobot robot = new BasicRobot(pos);
    robot.setName(name);

    NodeList nodelist = el.getChildNodes();
    for (int i = 0; i < nodelist.getLength(); i++) {
      Node node = nodelist.item(i);
      if (node.getNodeType() == Node.TEXT_NODE
          && node.getNodeValue().trim().length() == 0) {
        continue;
      } else if (isElement(node)) {
        Element element = (Element) node;
        if (checkTagName(element, RobotXMLIO.TAG_VELOCITY)) {
          // 初期速度設定
          double speed = Double.parseDouble(element
              .getAttribute(RobotXMLIO.ATTRIBUTE_SPEED.toString()));
          double angular = Double.parseDouble(element
              .getAttribute(RobotXMLIO.ATTRIBUTE_ANGULAR.toString()));
          robot.getCruiseController().setAngularVelocity(angular);
          robot.getCruiseController().setVelocity(speed);

        } else if (checkTagName(element, RobotXMLIO.TAG_GUI)) {
          if (Boolean.parseBoolean(element
              .getAttribute(RobotXMLIO.ATTRIBUTE_VISIBLE.toString())))
            new RobotControllerGUI(robot).setVisible(true);

        } else if (checkTagName(element, RobotXMLIO.TAG_RESULT)) {
          // 出力ファイル設定
          if (Boolean.parseBoolean(element
              .getAttribute(RobotXMLIO.ATTRIBUTE_RECORDABLE.toString()))) {

            String fileName = robot.getName();
            FileOutputStream fOutStream = new FileOutputStream(new File(
                resultfoulder, fileName + ".csv"));

            PrintStream fOut = new PrintStream(new BufferedOutputStream(
                fOutStream));
            robot.setOut(fOut);

          }
        } else if (checkTagName(element, RobotXMLIO.TAG_LOCALIZATINER)) {
          String strType = element.getAttribute(RobotXMLIO.ATTRIBUTE_TYPE
              .toString());
          LocalizatinerType type;
          try {
            type = LocalizatinerType.create(strType);
            robot.setLocalizationer(Localizationer.create(robot, type));
          } catch (CreateException e) {
            e.printStackTrace();
            continue;
          }

        }

      }
    }

    return robot;
  }

  private static boolean isElement(Node node) {
    return node.getNodeType() == Node.ELEMENT_NODE;
  }

  private static boolean isTextNode(Node node) {
    return node.getNodeType() == Node.TEXT_NODE;
  }

  private static boolean checkTagName(Element el, RobotXMLIO tag) {
    return el.getTagName().equals(tag.toString());
  }
}
