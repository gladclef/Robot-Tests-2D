/**
 * 
 */
package main;

import org.jbox2d.testbed.framework.TestbedModel;

import tests.SegmentedArmTest;
import tests.SimpleBox;

/**
 * @author gladc_000
 * Used to initialize the set of tests.
 */
public class MyTestList {
  public static void populateModel(TestbedModel model) {
    
    model.addCategory("Robot Tests");
    
    model.addCategory("Physics Test");
    model.addTest(new SegmentedArmTest());
    model.addTest(new SimpleBox());
  }
}
