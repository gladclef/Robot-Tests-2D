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
public class JUnitMotor {

  public static World getNewWorld() {
    World world = new World(new Vec2(0f, -10f));
    return world;
  }
  
  public static Body getNewBody(World world, Vec2 pos) {
    FixtureDef fd = new FixtureDef();
    fd.shape = new CircleShape();
    
    BodyDef bd = new BodyDef();
    bd.position.set(pos);
    Body body = world.createBody(bd);
    body.createFixture(fd);
    
    return body;
  }
  
  private static Motor getNewMotor() {
    return getNewMotor(getNewWorld());
  }
  
  private static Motor getNewMotor(World world) {
    Body b1 = getNewBody(world, new Vec2(-10, 0));
    Body b2 = getNewBody(world, new Vec2(10, 0));
    Vec2 anchor = new Vec2();
    return new Motor(b1, b2, anchor);
  }
  
  /**
   * Test method for {@link testsBodies.Motor#Motor(org.jbox2d.dynamics.Body, org.jbox2d.dynamics.Body, org.jbox2d.common.Vec2)}.
   */
  @Test
  public void testMotor() {
    Motor motor = getNewMotor();
    assertNotNull(motor.bodyA);
    assertNotNull(motor.bodyB);
  }

  /**
   * Test method for {@link testsBodies.Motor#createJoint(org.jbox2d.dynamics.World)}.
   */
  @Test
  public void testCreateJoint() {
    World world = getNewWorld();
    Motor motor = getNewMotor(world);
    // tests that it doesn't error, I suppose?
    motor.createJoint(world);
  }

  /**
   * Test method for {@link testsBodies.Motor#getActualMaxTorque()}.
   */
  @Test
  public void testGetActualMaxTorque() {
    Motor motor = getNewMotor();
    motor.setActualMaxTorque(10000);
    assertEquals(10000, motor.getActualMaxTorque(), 0.1);
  }

  /**
   * Test method for {@link testsBodies.Motor#setActualMaxTorque(float)}.
   */
  @Test
  public void testSetActualMaxTorque() {
    Motor motor = getNewMotor();
    motor.setActualMaxTorque(10000);
    assertEquals(10000, motor.getActualMaxTorque(), 0.1);
    motor.setPower(1.1f);
    motor.setActualMaxTorque(2000);
    assertEquals(2000, motor.getActualMaxTorque(), 0.1);
    assertEquals(2000, motor.getTorque(), 0.1);
  }

  /**
   * Test method for {@link testsBodies.Motor#getTorque()}.
   */
  @Test
  public void testGetTorque() {
    Motor motor = getNewMotor();
    motor.setActualMaxTorque(10000);
    motor.setTorque(10000);
    assertEquals(10000, motor.getTorque(), 0.1);
  }

  /**
   * Test method for {@link testsBodies.Motor#setTorque(float)}.
   */
  @Test
  public void testSetTorque() {
    Motor motor = getNewMotor();
    motor.setActualMaxTorque(10000);
    motor.setTorque(10000);
    assertEquals(10000, motor.getTorque(), 0.1);
  }

  /**
   * Test method for {@link testsBodies.Motor#getPower()}.
   */
  @Test
  public void testGetPower() {
    Motor motor = getNewMotor();
    motor.setActualMaxTorque(10000);
    motor.setPower(0.5f);
    assertEquals(0.5f, motor.getPower(), 0.1);
  }

  /**
   * Test method for {@link testsBodies.Motor#setPower(float)}.
   */
  @Test
  public void testSetPower() {
    Motor motor = getNewMotor();
    motor.setActualMaxTorque(10000);
    motor.setPower(0.5f);
    assertEquals(0.5f, motor.getPower(), 0.1);
  }

}
