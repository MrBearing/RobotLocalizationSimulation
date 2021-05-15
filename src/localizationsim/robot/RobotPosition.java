package localizationsim.robot;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.text.NumberFormat;

import localizationsim.Detectable;
import localizationsim.SimSystem;
import localizationsim.env.SimWorld;
import localizationsim.util.MT19937;
import Jama.Matrix;

public class RobotPosition implements Cloneable, Serializable, Detectable {
  public static MT19937 mt;
  static {
    mt = new MT19937();
  }
  
  
  /**
   * シリアルヴァージョンUI
   */
  private static final long serialVersionUID = 2686171433361309736L;

  /**
   * x座標 単位pixel = 1cm
   */
  private double x;
  /**
   * y座標 単位pixel = 1cm
   */
  private double y;
  /**
   * x軸から計測した際の角度 単位はラジアン
   */
  private double th;

  public RobotPosition() {
    this.x = 0.0;
    this.y = 0.0;
    this.th = 0.0;
  }

  public RobotPosition(Matrix mat) {
    this.set(mat);
  }

  public RobotPosition(double _x, double _y, double _th) {
    this.x = _x;
    this.y = _y;
    this.th = _th;
  }

  public void set(double x_, double y_, double th_) {
    this.setX(x_);
    this.setY(y_);
    this.setAngle(th_);
  }

  public void set(Matrix mat) {
    if ((mat.getColumnDimension() != 1) || (mat.getRowDimension() != 3)) {
      throw new IllegalArgumentException();
    }

    this.setXAsMeter(mat.get(0, 0));
    this.setYAsMeter(mat.get(1, 0));
    this.setAngle(mat.get(2, 0));
  }

  public double getX() {
    return x;
  }

  public double getXAsMeter() {
    return x / 100.0d;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setXAsMeter(double x_m) {
    this.x = x_m * 100.0d;
  }

  public double getY() {
    return y;
  }

  public double getYAsMeter() {
    return y / 100.0d;
  }

  public void setY(double y) {
    this.y = y;
  }

  public void setYAsMeter(double y_m) {
    this.y = y_m * 100.0d;
  }

  public double getAngle() {
    return th;
  }

  /**
   * 角度を設定する (th>PIの時は自動でリセット)
   * 
   * 
   * @param th_
   */
  public void setAngle(double th_) {
    this.th = th_ % (2 * Math.PI);
    // System.out.println("RobotPosition.setAngel# angle is "+th);

  }

  public double getAngleAsDegree() {
    return th * (180.0d / Math.PI);
  }

  public void setAngelAsDegree(double deg) {
    this.setAngle(deg * (Math.PI / 180.0d));
  }

  /**
   * ロボットの移動モデル
   * 
   * @param v
   *          速度 単位 m/s
   * @param w
   *          角速度 単位 rad/s
   * @param dt
   *          1ステップの時間 単位　msec
   */
  public void move(double vel, double w, double dt) {
    double v = vel * 100;

    double r = v / w;
    if (w == 0.0d) {
      r = 0.0d;
    }
    double x = this.getX() - r * Math.sin(this.getAngle()) + r
        * Math.sin(this.getAngle() + w * (dt / 1000.0d));
    this.setX(x);
    double y = this.getY() + r * Math.cos(this.getAngle()) - r
        * Math.cos(this.getAngle() + w * (dt / 1000.0d));

    this.setY(y);

    double angle = this.getAngle() + w * (dt / 1000.0d);

    this.setAngle(angle);
  }

  public Point2D getPoint2D() {
    return new Point2D.Double(this.getX(), this.getY());
  }

  @Override
  public String toString() {
    // String BR = System.getProperty("line.separator");
    NumberFormat format = NumberFormat.getInstance();
    format.setMinimumFractionDigits(3);
    format.setMaximumFractionDigits(3);
    return "RobotPos x:" + format.format(this.x) + "::"// + BR
        + "y:" + format.format(this.y) + "::"// + BR
        + "th:" + format.format(this.th);
  }

  /**
   * CSV形式で出力する際の形式で取得する
   * @return {@link String} カンマで区切られた値
   */
  public String toCSV() {
    NumberFormat format = NumberFormat.getInstance();
    format.setMinimumFractionDigits(3);
    format.setMaximumFractionDigits(3);
    return format.format(this.getXAsMeter()) + ","// + BR
        + format.format(this.getYAsMeter()) + ","// + BR
        + format.format(this.th);
  }

  @Override
  public RobotPosition clone() {
    RobotPosition ret = new RobotPosition(this.getX(), this.getY(),
        this.getAngle());
    return ret;
  }

  public Matrix toMatrix() {
    return new Matrix(new double[][] { { this.getXAsMeter() },
        { this.getYAsMeter() }, { this.getAngle() } });
  }

  /* *
   * public boolean equals(RobotPosition pos){ if(pos instanceof RobotPosition){
   * System.out.println("class different"); return false; }
   * 
   * return (this.getX() == pos.getX()) &&(this.getY() == pos.getY())
   * &&(this.getAngle() == pos.getAngle()); } 
   */

  private Integer id = null;

  public void setID(int id) {
    this.id = id;
  }

  @Override
  public Integer getID() {
    return this.id;
  }

  public double mesureDistanceFrom(Detectable detectable) {
    double dx = detectable.getX() - this.getX();
    double dy = detectable.getY() - this.getY();

    return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
  }

  public double mesureRelativeAngle(Detectable detectable) {
    double dx = detectable.getX() - this.getX();
    double dy = detectable.getY() - this.getY();

    return Math.atan2(dy, dx) - this.getAngle();
  }

  public RobotPosition subtracte(RobotPosition robotPosition){
    return RobotPosition.subtracte(this, robotPosition);
  }
  
  
  
  /**
   * 引き算を行うメソッド
   * @param robotpos1 
   * @param robotpos2
   * @return 差を返す（絶対値）
   */
  public static RobotPosition subtracte(RobotPosition robotpos1 , RobotPosition robotpos2){
    double dx = Math.abs( robotpos1.getX() - robotpos2.getX());
    double dy = Math.abs(robotpos1.getY() - robotpos2.getY() );
    double dth = Math.abs(robotpos1.getAngle() - robotpos2.getAngle() );
    
    return new RobotPosition(dx, dy ,dth);
  }
  
  
  /**
   * 指定されたRobotPositionを平均としたガウス分布を基にRobotPositionを生成する。
   * 
   * @param initRobotPos 基準となるロボットの位置
   * @return 生成されたRobotPositionを
   */
  public static RobotPosition createRandomRobotPosition(RobotPosition initRobotPos) {
    RobotPosition pos;
    // シミュレーション範囲のRobotPositionが生成できるまで回す
    while (true) {
      SimWorld world = SimSystem.getSimWorld();
      
      double w = world.getWidth() / 2.0D;
      double h = world.getHeight() / 2.0D;

      double x = initRobotPos.getX()
          + ((w / 2.0D) * (2.0D * mt.nextGaussian() - 1.0D) / 8.0D);
      double y = initRobotPos.getY()
          + ((h / 2.0D) * (2.0D * mt.nextGaussian() - 1.0D) / 8.0D);
      double th = initRobotPos.getAngle()
          + (2.0D * Math.PI * mt.nextGaussian() / 8.0D);

      if (world.contain(x,y)){
        pos = new RobotPosition(x, y, th);
        break;
      }
    }
    return pos;
  }

  /**
   * シミュレーション領域内に一様分布でRobotPositionを生成
   * @return {@link RobotPosition} ランダムなロボットの位置
   */
  public static RobotPosition createRandomRobotPosition() {
    RobotPosition pos;
    while (true) {// シミュレーション範囲のposが生成できるまで回す
      double w = SimSystem.getSimWorld().getWidth();
      double h = SimSystem.getSimWorld().getHeight();

      double x = (w / 2.0D) * (2.0D * mt.nextDouble() - 1.0D);// nextDouble
                                                              // で一様分布から得る
      double y = (h / 2.0D) * (2.0D * mt.nextDouble() - 1.0D);
      double th = 2.0D * Math.PI * mt.nextDouble();

      if (SimSystem.getSimWorld().contain(x,y)){
        pos = new RobotPosition(x, y, th);
        break;
      }

    }
    return pos;
  }

  
  
}