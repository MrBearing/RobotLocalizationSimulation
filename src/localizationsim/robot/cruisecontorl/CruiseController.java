package localizationsim.robot.cruisecontorl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import localizationsim.SimSystem;
import localizationsim.robot.RobotCompornent;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.RobotShape;
import localizationsim.util.MT19937;
import localizationsim.util.TimerExecutor;

/**
 * シミュレーション上でロボットの走行制御を行うクラス 誤差範囲に合わせて測度をランダムに変化させる。
 * 
 * @author TOkamoto
 * 
 */
public class CruiseController implements RobotCompornent{
  final static public double TIME_PER_STEP = Double.valueOf(SimSystem
      .getProperty("step_rate"));
  final static private double ERROR_RANGE = Double.valueOf(SimSystem
      .getProperty("CruiseController.error_range"));
  private RobotRoutine routine;
  private TimerExecutor tExecutor;
  
  /**
   * 単位:m/s
   */
  private double velocity;
  /**
   * 単位:rad/s
   */
  private double angularVelocity;
  final private RobotPosition pos;

  public CruiseController(RobotPosition init_pos) {
    this.routine = new RobotRoutine();
    this.setVelocity(0.0d);
    this.setAngularVelocity(0.0d);
    this.pos = init_pos;
    this.setTimeExecutor(new TimerExecutor(routine, TIME_PER_STEP));
  }

  public RobotPosition getRobotPosition() {
    return pos;
  }

  public double getVelocity() {
    return velocity;
  }

  public void setVelocity(double velocity) {
    this.velocity = velocity;
  }

  public double getAngularVelocity() {
    return angularVelocity;
  }

  public void setAngularVelocity(double angularVelocity) {
    this.angularVelocity = angularVelocity;
  }

  public TimerExecutor getTimeExecutor() {
    return tExecutor;
  }

  private void setTimeExecutor(TimerExecutor tExecutor) {
    this.tExecutor = tExecutor;
  }

  private class RobotRoutine implements Runnable {
    private MT19937 mt;// メルセンヌ・ツイスタによる乱数発生器

    public RobotRoutine() {
      mt = new MT19937();
    }

    @Override
    public void run() {
      // 状態遷移モデルの実装
      double e_v = mt.nextGaussian() * ERROR_RANGE;
      // double v = (velocity * 100) + e_v;
      double v = velocity + e_v;

      double e_w = mt.nextGaussian() * ERROR_RANGE;
      double w = angularVelocity + e_w;

      pos.move(v, w, TIME_PER_STEP);

    }

  }
  
  @Override
  public void draw(Graphics2D g2d) {
    BasicStroke stroke = new BasicStroke(4.0f);
    g2d.setStroke(stroke);
    RobotShape shape = this.getRobotShape();
    g2d.setPaint(Color.BLACK);
    g2d.draw(shape);
    g2d.setPaint(Color.pink);
    g2d.fill(shape);
  }
  
  /**
   * RobotShapeを返す
   * 
   * @return
   */
  public RobotShape getRobotShape() {
    return new RobotShape(getRobotPosition());
  }

  @Override
  public void start() {
    this.tExecutor.start();
    
  }

  @Override
  public void stop() {
    this.tExecutor.stop();
    
  }
  
  
}