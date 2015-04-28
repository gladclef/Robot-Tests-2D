/**
 * 
 */
package robot_logic;

import testsBodies.Motor;
import testsBodies.RobotArmSegmented;
import testsBodies.Segment;

/**
 * @author Benjamin
 * Sets the arm to specific positions.
 */
public class SegmentedArmPositions {
  public static enum positions { HORIZONTAL };
  
  public static void setPosition(RobotArmSegmented arm, positions position) {
    switch (position) {
    case HORIZONTAL:
      setPositionHorizontal(arm);
      break;
    }
  }
  
  public static void setPositionHorizontal(RobotArmSegmented arm) {
    for (Segment seg : arm.getSegments()) {
      Motor motor = seg.getMotor();
      motor.setPower(1f / arm.strengthRatio);
    }
  }
}
