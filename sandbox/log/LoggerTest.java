package log;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    final String LOGGER_NAME = "MyLogger";
    
    Logger logger = Logger.getLogger(LOGGER_NAME);
    
    //コンソールに出力するハンドラーを生成
    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(new SimpleFormatter());
    handler.setLevel(Level.CONFIG);
    logger.setLevel(Level.CONFIG);
    
    //"MyLogger"に作成したハンドラーを設定
    logger.addHandler(handler);
    
    logger.setUseParentHandlers(false);

    //ログの出力実行
    logger.config("configで出力");
    logger.info("infoで出力");

  }

}
