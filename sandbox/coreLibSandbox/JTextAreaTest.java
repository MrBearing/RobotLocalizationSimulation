package coreLibSandbox;

import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import localizationsim.gui.JTextAreaStream;

public class JTextAreaTest {

  public static void main(String[] args) {
    JTextArea area = new JTextArea();
    area.setEditable(false); // ReadOnly に
    JTextAreaStream stream = new JTextAreaStream(area);
    
    PrintStream out = new PrintStream(stream, true);

    // System.setOut(new PrintStream(stream, true)); // true は AutoFlush の設定

    JFrame frame = new JFrame();
    frame.getContentPane().add(area);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setSize(50, 100);
    frame.setVisible(true);

    for (int i = 0; i < 5; i++){
      out.println("i[" + i +"]"+ area.getClass().getName());
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // System.out.println("あああ");
    // System.out.println("いいい");
    // System.out.println("ううう");
    
    out.close();
  }
}