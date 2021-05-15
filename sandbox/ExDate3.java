import java.util.*;
import java.text.*;

public class ExDate3 {
  public static void main(String[] args) {
    Date date1 = new Date();  //(1)Dateオブジェクトを生成

    //(2)SimpleDateFormatオブジェクトを生成
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
    System.out.println(sdf1.format(date1));  //(3)Dateオブジェクトを表示

    sdf1.applyPattern("yyyy/MM/dd");  //(4)フォーマットパターンを変更
    System.out.println(sdf1.format(date1));  //(5)Dateオブジェクトを表示
  }
}
