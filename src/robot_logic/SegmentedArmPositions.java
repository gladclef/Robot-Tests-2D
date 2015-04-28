/**
 * 
 */
package robot_logic;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import testsBodies.Motor;
import testsBodies.RobotArmSegmented;
import testsBodies.Segment;

/**
 * @author Benjamin
 * Sets the arm to specific positions.
 */
public class SegmentedArmPositions {
  public static enum positions { HORIZONTAL, MIDSPACE };
  public Body goal;
  
  public void setPosition(RobotArmSegmented arm, positions position) {
    switch (position) {
    case HORIZONTAL:
      setPositionHorizontal(arm);
      break;
    case MIDSPACE:
      setPositionMidspace(arm);
      break;
    }
  }
  
  public void setPositionHorizontal(RobotArmSegmented arm) {
    for (Segment seg : arm.getSegments()) {
      Motor motor = seg.getMotor();
      motor.setPower(1f / arm.strengthRatio);
    }
  }
  
  /**
   * Sets the goal to a body in the middle of space, at about a
   * PI/6 angle to the base and arm length * 0.75 distance away from
   * the base.
   * @param arm The arm to set the goal for.
   */
  public void setPositionMidspace(RobotArmSegmented arm) {
    
    // find the position at which to place the goal
    float angle = (float) (Math.PI / 6.0);
    float distance = (arm.totalLength + arm.baseRadius) * 0.75f;
    Vec2 localGoalPosition = new Vec2(
        distance * (float)Math.cos(angle),
        distance * (float)Math.sin(angle));
    Vec2 worldGoalPosition = arm.basePos.add(localGoalPosition);
    
    // create the goal
    Body goal = getGoal(arm);
    
    // position the goal
    goal.setTransform(worldGoalPosition, 0);
  }
  
  /**
   * Gets the goal, or creates it if it hasn't been created yet.
   * @param arm The arm to create the goal for. 
   * @return The new (or existing) goal.
   */
  public Body getGoal(RobotArmSegmented arm) {
    if (goal != null) {
      return goal;
    }
    
    // create the fixtures and shapes for the new goal
    FixtureDef fixtureDef = new FixtureDef();
    CircleShape shapeDef = new CircleShape();
    shapeDef.setRadius(1.0f);
    fixtureDef.shape = shapeDef;
    
    // make the goal non-collidable
    fixtureDef.filter.maskBits = 0x0000;

    // create the body for the new goal
    BodyDef goalBody = new BodyDef();
    goalBody.position = new Vec2(0f, 0f);
    goalBody.type = BodyType.STATIC;
    goal = arm.world.createBody(goalBody);
    goal.createFixture(fixtureDef);
    
    // return the body
    return goal;
  }
}
