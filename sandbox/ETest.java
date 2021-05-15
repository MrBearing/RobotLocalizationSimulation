import localizationsim.localization.CreateException;
import localizationsim.localization.LocalizatinerType;


public class ETest {

  /**
   * @param args
   * @throws InterruptedException 
   * @throws CreateException 
   */
  public static void main(String[] args) throws InterruptedException, CreateException {
    //LocalizatinerType t = LocalizatinerType.MCL;
    
    //Thread.sleep(1000);
    
    LocalizatinerType t = LocalizatinerType.create("mcl");
    
    if(t== LocalizatinerType.MCL)
      System.out.println("MCL!!!!");
    System.out.println("dkdlkjdfslkjlkjdsflkj");
    
  }

}
