/**
 * 
 */
package main;

import org.jbox2d.testbed.framework.TestbedModel;

import tests.SimpleBox;

/**
 * @author gladc_000
 * Used to initialize the set of tests.
 */
public class MyTestList {
  public static void populateModel(TestbedModel model) {
    
    model.addCategory("Physics Test");
    model.addTest(new SimpleBox());
  }
}
