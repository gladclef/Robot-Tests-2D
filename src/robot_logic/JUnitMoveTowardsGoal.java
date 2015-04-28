/**
 * 
 */
package robot_logic;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testsBodies.JUnitRobotArmSegmented;
import testsBodies.RobotArmSegmented;
import testsBodies.Segment;

/**
 * @author Benjamin
 *
 */
public class JUnitMoveTowardsGoal {
  
  private static SegmentedArmPositions getNewPositions(RobotArmSegmented arm) {
    SegmentedArmPositions positions = new SegmentedArmPositions();
    positions.setPosition(arm, SegmentedArmPositions.positions.MIDSPACE);
    return positions;
  }
  
  private static MoveTowardsGoal getNewInstance() {
    RobotArmSegmented arm = JUnitRobotArmSegmented.getNewInstance();
    return new MoveTowardsGoal(arm, getNewPositions(arm));
  }

  /**
   * Test method for {@link robot_logic.MoveTowardsGoal#MoveTowardsGoal(testsBodies.RobotArmSegmented, robot_logic.SegmentedArmPositions)}.
   */
  @Test
  public void testMoveTowardsGoal() {
    MoveTowardsGoal logic = getNewInstance();
    assertNotNull(logic.getArm());
    assertNotNull(logic.getPositions());
  }

  /**
   * Test method for {@link robot_logic.MoveTowardsGoal#update()}.
   */
  @Test
  public void testUpdate() {
    MoveTowardsGoal logic = getNewInstance();
    logic.update();
    List<Segment> segments = logic.getArm().getSegments();
    for (Segment seg : segments) {
      assertNotEquals(seg.getMotor().getPower(), 0f);
    }
  }

  /**
   * Test method for {@link robot_logic.MoveTowardsGoal#getArm()}.
   */
  @Test
  public void testGetArm() {
    MoveTowardsGoal logic = getNewInstance();
    RobotArmSegmented arm = JUnitRobotArmSegmented.getNewInstance();
    logic.setArm(arm);
    assertTrue(logic.getArm().equals(arm));
  }

  /**
   * Test method for {@link robot_logic.MoveTowardsGoal#setArm(testsBodies.RobotArmSegmented)}.
   */
  @Test
  public void testSetArm() {
    MoveTowardsGoal logic = getNewInstance();
    RobotArmSegmented arm = JUnitRobotArmSegmented.getNewInstance();
    logic.setArm(arm);
    assertTrue(logic.getArm().equals(arm));
  }

  /**
   * Test method for {@link robot_logic.MoveTowardsGoal#getPositions()}.
   */
  @Test
  public void testGetPositions() {
    MoveTowardsGoal logic = getNewInstance();
    SegmentedArmPositions positions = getNewPositions(logic.getArm());
    logic.setPositions(positions);
    assertTrue(logic.getPositions().equals(positions));
  }

  /**
   * Test method for {@link robot_logic.MoveTowardsGoal#setPositions(robot_logic.SegmentedArmPositions)}.
   */
  @Test
  public void testSetPositions() {
    MoveTowardsGoal logic = getNewInstance();
    SegmentedArmPositions positions = getNewPositions(logic.getArm());
    logic.setPositions(positions);
    assertTrue(logic.getPositions().equals(positions));
  }

}
