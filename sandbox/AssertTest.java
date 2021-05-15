
public class AssertTest {
  
  private static void verify(int x) {
    // x が負だとアサーションエラー
    assert x >= 0;
    System.out.println("x = " + x);
  }
  public static void main(String[] args) {
    int x = 0;
    verify(x);
    x = -1;
    verify(x);
  }

}
