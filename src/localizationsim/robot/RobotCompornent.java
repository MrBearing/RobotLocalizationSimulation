package localizationsim.robot;

import localizationsim.draw.Drawable;
/**
 * IRobotに搭載されるコンポーネントを表すインターフェース
 * 
 * @author TOkamoto
 *
 */
public interface RobotCompornent extends Drawable{
  /**
   * コンポーネントの起動シーケンス
   */
  public void start();
  /**
   * コンポーネントの停止シーケンス
   */
  public void stop();
}
