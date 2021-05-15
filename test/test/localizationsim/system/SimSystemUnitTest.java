package test.localizationsim.system;

import java.util.Set;

import localizationsim.SimSystem;

import org.junit.Before;
import org.junit.Test;

public class SimSystemUnitTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetProperties() {
    Set<?> s = SimSystem.getPropertiesEntry();
    
    SimSystem.setProperty("TTTTTest", "4654646");
    
    System.out.println(s);
  }

}
