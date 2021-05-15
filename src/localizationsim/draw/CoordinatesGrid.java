package localizationsim.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import localizationsim.SimSystem;

public class CoordinatesGrid implements Drawable {
  private double width;
  private double height;
  private double GRID_VERNIER_INTERVAL;

  public CoordinatesGrid(double width, double height, double interbal) {
    this.setWidth(width);
    this.setHeight(height);
    this.GRID_VERNIER_INTERVAL = interbal;

  }

  public CoordinatesGrid(double width, double height) {
    this.setWidth(width);
    this.setHeight(height);
    this.GRID_VERNIER_INTERVAL = Double.valueOf(SimSystem
        .getProperty("CoordinatesGrid.GRID_VERNIER_INTERVAL"));

  }

  @Override
  public void draw(Graphics2D g2d) {
    // 座標軸 補助目盛り描画
    // x軸補助目盛描画
    int num_x = (int) ((getWidth() / 2.0d) / GRID_VERNIER_INTERVAL) + 1;
    for (int i = 0; i < num_x; i++) {
      Line2D.Double pos = new Line2D.Double(-getWidth() / 2.0d, i
          * GRID_VERNIER_INTERVAL, getWidth() / 2.0d, i * GRID_VERNIER_INTERVAL);
      Line2D.Double nega = new Line2D.Double(-getWidth() / 2.0d, -i
          * GRID_VERNIER_INTERVAL, getWidth() / 2.0d, -i
          * GRID_VERNIER_INTERVAL);
      g2d.setPaint(new Color(182, 182, 182));
      g2d.draw(pos);
      g2d.draw(nega);
    }

    // y軸補助目盛描画
    int num_y = (int) ((getHeight() / 2.0d) / GRID_VERNIER_INTERVAL) + 1;
    for (int i = 0; i < num_y; i++) {
      Line2D.Double pos = new Line2D.Double(i * GRID_VERNIER_INTERVAL,
          -getHeight() / 2.0d, i * GRID_VERNIER_INTERVAL, getHeight() / 2.0d);
      Line2D.Double nega = new Line2D.Double(-i * GRID_VERNIER_INTERVAL,
          -getHeight() / 2.0d, -i * GRID_VERNIER_INTERVAL, getHeight() / 2.0d);
      g2d.setPaint(new Color(182, 182, 182));
      g2d.draw(pos);
      g2d.draw(nega);
    }

    Stroke axis_stroke = new BasicStroke(1.8f);
    g2d.setStroke(axis_stroke);
    // x軸描画
    Line2D x_axis = new Line2D.Double(-getWidth() / 2.0d, 0, getWidth() / 2.0d,
        0);
    g2d.setPaint(Color.BLACK);
    g2d.draw(x_axis);
    // y軸描画
    Line2D y_axis = new Line2D.Double(0, -getHeight() / 2.0d, 0,
        getHeight() / 2.0d);
    g2d.setPaint(Color.BLACK);
    g2d.draw(y_axis);

  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getHeight() {
    return height;
  }

  public void setHeight(double height) {
    this.height = height;
  }

}
