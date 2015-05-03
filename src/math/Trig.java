/**
 * 
 */
package math;

import org.jbox2d.common.Vec2;

/**
 * @author gladc_000
 *
 */
public class Trig {
  
  /**
   * Get the length of the hypotonous of a right triangle.
   * @param v The vector
   * @return The length
   */
  public static float getLength(float x, float y) {
    return (float) Math.sqrt(x * x + y * y);
  }
  
  /**
   * Get the length of the hypotonous of a right triangle.
   * @param v The vector
   * @return The length
   */
  public static double getLength(double x, double y) {
    return Math.sqrt(x * x + y * y);
  }
  
  /**
   * Gets the angle of the vector, from PI to -PI,
   *     where 0 is located at [1,0]
   * @param v The vector
   * @return Angle of the vector, in radians.
   */
  public static float getAngle(Vec2 v) {
    
    // handle angles on a major axis
    if (v.x == 0) {
      if (v.y < 0) {
        return (float) (-Math.PI / 2);
      }
      return (float) (Math.PI / 2);
    } else if (v.y == 0 &&
        v.x < 0) {
      return (float) Math.PI;
    }
    
    // handle other angles
    float angle = (float) Math.atan(Math.abs(v.y) / Math.abs(v.x));
    if (v.x < 0) {
      if (angle < 0) {
        angle -= Math.PI / 2;
      } else {
        angle += Math.PI / 2;
      }
    }
    if (v.y < 0) {
      angle *= -1;
    }
    return angle;
  }
}
