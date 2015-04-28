/**
 * 
 */
package testsBodies;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

/**
 * @author Benjamin
 * A revolute joint from JBox2D that tracks other information
 * about itself to present a nicer interface to the programmer.
 */
public class Motor extends RevoluteJointDef {
  private float actualMaxTorque;
  private RevoluteJoint joint;
  private float desiredTorque = 0.0f;

  /**
   * Create a new motor, with parameters to initialize it using the
   *     RevoluteJointDef.initialize() method.
   * @param b1
   * @param b2
   * @param anchor
   */
  public Motor(final Body b1, final Body b2, final Vec2 anchor) {
    super.initialize(b1, b2, anchor);
    motorSpeed = (float) (Math.PI * 2);
    enableMotor = true;
  }
  
  /**
   * Once all the parameters are set, create the joint.
   * @param world The world that the bodies being joined together exist in.
   */
  public void createJoint(World world) {
    if (joint != null) {
      return;
    }
    joint = (RevoluteJoint) world.createJoint(this);
  }
  
  /**
   * @return The maximum amount of torque that this motor can apply.
   */
  public float getActualMaxTorque() {
    return actualMaxTorque;
  }

  /**
   * @param actualMaxTorque The maximum amount of torque that this motor can apply.
   */
  public void setActualMaxTorque(float actualMaxTorque) {
    boolean updateTorque = (
        Math.abs(desiredTorque) >=
        Math.abs(actualMaxTorque));
    this.actualMaxTorque = actualMaxTorque;
    if (updateTorque) {
      setTorque(desiredTorque);
    }
  }
  
  /**
   * @return The torque that is being applied, in N-m.
   */
  public float getTorque() {
    return maxMotorTorque;
  }
  
  /**
   * The applied torque is capped at the maximum applicable torque.
   * @param desiredTorque The torque that is desired to be applied, in N-m.
   */
  public void setTorque(float desiredTorque) {
    float sign = (desiredTorque < 0) ? -1 : 1;
    maxMotorTorque = sign * Math.min(
        Math.abs(desiredTorque),
        actualMaxTorque);
    TestPanelJ2D.log.debug("" + maxMotorTorque + " : " + actualMaxTorque);
    if (joint != null) {
      joint.setMaxMotorTorque(maxMotorTorque); 
    }
  }
  
  /**
   * @return The ratio of applied torque to the maximum torque (-1.0f to 1.0f).
   */
  public float getPower() {
    return maxMotorTorque / actualMaxTorque;
  }
  
  /**
   * @param power The percentage of torque to be applied (-1.0f to 1.0f).
   */
  public void setPower(float power) {
    setTorque(power * actualMaxTorque);
  }
}
