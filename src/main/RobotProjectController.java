/**
 * 
 */
package main;

import org.jbox2d.testbed.framework.TestbedController;
import org.jbox2d.testbed.framework.TestbedErrorHandler;
import org.jbox2d.testbed.framework.TestbedModel;

import tests.RobotTest;

/**
 * @author Benjamin
 *
 */
public class RobotProjectController extends TestbedController {
  public RobotProjectController(TestbedModel argModel, UpdateBehavior behavior,
      MouseBehavior mouseBehavior, TestbedErrorHandler errorHandler) {
    super(argModel, behavior, mouseBehavior, errorHandler);
  }

  @Override
  public void updateTest() {
    ((RobotTest)currTest).update();
    super.updateTest();
  }
}
