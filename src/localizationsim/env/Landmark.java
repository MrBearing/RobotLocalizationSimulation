package localizationsim.env;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import localizationsim.Detectable;
import localizationsim.draw.Drawable;
import localizationsim.util.DifferentFileTypeException;

public class Landmark implements Drawable, Detectable, Cloneable {
  private Integer id = null;
  private double x;
  private double y;

  public Landmark(double x_, double y_) {
    // this.id = SimSystem.issueDetectableObjectID();
    this.x = x_;
    this.y = y_;
  }

  protected void setID(Integer id) {
    this.id = id;
  }

  public Integer getID() {
    return id;
  }

  public double getX() {
    return x;
  }

  public double getXAsMeter() {
    return this.x / 100.0d;
  }

  public double getY() {
    return y;
  }

  public double getYAsMeter() {
    return y / 100.0d;
  }

  @Override
  public void draw(Graphics2D g2d) {
    BasicStroke stroke = new BasicStroke(4.0f);
    g2d.setStroke(stroke);
    Rectangle2D.Double markShape = new Rectangle2D.Double(this.getX() - 25.0d,
        this.getY() - 25.0d, 50.0d, 50.0d);
    g2d.setPaint(Color.black);
    g2d.draw(markShape);
    g2d.setPaint(Color.gray);
    g2d.fill(markShape);
    g2d.setPaint(Color.white);
    g2d.drawString(String.valueOf(this.getID()), (int) this.getX(),
        (int) this.getY());
  }

  @Override
  public String toString() {

    return "[[LandmarkID:" + id + "x:" + this.x + ",y:" + this.y + "]]";

  }

  @Override
  public Object clone() {
    double cpX = this.x;
    double cpY = this.y;
    Integer cpId = this.id;
    Landmark ret = new Landmark(cpX, cpY);
    ret.setID(cpId);

    return ret;

  }

  /**
   * マップファイルを基にランドマークを作成する
   * 
   * @param file
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static List<Landmark> create(File file) throws FileNotFoundException,
      IOException, DifferentFileTypeException {
    if (!file.getPath().endsWith(".simmap")) {
      throw new DifferentFileTypeException();
    }
    List<Landmark> marks = new ArrayList<Landmark>();

    BufferedReader reader = new BufferedReader(new FileReader(file));

    while (true) {
      String line = reader.readLine();
      if (line == null)
        break;// 行末まで来たらブレイク

      String[] value = line.split(",");

      marks
          .add(new Landmark(Double.valueOf(value[0]), Double.valueOf(value[1])));

    }
    reader.close();

    return marks;
  }

}
