/**
 * 
 */
package robot_logic;

import java.util.List;

import testsBodies.RobotArmSegmented;
import testsBodies.Segment;

/**
 * @author Benjamin
 * Used to house the logic used in moving a robot arm toward the goal.
 */
public class MoveTowardsGoal implements ArmLogic {
  private RobotArmSegmented arm;
  private SegmentedArmPositions positions;
  
  public MoveTowardsGoal(RobotArmSegmented arm, SegmentedArmPositions positions) {
    setArm(arm);
    setPositions(positions);
  }
  
  @Override
  /**
   * Set the motor torques to move the arm towards the goal.
   */
  public void update() {
    List<Segment> segments = arm.getSegments();
    float[] desiredTorques = new float[segments.size()];
    
    // find the desired torques for all segments
    for (int segIndex = 0; segIndex < segments.size(); segIndex++) {
      Segment seg = segments.get(segIndex);
      RadialEnergyField energyField = generateEnergyField(seg, 360);
      desiredTorques[segIndex] = findDesiredTorque(seg, energyField); 
    }
    
    // set the torque
    for (int segIndex = 0; segIndex < segments.size(); segIndex++) {
      Segment seg = segments.get(segIndex);
      seg.getMotor().setTorque(desiredTorques[segIndex]);
    }
  }

  private float findDesiredTorque(Segment seg, RadialEnergyField energyField) {
    // TODO
    float desiredTorque = 0;
    return desiredTorque;
  }

  /**
   * Generates a potential energy field that tells the segment where to move.
   * @param seg The segment to generate the field for.
   * @param points The accuracy to which to generate the field (how many points to
   *     generate the field for).
   * @return A potential energy field
   *     
   */
  private RadialEnergyField generateEnergyField(Segment seg, int points) {
    // TODO
    RadialEnergyField energyField = 
        new RadialEnergyField(points, seg.getLength() * 0.875f);
    return energyField;
  }

  public RobotArmSegmented getArm() {
    return arm;
  }

  public void setArm(RobotArmSegmented arm) {
    this.arm = arm;
  }

  public SegmentedArmPositions getPositions() {
    return positions;
  }

  public void setPositions(SegmentedArmPositions positions) {
    this.positions = positions;
  }
  
}
