/**
 * 
 */
package math;

import static org.junit.Assert.*;

import org.jbox2d.common.Vec2;
import org.junit.Test;

/**
 * @author gladc_000
 *
 */
public class TrigTest {

  /**
   * Test method for {@link math.Trig#getLength(float, float)}.
   */
  @Test
  public void testGetLengthFloatFloat() {
    assertEquals(5f, Trig.getLength(3f, 4f), 0.01);
  }

  /**
   * Test method for {@link math.Trig#getLength(double, double)}.
   */
  @Test
  public void testGetLengthDoubleDouble() {
    assertEquals(5.0, Trig.getLength(3.0, 4.0), 0.01);
  }

  /**
   * Test method for {@link math.Trig#getAngle(org.jbox2d.common.Vec2)}.
   */
  @Test
  public void testGetAngle() {
    float pi = (float) Math.PI;
    assertEquals(0f, Trig.getAngle(new Vec2(1,0)), 0.01);
    assertEquals(pi / 4, Trig.getAngle(new Vec2(1,1)), 0.01);
    assertEquals(pi / 2, Trig.getAngle(new Vec2(0,1)), 0.01);
    assertEquals(3*pi/4, Trig.getAngle(new Vec2(-1,1)), 0.01);
    assertEquals(pi, Math.abs(Trig.getAngle(new Vec2(-1,0))), 0.01);
    assertEquals(-3*pi/4, Trig.getAngle(new Vec2(-1,-1)), 0.01);
    assertEquals(-pi / 2, Trig.getAngle(new Vec2(0,-1)), 0.01);
    assertEquals(-pi / 4, Trig.getAngle(new Vec2(1,-1)), 0.01);
  }

}
