package xml;

import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {
  public static void main(String[] args) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new File("./conf/list.xml"));
      Element root = doc.getDocumentElement();

      NodeList itemlist = root.getElementsByTagName("item");
      for (int i = 0; i < itemlist.getLength(); i++) {
        Node node = itemlist.item(i);
        NodeList children = node.getChildNodes();
        String name = null;
        String price = null;
        for (int j = 0; j < children.getLength(); j++) {
          Node child = children.item(j);
          if (child.getNodeName().equals("name")) {
            name = child.getFirstChild().getNodeValue().trim();
          }else if(child.getNodeName().equals("price")) {
            price = child.getFirstChild().getNodeValue().trim();
          }
        }
        System.out.println(name+"："+price+"円");
      }

    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
