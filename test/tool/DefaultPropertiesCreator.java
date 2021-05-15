package tool;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class DefaultPropertiesCreator {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Properties properties = new Properties();
    try {
      properties.load(new BufferedInputStream(new FileInputStream(new File(
          "./conf/simulation.properties"))));
      properties.setProperty("space_width", "800");
      properties.setProperty("space_height", "800");
      properties.setProperty("robot_number", "1");
      properties.setProperty("map_file", "./conf/test.simmap");
      
      properties.store(new BufferedWriter(new FileWriter("./conf/simulation.properties")),"");
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
