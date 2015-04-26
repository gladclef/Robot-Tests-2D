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
   * Creates a new energy potential for the segment's position
   * and updates the motor torque to move the segment accordingly. 
   */
  public void update() {
    // TODO
  }
}
