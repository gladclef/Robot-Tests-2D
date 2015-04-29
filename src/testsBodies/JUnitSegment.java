/**
 * 
 */
package testsBodies;

import static org.junit.Assert.*;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.junit.Test;

/**
 * @author Benjamin
 *
 */
public class JUnitSegment {

  public static Segment getNewSegment() {
    World world = JUnitMotor.getNewWorld();
    return getNewSegment(world, new Vec2());
  }
  
  public static Segment getNewSegment(World world, Vec2 bodyPos) {
    Body body = JUnitMotor.getNewBody(world, bodyPos);
    return new Segment(1f, new Vec2(), 1f, body);
  }
  
  /**
   * Test method for {@link testsBodies.Segment#Segment(float, org.jbox2d.common.Vec2, float, org.jbox2d.dynamics.Body)}.
   */
  @Test
  public void testSegment() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link testsBodies.Segment#getMotor()}.
   */
  @Test
  public void testGetMotor() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link testsBodies.Segment#setMotor(testsBodies.Motor)}.
   */
  @Test
  public void testSetMotor() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link testsBodies.Segment#getTorque()}.
   */
  @Test
  public void testGetTorque() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link testsBodies.Segment#calculateLocalCenterOfMass(org.jbox2d.common.Vec2)}.
   */
  @Test
  public void testCalculateLocalCenterOfMass() {
    fail("Not yet implemented");
  }

}
