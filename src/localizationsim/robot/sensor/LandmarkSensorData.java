package localizationsim.robot.sensor;

import java.util.Map;
import java.util.TreeMap;

public class LandmarkSensorData {
  
  private Map<Integer,OneLandmarkSensorData> list ;
  
  public LandmarkSensorData(){
    this.list = new TreeMap<Integer, OneLandmarkSensorData>();
  }
  
  public Map<Integer,OneLandmarkSensorData> getData(){
    return this.list;
  }
  
  public void addData(int i, double distance ,double relativeAngle){
    this.list.put(new Integer(i), new OneLandmarkSensorData(i, distance, relativeAngle));
  }
  
  public void addData(OneLandmarkSensorData data){
    this.list.put(data.getID(), data);
  }
  
  public String toString(){
    String ret = "";
    String BR  = System.getProperty("line.separator");
    
    for(Map.Entry<Integer,OneLandmarkSensorData> entry :list.entrySet()){
      ret+= "No."+entry.getKey().toString();
      ret+= entry.getValue().toString();
      ret += BR;
    }
    
    return ret;
  }
  
}
