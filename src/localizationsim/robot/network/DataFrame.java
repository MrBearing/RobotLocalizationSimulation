package localizationsim.robot.network;

import localizationsim.robot.RobotPosition;

/**
 * 
 * @author TOkamoto
 * 
 */
public class DataFrame {

  // TODO 余裕が有れば、Generics型として定義する。

  /**
   * 宛先のIDを示す
   */
  private Integer destinationID;
  /**
   * 送信元のIDを示す
   */
  private Integer sourceID;
  /**
   * 送信元ロボットの推定位置
   */
  private RobotPosition position;

  /**
   * コンストラクタ
   * 
   * @param src
   *          送信元アドレスを指定する
   * @param dst
   *          宛先アドレスを指定する
   * @param pos
   *          送信するロボットの位置
   */
  public DataFrame(Integer src, Integer dst, RobotPosition pos) {
    this.setSourceID(src);
    this.setDestinationID(dst);
    this.setPosition(pos);
  }

  /**
   * 送信元のアドレスを返す
   * 
   * @return 送信元アドレス
   */
  public Integer getSourceID() {
    return sourceID;
  }

  /**
   * 宛先アドレスを返す
   * 
   * @return 宛先アドレス
   */
  public Integer getDestinationID() {
    return destinationID;
  }

  /**
   * 格納されている送信データを表す
   * 
   * @return 送信元のロボットの推定自己位置
   */
  public RobotPosition getPosition() {
    return position;
  }

  private void setPosition(RobotPosition position) {
    this.position = position;
  }

  private void setSourceID(Integer sourceID) {
    this.sourceID = sourceID;
  }

  private void setDestinationID(Integer destinationID) {
    this.destinationID = destinationID;
  }

  @Override
  public String toString() {
    return "[[src ID " + this.getSourceID() + "destID"
        + this.getDestinationID() + this.getPosition() + "]]";

  }

  /**
   * 宛先アドレスがマルチキャストアドレスであるかを返す
   * 
   * @return
   */
  public boolean isMulticast() {
    return this.destinationID < 0;

  }

}
