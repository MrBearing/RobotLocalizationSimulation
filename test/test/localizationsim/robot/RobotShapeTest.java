package test.localizationsim.robot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import localizationsim.robot.RobotPosition;
import localizationsim.robot.RobotShape;

public class RobotShapeTest extends JFrame {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public RobotShape robotShape;
  public RobotPosition pos;
  
  
  public RobotShapeTest(){
    super("test");
    this.pos = new RobotPosition(100, 100, 30);
    
  }
  
  
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.clearRect(0, 0, this.getWidth(), this.getHeight());
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    BasicStroke wideStroke = new BasicStroke(4.0f);
    g2.setStroke(wideStroke);
    
    robotShape = new RobotShape(pos);
    
    g2.setPaint(Color.blue);
    g2.fill(robotShape);
    g2.setPaint(Color.gray);
    g2.draw(robotShape);
  }

  public static void main(String[] args) throws InterruptedException {

    RobotShapeTest test = new RobotShapeTest();

    test.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    test.setBounds(0, 0, 300, 300);
    test.setVisible(true);
    
    while(true){
      double x = test.pos.getX() + 0.10d;
      test.pos.setX(x);
      
      
      test.repaint();
      Thread.sleep(100);
    }
    
  }

}
