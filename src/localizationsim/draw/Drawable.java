package localizationsim.draw;

import java.awt.Graphics2D;
/**
 * 描画可能なオブジェクトを表すインターフェース
 * @author TOkamoto
 *
 */
public interface Drawable {
  /**
   * ユーザーはこのメソッドを実装して描画を行なってください。
   * 
   * @param g2d
   */
  public void draw(Graphics2D g2d);
}
