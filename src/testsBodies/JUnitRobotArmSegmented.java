/**
 * 
 */
package testsBodies;

import static org.junit.Assert.*;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.junit.Test;

/**
 * @author Benjamin
 *
 */
public class JUnitRobotArmSegmented {

  private static World getNewWorld() {
    World world = new World(new Vec2(0f, -10f));
    return world;
  }
  
  private static Body getNewBase() {
    World world = getNewWorld();
    return getNewBase(world);
  }
  
  private static Body getNewBase(World world) {
    FixtureDef fd = new FixtureDef();
    fd.shape = new CircleShape();
    fd.shape.m_radius = 4.0f;
    fd.density = 1.0f;
    fd.friction = 0.9f;
    
    BodyDef bd = new BodyDef();
    bd.position.set(new Vec2(-20f, -20f));
    
    Body base = world.createBody(bd);
    base.createFixture(fd);
    
    return base;
  }
  
  public static RobotArmSegmented getNewInstance() {
    return getNewInstance(true);
  }
  
  public static RobotArmSegmented getNewInstance(boolean populated) {
    RobotArmSegmented arm = new RobotArmSegmented();
    arm.base = getNewBase();
    arm.world = arm.base.getWorld();
    arm.segmentCount = 3;
    arm.baseRadius = arm.base.getFixtureList().getShape().getRadius();
    arm.totalLength = 45f;
    arm.segmentWidth = 1.5f;
    arm.jointRadius = 0.5f;
    arm.basePos = arm.base.getWorldCenter();
    arm.splitRatio = 1.5f;
    arm.strengthRatio = 2.0f;
    if (populated) {
      arm.populateRobotArm(); 
    }
    return arm;
  }
  
  /**
   * Test method for {@link testsBodies.RobotArmSegmented#getSegments()}.
   */
  @Test
  public void testGetSegments() {
    RobotArmSegmented arm = getNewInstance();
    assertNotNull(arm.getSegments());
  }

  /**
   * Test method for {@link testsBodies.RobotArmSegmented#populateRobotArm()}.
   */
  @Test
  public void testPopulateRobotArm() {
    RobotArmSegmented arm = getNewInstance();
    
    // get the distance between the first and last segment
    Segment first = arm.getSegments().get(0);
    Segment last = arm.getSegments().get(arm.getSegments().size() - 1);
    Vec2 firstPos = first.getBody().getWorldPoint(new Vec2());
    Vec2 lastPos = last.getBody().getWorldPoint(new Vec2());
    double distance = Math.sqrt(
        Math.pow(firstPos.x - lastPos.x, 2) +
        Math.pow(firstPos.y - lastPos.y, 2));
    
    // add in the unaccounted length of the segments
    distance += first.getLength() / 2f;
    distance += last.getLength() / 2f;
    
    // verify
    assertTrue(
        "distance:" + distance + " arm.totalLength:" + arm.totalLength,
        Math.abs(distance - arm.totalLength) < 0.1f);
  }

  /**
   * Test method for {@link testsBodies.RobotArmSegmented#getGoal()}.
   */
  @Test
  public void testGetGoal() {
    RobotArmSegmented arm = getNewInstance();
    Body goal = getNewBase(arm.world);
    arm.setGoal(goal);
    assertTrue(arm.getGoal().equals(goal));
  }

  /**
   * Test method for {@link testsBodies.RobotArmSegmented#setGoal(org.jbox2d.dynamics.Body)}.
   */
  @Test
  public void testSetGoal() {
    RobotArmSegmented arm = getNewInstance();
    Body goal = getNewBase(arm.world);
    arm.setGoal(goal);
    assertTrue(arm.getGoal().equals(goal));
  }

}
