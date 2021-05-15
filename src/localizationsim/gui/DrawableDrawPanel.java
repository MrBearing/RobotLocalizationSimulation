package localizationsim.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import localizationsim.draw.Drawable;
/**
 * 描画可能要素を描画するためのJComponentです。
 * 
 * @author TOkamoto
 *
 */
public class DrawableDrawPanel extends JComponent{//extends JPanel
  /**
   * シリアルバージョンUID
   */
  private static final long serialVersionUID = 2199213525477377430L;
  /**
   * 描画可能オブジェクトを格納したリスト
   */
  private List<Drawable> drawableObjectList;
  /**
   * システムのデフォルトストロークを保存しておくための変数
   */
  private Stroke defaultStroke;
  /**
   * デフォルトのアフィン変換を保存しておくための変数
   */
  private AffineTransform defaultTransform;
  /**
   * 中心に座標を移すために使用される座標変換
   */
  private AffineTransform objectsTransform;
  /**
   * コンストラクタです。
   */
  public DrawableDrawPanel(){
    super();
    this.drawableObjectList = new ArrayList<Drawable>();
    
    this.setScale(1.0);
    
  }
  
  /**
   * 描画対象のオブジェクトを追加します。
   * @param dObject
   */
  public void addDrawableObject(Drawable dObject){
    this.drawableObjectList.add(dObject);
  }
  
  /**
   * コレクション内の描画対象のオブジェクトを全て追加します
   * @param c
   */
  public void addDrawableObject(Collection<Drawable> c){
    this.drawableObjectList.addAll(c);
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);// アンチエイリアスON
   
    
    //デフォルトのトランスフォームを取得
    defaultTransform = g2d.getTransform();

    // 座標変換
    g2d.transform(this.getAffineTransform());
    System.out.println();
    drawObjects(g2d);//Drawable要素を描画

    // デフォルトのトランスフォームに戻す。
    g2d.setTransform(defaultTransform);
  }

  
  
  /**
   * 描画対象を描画
   * @param g2d
   */
  private void drawObjects(Graphics2D g2d) {
    //デフォルトのストローク取得
    defaultStroke = g2d.getStroke();
    for(Drawable dob :this.drawableObjectList){
      dob.draw(g2d);
      
      //デフォルトのストロークに戻す
      g2d.setStroke(defaultStroke);
    }
  }
  /**
   * 描画スケールを変更する。
   * @param scale
   */
  public void setScale(double scale){
    Dimension d = this.getPreferredSize();
    
    this.setAffineTransform(new AffineTransform(scale, 0.0d, 0.0d,
        -scale, d.getWidth() / 2.0d, d.getHeight() / 2.0d));
  }
  /**
   * 描画のアフィン変換を取得します
   * 
   * @return
   */
  private AffineTransform getAffineTransform() {
    return objectsTransform;
  }

  /**
   * アフィン変換を設定します。
   * @param affineTransform
   */
  private void setAffineTransform(AffineTransform affineTransform) {
    this.objectsTransform = affineTransform;
  }

}
