package localizationsim.localization;

import java.awt.Graphics2D;

import localizationsim.Detectable;
import localizationsim.robot.IRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.sensor.ILandmarkSensor;
import localizationsim.robot.sensor.LandmarkSensorData;
import localizationsim.robot.sensor.OneLandmarkSensorData;
import Jama.Matrix;

public class EKFLocalizationer extends Localizationer{
  //private RobotPosition pos_past;

  //private static final double stepTime; // 単位(sec)

  private Matrix sigma;
  //private Map<Integer, Landmark> map;
  private double prabability;



  public EKFLocalizationer(IRobot robot){
    super(robot);
    sigma = new Matrix(new double[][] {
        { 0.00d, 0.0d, 0.0d },
        { 0.0d, 0.00d, 0.0d },
        { 0.0d, 0.0d, 0.00d } });
  }
  
  
  public Matrix getSigma() {
    return this.sigma;
  }

  public RobotPosition getMu() {
    return this.estimatePosition;
  }

  public double getProbability() {
    return this.prabability;

  }

  public void estimate(double velocity, double w, LandmarkSensorData z) {

    double[] alpha = { 0.000d, 0.0000d, 0.0000d, 0.000d };
    double theta = this.estimatePosition.getAngle();

    //************* Prediction step*************
    //動作の線形化のために、ヤコビヤンGtを計算
    Matrix Gt = new Matrix(new double[][] {
        {
            1.0d,
            0.0d,
            (velocity / w)
                * (-Math.cos(theta) + Math.cos(theta + w * STEP_RATE)) },
        {
            0.0d,
            1.0d,
            (velocity / w)
                * (-Math.sin(theta) + Math.sin(theta + w * STEP_RATE)) },
        { 0.0d, 0.0d, 1.0d } });
    //System.out.println("GT");    
    //Gt.print(3, 3);
    //Mt：制御空間における、ノイズの共分散行列
    Matrix Mt = new Matrix(
        new double[][] {
            { alpha[0] * Math.pow(velocity, 2) + alpha[1] * Math.pow(w, 2),
                0.0d },
            { 0.0d,
                alpha[2] * Math.pow(velocity, 2) + alpha[3] * Math.pow(w, 2) } });
    //System.out.println("mT");    
    //Mt.print(3, 3);
    
    //Vt:Mtを制御空間から状態空間へ写像するための行列
    Matrix Vt = new Matrix(
        new double[][] {
            {
                (-Math.sin(theta) + Math.sin(theta + w * STEP_RATE)) / w,

                (velocity * ((Math.sin(theta) - Math.sin(theta + w * STEP_RATE))))
                    / (Math.pow(w, 2))
                    + (velocity * (Math.cos(theta + w * STEP_RATE)) * STEP_RATE)
                    / w },
            {
                (Math.cos(theta) - Math.cos(theta + w * STEP_RATE)) / w,

                (velocity)
                    * ((Math.cos(theta) - Math.cos(theta + w * STEP_RATE)))
                    / (Math.pow(w, 2))
                    + (velocity * (Math.sin(theta + w * STEP_RATE)) * STEP_RATE)
                    / w }, { 0.0d, STEP_RATE } });
    //System.out.println("VT");    
    //Vt.print(3, 3);

    // 平均 muを更新
    Matrix mu_past = this.estimatePosition.toMatrix();
    Matrix up = new Matrix(new double[][] {
        { -(velocity / w) * Math.sin(theta) + (velocity / w)
            * Math.sin(theta + w * STEP_RATE) },
        { (velocity / w) * Math.cos(theta) - (velocity / w)
            * Math.cos(theta + w * STEP_RATE) }, { w * STEP_RATE } });

    RobotPosition mu_ = new RobotPosition(mu_past.plus(up));//

    // 共分散算出
    //sigma_ = (Gt sigma Gt~T) + (Vt Mt Vt) 
    Matrix sigma_ = (Gt.times(sigma).times(Gt.transpose()))
        .plus( (Vt.times(Mt)).times( Vt.transpose() ) );
    
    
    //System.out.println("VT!!");
    //Vt.times(Mt).times(Vt.transpose()).print(3,3);
    
    // ********************
   
    System.out.println("sigma");    
    sigma_.print(3, 3);

    
    // ++++++++Correction step+++++++

    Matrix Qt = new Matrix(new double[][] { 
        { 0.001d, 0.0d, 0.0d },
        { 0.0d, 0.001d, 0.0d },
        { 0.0d, 0.0d, 0.001d } });

    // System.out.println("z size"+z.getData().size());

    Matrix[] St = new Matrix[z.getData().size()];
    Matrix[] z_ = new Matrix[z.getData().size()];

    for (OneLandmarkSensorData zi : z.getData().values()) {
      // マップと予測位置から観測予測値 z_ を計算
      // System.out.println("id::"+zi.getID());
      Detectable mk = this.map.get(Integer.valueOf(zi.getID()));
      double dy = mk.getYAsMeter() - mu_.getYAsMeter();
      double dx = mk.getXAsMeter() - mu_.getXAsMeter();
      double q = Math.pow(dx, 2) + Math.pow(dy, 2);
      OneLandmarkSensorData obs_z = new OneLandmarkSensorData(
          zi.getID(), Math.sqrt(q), Math.atan2(dy, dx) - mu_.getAngle());

      z_[zi.getID()] = obs_z.toMatrix();

      // Ht算出
      Matrix Ht = new Matrix(new double[][] {
          { -dx / Math.sqrt(q), -dy / Math.sqrt(q), 0.0d },
          { dy / q, -dx / q, -1.0d },
          { 0.0d, 0.0d, 0.0d } });

      // St 算出 //St[i] = Ht* simgma_ * [Ht]~T +Qt
      St[zi.getID()] = Ht.times(sigma_).times(Ht.transpose()).plus(Qt);

      // Kt算出 // Kt = simgma_*[Ht]~T*St^-1
      Matrix Kt = sigma_.times(Ht.transpose()).times(St[zi.getID()].inverse());

      // mu_更新 // mu_ = mu_ +Kt*(zi - zi_);
      Matrix mu_mt = mu_.toMatrix();
      mu_mt = mu_mt.plus(Kt.times(zi.toMatrix().minus(z_[zi.getID()])));
      mu_.set(mu_mt);

      Matrix I = Matrix.identity(3, 3);
      // sigma_更新 //sigma_ = (I- Kt*Ht)*sigma_
      sigma_ = (I.minus(Kt.times(Ht)).times(sigma_));
      
    }
    // +++++++++++++++++++++++++++++++

    this.sigma = sigma_;

    this.estimatePosition = mu_.clone();

    // 確率算出
    this.prabability = 1.0;
    for (int i = 0; i < St.length; i++) {
      Matrix dz = z.getData().get(Integer.valueOf(i)).toMatrix().minus(z_[i]);// zi - zi_
      
      this.prabability *= Math.sqrt(St[i].times(Math.PI).det())
          * Math.exp((-0.50d)
              * dz.transpose().times(St[i].inverse()).times(dz).get(0, 0));

    }

  }

  @Override
  public void draw(Graphics2D g2d) {
    
  }

  @Override
  protected void update() {

    ILandmarkSensor ls = getTargetRobot().getLandmarkSensor();
    LandmarkSensorData z = (LandmarkSensorData) ls.getSensorData();

    double velocity = getTargetRobot().getCruiseController().getVelocity();
    double w = getTargetRobot().getCruiseController().getAngularVelocity();
    
    estimate(velocity, w, z);
    
  }



}
