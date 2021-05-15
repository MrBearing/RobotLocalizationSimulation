import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;


public class script {

  /**
   * @param args
   */
  public static void main(String[] args) {
    ScriptEngineManager manager = new ScriptEngineManager();
    for(ScriptEngineFactory factory : manager.getEngineFactories()){
      System.out.println(factory.getEngineName());
      for(String name : factory.getNames()){
        System.out.println("\t"+name);
      }
    }
  }

}
