/**
 * 
 */
package testsBodies;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * @author Benjamin
 * Represents the physical bodies of the segment, as well as the motors and
 * controller logic of the segment.
 */
public class Segment extends SegmentBody {
  /** The motor associated with this segment, that connects this segment
   *  to the previous segment. */
  private Motor motor;

  public Segment(float length, Vec2 initPosition, float jointRadius, Body body) {
    super(length, initPosition, jointRadius, body);
  }
  
  public Motor getMotor() {
    return motor;
  }

  public void setMotor(Motor motor) {
    this.motor = motor;
  }
  
  /**
   * Calculates current torque on the joint.
   */
  public float getTorque() {
    Vec2 centerOfMass = new Vec2();
    float totalMass = calculateLocalCenterOfMass(centerOfMass);
    // TODO
    return 0;
  }
  
  /**
   * Calculates the center of mass for the following segments, including the
   *     end effector but not including this segment
   * @param The center of mass
   * @return The total mass 
   */
  public float calculateLocalCenterOfMass(Vec2 centerOfMass) {
    // TODO
    return 0;
  }
}
