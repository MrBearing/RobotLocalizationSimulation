package dismain;

public class Hoge2 implements Runnable{
  public static int main=0;
  static Hoge2 h=new Hoge2();
  public Hoge2(){
      f();
      System.exit(0);
  }
  private void f(){
      if ( main==0 ) {
          System.out.print("Hello ");
          Runtime.getRuntime().addShutdownHook(new Thread(this));
      }
      else
          System.out.println("world");
      ++main;
  }
  public void run(){
      System.out.println("World");
  }
}