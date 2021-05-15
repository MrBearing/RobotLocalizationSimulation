package dismain;
public class NoNeedsMain2 {
  static{
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("booo");
      }
    }));
    System.exit(0);
  }
}
