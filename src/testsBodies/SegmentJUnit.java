/**
 * 
 */
package testsBodies;

import static org.junit.Assert.*;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.junit.Test;

/**
 * @author Benjamin
 *
 */
public class SegmentJUnit {

  public static Segment getNewSegment() {
    World world = MotorJUnit.getNewWorld();
    return getNewSegment(world, new Vec2());
  }
  
  public static Segment getNewSegment(World world, Vec2 bodyPos) {
    Body body = MotorJUnit.getNewBody(world, bodyPos);
    return new Segment(1f, new Vec2(), 1f, body);
  }
  
  /**
   * Test method for {@link testsBodies.Segment#Segment(float, org.jbox2d.common.Vec2, float, org.jbox2d.dynamics.Body)}.
   */
  @Test
  public void testSegment() {
    World world = MotorJUnit.getNewWorld();
    Vec2 bodyPos = new Vec2(2f, 3f);
    Segment segment = getNewSegment(world, bodyPos);
    assertEquals(1f, segment.getLength(), 0.01);
    assertEquals(new Vec2(), segment.getInitPosition());
    assertEquals(1f, segment.getJointRadius(), 0.01);
    assertEquals(bodyPos, segment.getBody().getWorldPoint(new Vec2()));
  }

  /**
   * Test method for {@link testsBodies.Segment#getMotor()}.
   */
  @Test
  public void testGetMotor() {
    World world = MotorJUnit.getNewWorld();
    Motor motor = MotorJUnit.getNewMotor(world);
    Segment segment = getNewSegment(world, new Vec2());
    segment.setMotor(motor);
    assertEquals(motor, segment.getMotor());
  }

  /**
   * Test method for {@link testsBodies.Segment#setMotor(testsBodies.Motor)}.
   */
  @Test
  public void testSetMotor() {
    World world = MotorJUnit.getNewWorld();
    Motor motor = MotorJUnit.getNewMotor(world);
    Segment segment = getNewSegment(world, new Vec2());
    segment.setMotor(motor);
    assertEquals(motor, segment.getMotor());
  }

  /**
   * Test method for {@link testsBodies.Segment#getTorque()}.
   */
  @Test
  public void testGetTorque() {
    World world = MotorJUnit.getNewWorld();
    Body body1 = MotorJUnit.getNewBody(world, new Vec2(1f, 0f));
    Body body2 = MotorJUnit.getNewBody(world, new Vec2(2f, 0f));
    Segment segment = getNewSegment(world, new Vec2());
    segment.setNext(new Segment(1f, new Vec2(1f, 0f), 0f, body1));
    segment.setNext(new Segment(1f, new Vec2(2f, 0f), 0f, body2));
    
    // verify the torque
    float torque = body1.getMass() * 1f + body2.getMass() * 2f;
    assertEquals(torque, segment.getTorque(), 0.01);
  }

  /**
   * Test method for {@link testsBodies.Segment#calculateLocalCenterOfMass(org.jbox2d.common.Vec2)}.
   */
  @Test
  public void testCalculateLocalCenterOfMass() {
    World world = MotorJUnit.getNewWorld();
    Body body1 = MotorJUnit.getNewBody(world, new Vec2(1f, 0f));
    Body body2 = MotorJUnit.getNewBody(world, new Vec2(2f, 0f));
    Segment segment = getNewSegment(world, new Vec2());
    segment.setNext(new Segment(1f, new Vec2(1f, 0f), 0f, body1));
    segment.setNext(new Segment(1f, new Vec2(2f, 0f), 0f, body2));
    
    // verify the center of mass and total mass
    Vec2 com = new Vec2();
    float totalMass = body1.getMass() + body2.getMass();
    assertEquals(totalMass, segment.calculateLocalCenterOfMass(com), 0.01);
    assertEquals(new Vec2(1.5f, 0f), com);
  }
}
