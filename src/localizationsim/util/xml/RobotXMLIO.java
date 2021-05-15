package localizationsim.util.xml;
/**
 * ロボットの設定を記述したXMLファイルで使用されるタグとそのアトリビュートを記述したEnum
 * @author TOkamoto
 *
 */
public enum RobotXMLIO {
  TAG_ROBOTS("robots"),
  TAG_ROBOT("robot"), 
  ATTRIBUTE_NAME("name"), 
  ATTRIBUTE_X("x"), ATTRIBUTE_Y("y"), ATTRIBUTE_THETA("theta"), 
  TAG_VELOCITY("velocity"),
  ATTRIBUTE_ANGULAR("angular"),
  ATTRIBUTE_SPEED("speed"),
  TAG_GUI("gui"),
  ATTRIBUTE_VISIBLE("visible"),
  TAG_RESULT("result"),
  ATTRIBUTE_RECORDABLE("recode"),
  TAG_LOCALIZATINER("localizationer"),
  ATTRIBUTE_TYPE("type"),;
  

  private String value;

  private RobotXMLIO(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
