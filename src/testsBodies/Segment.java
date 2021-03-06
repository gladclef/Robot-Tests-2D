/**
 * 
 */
package testsBodies;

import math.Trig;

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
   * Calculates current torque on the joint as caused by gravity.
   */
  public float getTorque() {
    
    // get the center of mass and total mass
    Vec2 centerOfMass = new Vec2();
    double totalMass = calculateLocalCenterOfMass(centerOfMass);
    
    // calculate the force caused by gravity at the center of mass
    Vec2 grav = (Vec2) getBody().getWorld().getGravity();
    double gravMagnitude = grav.length() * totalMass;
    
    // calculate the force against the normal to the vector describing
    // the center of mass
    double angle = Trig.getAngle(centerOfMass);
    double x = 2 * gravMagnitude * Math.sin(angle / 2);
    double y = Trig.getLength(gravMagnitude, x);
    double normMagnitude = Trig.getLength(x, y);
    
    // calculate the torque
    float torque = (float) (normMagnitude * centerOfMass.length());
    double combinedAngle = Trig.getAngle(grav) - Trig.getAngle(centerOfMass);
    if (Math.abs(combinedAngle) > Math.PI) {
        combinedAngle = - (Math.PI - Math.abs(combinedAngle));
    }
    float sign = (combinedAngle > 0) ? 1f : -1f; 
    torque *= sign;
    
    return torque;
  }
  
  /**
   * Calculates the center of mass for the following segments, including the
   *     end effector but not including this segment, relative to this
   *     segments's joint position.
   * @param The center of mass
   * @return The total mass 
   */
  public float calculateLocalCenterOfMass(Vec2 centerOfMass) {
    float totalMass = 0;
    SegmentBody next = getNext();
    
    // handle case for end effector
    if (next == null) {
      return 0f;
    }
    
    // add up the center of mass and total mass for each of the following
    // segments
    for (; next != null; next = next.getNext()) {
      float mass = next.getBody().getMass();
      totalMass += mass;
      float massRatio = (totalMass == 0) ? (1f) : (mass / totalMass);
      Vec2 nextWorldPos = (Vec2) next.getBody().getWorldCenter().sub(centerOfMass);
      centerOfMass.addLocal(nextWorldPos.mul(massRatio));
    }
    
    return totalMass;
  }
}
