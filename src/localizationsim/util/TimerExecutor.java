package localizationsim.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 特定のタスクを一定の時間間隔で実行するクラス
 * @note java.util.Timerクラスで置き換えしたほうがよくね
 * @author TOkamoto
 *
 */
public class TimerExecutor {
  private final double stepRate;
  private Runnable routine;
  
  private volatile ScheduledFuture<?> future;

  final private ScheduledExecutorService scheduledService;

  
  /**
   * TimeExecutorのコンストラクタ
   * 
   * @param routine 実行対象のルーチン
   * @param step_rate 実行間隔
   */
  public TimerExecutor(Runnable routine, double step_rate) {
    this.stepRate = step_rate;
    this.routine =routine;
    this.scheduledService = Executors.newSingleThreadScheduledExecutor();

    this.future = null;
  }

  /**
   * 処理開始を行うメソッド
   */
  public void start() {
    if(future == null)
    future = this.scheduledService.scheduleAtFixedRate(this.routine, 0,
        (long) stepRate, TimeUnit.MILLISECONDS);
    
  }

  /**
   * 処理を一時停止するメソッド
   */
  public void stop() {
    if (this.future != null) {
      this.future.cancel(true);
    }
  }
  /**
   * サービス停止するメソッド
   */
  public void shutdown() {
    scheduledService.shutdown();
  }

}