package localizationsim.robot.sensor;

import java.text.NumberFormat;

import Jama.Matrix;

public class OneLandmarkSensorData {
  private double distance;
  private double relativeAngle;
  private int id;

  public OneLandmarkSensorData(int id,double distance, double relativeAngle) {
    this.distance = distance;
    this.relativeAngle = relativeAngle;
    this.id = id;
  }
  
  public int getID(){
    return this.id;
  }
  
  public double getDistance() {
    return distance;
  }
  
  public double getDistanceAsMeter(){
    return distance/100.0;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }
  
  public double getRelativeAngle() {
    return relativeAngle;
  }

  public double getRelativeAngleAsDegree(){
    return relativeAngle * (180.0d / Math.PI);
  }
  
  public void setRelativeAngle(double relativeAngle) {
    this.relativeAngle = relativeAngle;
  }

  public String toString() {
    NumberFormat format = NumberFormat.getInstance();
    format.setMaximumFractionDigits(3);
    
    return "<<ID:"+id+"distance:"+format.format(this.getDistanceAsMeter())+"(m),angle:"
      +format.format(this.getRelativeAngleAsDegree())+"(deg)>>";
  }
  /**
   * 
   * 単位は(m , rad , なし)
   * 
   * @return
   */
  public Matrix toMatrix(){
    return new Matrix(new double[][]{{this.getDistanceAsMeter()},{this.relativeAngle},{this.id}});
  }
  
}












