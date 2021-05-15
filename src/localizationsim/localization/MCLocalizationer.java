package localizationsim.localization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import localizationsim.Detectable;
import localizationsim.SimSystem;
import localizationsim.env.Landmark;
import localizationsim.env.SimWorld;
import localizationsim.robot.IRobot;
import localizationsim.robot.RobotPosition;
import localizationsim.robot.RobotShape;
import localizationsim.robot.sensor.ILandmarkSensor;
import localizationsim.robot.sensor.LandmarkSensorData;
import localizationsim.robot.sensor.OneLandmarkSensorData;
import localizationsim.util.MT19937;

/**
 * モンテカルロ自己位置推定を使用した自己位置推定機です。
 * 
 * 
 * @author TOkamoto
 * 
 */
public class MCLocalizationer extends Localizationer {

  private static final String KEY_HEIGHT = "space_height";
  private static final String KEY_WIDTH = "space_width";
  private static final String KEY_PARTICLE_NUMBER = "MCLocalizationer.particleNumber";
  private static final String KEY_ALPHA_SLOW = "MCLocalizationer.alphaSlow";
  private static final String KEY_ALPHA_FAST = "MCLocalizationer.alphaFast";

  /**
   * 初期化時の状態を指定するためのEnum
   * 
   * @author TOkamoto
   * 
   */
  public static enum InitType {
    /**
     * 初期位置が既知の場合
     */
    KNOWN_POSITION,
    /**
     * 初期位置が未知の場合
     */
    UNKNOWN_POSITION
  };

  /**
   * 探査領域の幅
   */
  private static final double width = Double.valueOf(SimSystem
      .getProperty(KEY_WIDTH));
  /**
   * 探査領域の高さ
   */
  private static final double h = Double.valueOf(SimSystem
      .getProperty(KEY_HEIGHT));

  /**
   * この値の3シグマが2PIになるように;
   */
  public static final double ANGELE_VARIANCE = 2.0D * Math.PI / 3.0D;

  /**
   * 距離に関する分散はheightとwidthの3シグマ
   */
  public static final double DISTANCE_VARIACE = ((width + h) / 2.0D) / 3.0D
      * Math.sqrt(2.0);

  /**
   * パーティクル数
   */
  private static final int PARTICLE_NUMBER = Integer.valueOf(SimSystem
      .getProperty(KEY_PARTICLE_NUMBER));

  private double alphaFast; // = 1.0;// 0.3D;
  private double alphaSlow;// = 1.0;// 0.5D;

  private Double w_fast = null;
  private Double w_slow = null;
  private static MT19937 mt = new MT19937();
  private List<Particle> particleList;

  /**
   * 唯一のコンストラクタ
   * 
   * @param robot
   *          このコンポーネントを保持しているロボット
   * @param type
   *          ロボットの初期位置の既知・未知を指定する
   */
  public MCLocalizationer(IRobot robot, InitType type) {
    super(robot);
    RobotPosition initRobotPos = robot.getRobotPosition();

    this.alphaFast = Double.valueOf(SimSystem.getProperty(KEY_ALPHA_FAST));
    this.alphaSlow = Double.valueOf(SimSystem.getProperty(KEY_ALPHA_SLOW));

    // 乱数発生機 初期化
    mt = new MT19937();
    switch (type) {
    case KNOWN_POSITION:// 初期位置が既知の場合
      this.particleList = createGaussianParticle(initRobotPos);
      break;
    case UNKNOWN_POSITION:// 初期位置不明な場合
      this.particleList = createUniformParticles();
      break;
    default:
      break;
    }

  }

  /**
   * パーティクルのリストを取得する
   * 
   * @return
   */
  public List<Particle> getParticleList() {
    return this.particleList;
  }

  /**
   * パーティクルのリストのクローンを作成する。
   * 
   * @return
   */
  public List<Particle> cloneParticleList() {
    List<Particle> cl = Collections.synchronizedList(new ArrayList<Particle>());

    for (Particle pt : this.particleList) {
      cl.add(pt.clone());
    }

    return cl;
  }

  @Override
  public RobotPosition getEstimatePosition() {
    return Particle.getPositionAverage(particleList);
  }

  @Override
  public void draw(Graphics2D g2d) {
    super.draw(g2d);
    /*
    RobotPosition avg_pos = this.getEstimatePosition();
    RobotShape shape = new RobotShape(avg_pos);

    Stroke defStroke = g2d.getStroke();
    BasicStroke stroke = new BasicStroke(4.0f);
    g2d.setStroke(stroke);
    g2d.setPaint(Color.BLACK);
    g2d.draw(shape);
    g2d.setPaint(Color.blue);
    g2d.fill(shape);

    g2d.setStroke(defStroke);
    */
    
    // TODO 描画もとに戻す
    /*
    for (Particle pt : this.particleList) {
      pt.draw(g2d);
    }
    */
  }

  @Override
  protected void update() {
    ILandmarkSensor ls = getTargetRobot().getLandmarkSensor();
    LandmarkSensorData z = (LandmarkSensorData) ls.getSensorData();

    double velocity = getTargetRobot().getCruiseController().getVelocity();
    double angularVelocity = getTargetRobot().getCruiseController()
        .getAngularVelocity();

    List<Particle> tempParticleList_ = Collections
        .synchronizedList(new ArrayList<Particle>());
    List<Particle> updateParticleList = Collections
        .synchronizedList(new ArrayList<Particle>());

    // 初回起動時 荷重移動平均初期化
    if ((w_fast == null) || (w_slow == null)) {
      w_fast = 1.0d;
      w_slow = 1.0d;
    }

//    System.out.println("############# ID :"
//        + this.getTargetRobot().getNetworkNode().getID() + "#################");

    for (Particle pt_old : this.getParticleList()) {
      // 一つ前のステップのパーティクルのクローンを作成
      Particle pt = pt_old.clone();
      // パーティクルを動作モデルにしたがって移動
      pt.getPosition()
          .move(velocity, angularVelocity, Localizationer.STEP_RATE);

      // パーティクルの重み計算
      double weight = calcWeight(z, pt);

      pt.setWeight(weight);
      // 追加
      tempParticleList_.add(pt);
    }

    // 平均計算
    double w_avg = Particle.getWeightAverage(tempParticleList_);

    Particle.nomalize(tempParticleList_);// 各パーティクルのウェイトを正規化

    // 加重移動平均計算
    w_slow = w_slow + getAlphaSlow() * (w_avg - w_slow);
    w_fast = w_fast + getAlphaFast() * (w_avg - w_fast);

    /* ランダムパーティクル注入量決定 */
    double rand_part_rate = 1.0d - (w_fast / w_slow);
    int num_rand_part = 0;

    int particleListSize = this.getParticleList().size();

//    System.out.println("w_avg is " + w_avg);
//    System.out.printf("a_slow:%.3f", getAlphaSlow());
//    System.out.println("w_slow is " + w_slow);
//    System.out.printf("a_fast:%.3f", getAlphaFast());
//    System.out.println("w_fast is " + w_fast);
//    System.out.println("rand_ part_rate : "
//        + ((0.0D < rand_part_rate) ? rand_part_rate : "nega"));

    if (0.0D < rand_part_rate) {
      num_rand_part = (int) (rand_part_rate * particleListSize);

      // System.out.println("num" + num_rand_part);

      for (int i = 0; i < num_rand_part; i++) {

        // NFIXME 初期ゆう度調整 →正規化されるため問題なし
        updateParticleList.add(createRandomParticle((double) 1.0D
            / (double) particleListSize));
      }
    }

//    System.out.println("Number of Random particle is " + num_rand_part);
//    System.out.println("resumple num is " + (particleListSize - num_rand_part));

    // 再サンプリング
    List<Particle> list = MCLocalizationer.lowVarianceResampling(
        tempParticleList_, particleListSize - num_rand_part);

    updateParticleList.addAll(list);

    this.particleList = updateParticleList;

  }

  /**
   * センサデータから対象パーティクルの重みを計算する
   * 
   * @param z
   *          センサデータ
   * @param pt
   *          　パーティクル
   * @return
   */
  public double calcWeight(LandmarkSensorData z, Particle pt) {
    // 重み計算　(ゆう度計算)
    // 測定距離と相対角度をガウス関数で重み付けする
    // weight 初期値　検討

    double weight = 1.0d;
    for (OneLandmarkSensorData zi : z.getData().values()) {
      int id = zi.getID();

      // マップ内に当該のものがあるか
      Detectable detectable = this.map.get(Integer.valueOf(id));

      if (detectable == null) {
        continue; // nullの場合にはループを飛ばす
      }
      // 一つの計測対象について尤度を計算する。
      // double lh = calcCooprativeLikelihoodByAngle(zi, pt.getPosition(),
      // detectable);
      // lh *= calcCooprativeLikelihoodByDistance(zi, pt.getPosition(),
      // detectable);
      double lh = calcCooprativeLikelihoodByDistance(zi, pt.getPosition(),
          detectable);
      // double lh = calcLikelihoodByDistance(zi, pt.position, mk);
      weight *= lh;
    }
    return weight;
  }

  public void storeParameter() throws IOException {
    SimSystem.setProperty(KEY_ALPHA_FAST, Double.toString(alphaFast));
    SimSystem.setProperty(KEY_ALPHA_SLOW, Double.toString(alphaSlow));
    SimSystem.storeProperties(SimSystem.getConfigFile());
    // System.out.println("store param");
  }

  public double getAlphaFast() {
    return alphaFast;
  }

  public double getAlphaSlow() {
    return alphaSlow;
  }

  public void setAlphaSlow(double alphaSlow) {
    this.alphaSlow = alphaSlow;
  }

  public void setAlphaFast(double alphaFast) {
    this.alphaFast = alphaFast;
  }

  /**
   * 初期位置を基にガウス分布にしたがってパーティクル生成
   * 
   * @param initRobotPos
   *          ロボットの初期位置
   * @return パーティクルのリスト
   */
  private List<Particle> createGaussianParticle(RobotPosition initRobotPos) {
    // スレッドセーフ化
    List<Particle> particleList = Collections
        .synchronizedList(new ArrayList<Particle>());
  
    // 初期位置を基にガウス分布にしたがってパーティクル生成
    for (int i = 0; i < PARTICLE_NUMBER; i++) {
      RobotPosition pos = RobotPosition.createRandomRobotPosition(initRobotPos);
      // パーティクルのリストに追加
      particleList.add(new Particle(pos, (double) 1.0D / PARTICLE_NUMBER));
  
    }
    return particleList;
  }

  /**
   * 一様分布に従って領域内にパーティクル生成する。
   * 
   * @return パーティクルのリスト
   */
  private List<Particle> createUniformParticles() {
    // スレッドセーフ化
    List<Particle> particleList = Collections
        .synchronizedList(new ArrayList<Particle>());
  
    for (int i = 0; i < PARTICLE_NUMBER; i++) {
      RobotPosition pos = RobotPosition.createRandomRobotPosition();
      // パーティクルのリストに追加
      particleList.add(new Particle(pos, (double) 1.0D / PARTICLE_NUMBER));
    }
    return particleList;
  }

  /**
   * 一様分布でランダムにパーティクルを生成する。
   * 
   * @param init_likeHood
   *          パーティクルの初期尤度
   * @return {@link Particle} パーティクル
   */
  public static Particle createRandomParticle(double init_likeHood) {
  
    if (mt == null)
      mt = new MT19937();
    RobotPosition pos = RobotPosition.createRandomRobotPosition();
  
    return new Particle(pos, init_likeHood);
  
  }

  /**
   * Low variance samplerによるリサンプリング (通名：等間隔サンプリング)
   * 同一のパーティクルが何度も選択される場合があるが、問題ない。
   * 
   * @param cloneParttList
   * @param num_add_pat
   * @return
   */
  public static List<Particle> lowVarianceResampling(
      List<Particle> src_partList, int num_add_pat) {

    if (mt == null)
      mt = new MT19937();

    List<Particle> clonePartList = Particle.cloneParticleList(src_partList);

    List<Particle> dstPatrList = Collections
        .synchronizedList(new ArrayList<Particle>());

    // 区間[0 ; 1/M]で乱数発生(Mは発生させるパーティクル数)
    double r = mt.nextDouble() / num_add_pat;

    // パーティクルの重みの和
    double c = clonePartList.get(0).getWeight();

    int i = 0;

    double interval = 1.0 / num_add_pat;
    for (int m = 0; m < num_add_pat; m++) {
      double U = r + m * interval;

      while (U > c) {
        i = i + 1;
        c = c + clonePartList.get(i).getWeight();
      }

      dstPatrList.add(clonePartList.get(i).clone());
    }

    return dstPatrList;
  }

  /**
   * 他のロボット及びランドマークの計測距離を基に尤度を計算する
   * 
   * @param zi
   *          観測値
   * @param pos
   *          尤度計算対象のパーティクル
   * @param detectable
   *          計測対象
   * @return
   */
  public static double calcCooprativeLikelihoodByDistance(
      OneLandmarkSensorData zi, RobotPosition pos, Detectable detectable) {

    double dx = Math.abs(detectable.getX() - pos.getX());
    double dy = Math.abs(detectable.getY() - pos.getY());

    double distance_x = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

    // 計測を基にガウス分布に従ってゆう度計算 ( =p(zi|x) )　　
    double likelihood = gaussian(zi.getDistance(), DISTANCE_VARIACE, distance_x);// 距離

    return likelihood;
  }

  /**
   * 他のロボット及び、ランドマークの計測角度を基に尤度を計算する。
   * 
   * @param zi
   *          観測値
   * @param pos
   *          尤度計算対象のパーティクル
   * @param detectable
   *          計測対象
   * @return
   */
  public static double calcCooprativeLikelihoodByAngle(
      OneLandmarkSensorData zi, RobotPosition pos, Detectable detectable) {
    // そのパーティクルから計測した時、観測される値
    double dx = Math.abs(detectable.getX() - pos.getX());
    double dy = Math.abs(detectable.getY() - pos.getY());
    double angle = Math.atan2(dy, dx) - pos.getAngle();

    // 計測を基にガウス分布に従ってゆう度計算 ( =p(zi|x) )　　
    double likelihood = gaussian(zi.getRelativeAngle(), ANGELE_VARIANCE, angle);// 角度
    return likelihood;
  }

  /**
   * 計測距離を基に尤度(ゆうど)計算
   * 
   * @param zi
   *          観測値
   * @param pos
   *          パーティクルの位置情報
   * @return
   */
  public static double calcLikelihoodByDistance(OneLandmarkSensorData zi,
      RobotPosition pos, Landmark mk) {

    // そのパーティクルから計測した時、観測される値
    double dx = Math.abs(mk.getX() - pos.getX());
    double dy = Math.abs(mk.getY() - pos.getY());

    double distance_x = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

    // 計測を基にガウス分布に従ってゆう度計算 ( =p(zi|x) )　　
    double distanceLikelihood = gaussian(zi.getDistance(), DISTANCE_VARIACE,
        distance_x);// 距離

    return distanceLikelihood;
  }

  /**
   * 尤度(ゆうど)計算 角度を基にゆう度計算を行う
   * 
   * 
   * @param zi
   *          観測値
   * @param pos
   * @param mk
   * @return
   */
  public static double calcLikelihoodByAngle(OneLandmarkSensorData zi,
      RobotPosition pos, Landmark mk) {
    // そのパーティクルから計測した時、観測される値
    double dx = Math.abs(mk.getX() - pos.getX());
    double dy = Math.abs(mk.getY() - pos.getY());
    double angle = Math.atan2(dy, dx) - pos.getAngle();

    // 計測を基にガウス分布に従ってゆう度計算 ( =p(zi|x) )　　
    double angleLikelihood = gaussian(zi.getRelativeAngle(), ANGELE_VARIANCE,
        angle);// 角度

    return angleLikelihood;
  }

  /**
   * ガウス関数
   * 
   * 
   * @param mu
   *          平均
   * @param sigma
   *          分散
   * @param x
   *          入力値
   * @return
   */
  public static double gaussian(double mu, double sigma, double x) {
    return Math.exp(-0.5D * Math.pow(x - mu, 2.0D) / Math.pow(sigma, 2.0D))
        / (Math.sqrt(2.0 * Math.PI) * sigma);
  }

}
