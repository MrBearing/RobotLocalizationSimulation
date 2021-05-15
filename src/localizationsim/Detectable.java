package localizationsim;

/**
 * LandMarkSensorで検出可能であることを示すインターフェース
 * @author TOkamoto
 *
 */
public interface Detectable {
  public double getX();
  public double getXAsMeter();
  public double getY();
  public double getYAsMeter();
  public Integer getID();
  @Override
  public String toString();
}
