package tests;

import org.jbox2d.testbed.framework.TestbedTest;

public abstract class RobotTest extends TestbedTest {
  
  /**
   * To be called between iterations.
   */
  public abstract void update();
}
