/**
 * 
 */
package robot_logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.junit.Test;

/**
 * @author gladc_000
 *
 */
public class RadialEnergyFieldJUnit {

  public static RadialEnergyField getRadialEnergyField() {
    return getRadialEnergyField(360, 4f);
  }

  public static RadialEnergyField getRadialEnergyField(
      int pointCount, float radius) {
    return new RadialEnergyField(pointCount, radius);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#RadialEnergyField(int, float)}.
   */
  @Test
  public void testRadialEnergyField() {
    try {
      new RadialEnergyField(1, 1);
      assertTrue("Should have thrown an IllegalArgumentException", false);
    } catch (IllegalArgumentException e) {
    }
    
    RadialEnergyField field = getRadialEnergyField();
    assertEquals(360, field.getPointCount());
    assertEquals(4f, field.getRadius(), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#generateField(org.jbox2d.common.Vec2)}
   * .
   */
  @Test
  public void testGenerateFieldVec2() {
    RadialEnergyField field = getRadialEnergyField();
    field.generateField(new Vec2(2f, 0f));

    // the following value was generated from the formula given by
    // x = (d^2 - rGoal^2 + rOrig^2) / 2d
    // as given on http://mathworld.wolfram.com/Circle-CircleIntersection.html
    float intersectionAngle = (float) 1.318;
    assertEquals(0f, field.getEnergy(intersectionAngle), 0.01);

    // check that the highest energy position is the one farthest from the
    // intersection
    assertEquals(1f, field.getEnergy((float) Math.PI), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#generateField(org.jbox2d.common.Vec2, float)}
   * .
   */
  @Test
  public void testGenerateFieldVec2Float() {
    RadialEnergyField field = getRadialEnergyField();
    field.generateField(new Vec2(2f, 0f), 3f);

    // the following value was generated from the formula given by
    // x = (d^2 - rGoal^2 + rOrig^2) / 2d
    // as given on http://mathworld.wolfram.com/Circle-CircleIntersection.html
    float intersectionAngle = (float) 0.813;
    assertEquals(0f, field.getEnergy(intersectionAngle), 0.01);

    // check that the highest energy position is the one farthest from the
    // intersection
    assertEquals(1f, field.getEnergy((float) Math.PI), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#generateField(org.jbox2d.common.Vec2, java.util.List)}
   * .
   */
  @Test
  public void testGenerateFieldVec2ListOfVec2() {
    RadialEnergyField field = getRadialEnergyField();
    ArrayList<Vec2> obstacles = new ArrayList<Vec2>();
    obstacles.add(new Vec2(8f, 0f));
    field.generateField(new Vec2(10f, 1f), obstacles);

    // angle of line tangent to obstacle is
    // asin((goalRadius + buffer) / goalX)
    float tangentAngle = (float) Math.asin((4 + field.getBuffer()) / 8);
    assertEquals(0f, field.getEnergy(tangentAngle), 0.01);

    // check that the highest energy position is the one farthest from the
    // intersection
    assertEquals(1f, field.getEnergy((float) Math.PI), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#getEnergy(java.lang.Integer)}.
   */
  @Test
  public void testGetEnergyInteger() {
    RadialEnergyField field = getRadialEnergyField();
    
    // check that it throws an exception
    try {
      field.getEnergy(0);
      assertTrue("Didn't throw exception", false);
    } catch (IllegalStateException e) {
    }

    // check that the highest energy position is the one farthest from the
    // goal circle and the lowest energy point is the one closest to it
    field.generateField(new Vec2(9f, 0f));
    assertEquals(0f, field.getEnergy(new Integer(0)), 0.01);
    assertEquals(1f, field.getEnergy(new Integer(180)), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#getEnergy(float)}.
   */
  @Test
  public void testGetEnergyFloat() {
    RadialEnergyField field = getRadialEnergyField();
    
    // check that it throws an exception
    try {
      field.getEnergy(0);
      assertTrue("Didn't throw exception", false);
    } catch (IllegalStateException e) {
    }

    // check that the highest energy position is the one farthest from the
    // goal circle and the lowest energy point is the one closes to it
    field.generateField(new Vec2(9f, 0f));
    assertEquals(0f, field.getEnergy(0f), 0.01);
    assertEquals(1f, field.getEnergy((float) Math.PI), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#getPositionForAngle(float)}.
   */
  @Test
  public void testGetPositionForAngle() {
    RadialEnergyField field = getRadialEnergyField();
    assertEquals(180, field.getPositionForAngle((float) Math.PI), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#getAngleForPosition(int)}.
   */
  @Test
  public void testGetAngleForPosition() {
    RadialEnergyField field = getRadialEnergyField();
    assertEquals(Math.PI, field.getAngleForPosition(180), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#getLowestEnergyPosition()}.
   */
  @Test
  public void testGetLowestEnergyPosition() {
    RadialEnergyField field = getRadialEnergyField();
    field.generateField(new Vec2(5f, 0f));
    assertEquals(0, field.getLowestEnergyPosition(), 0.01);
  }

  /**
   * Test method for
   * {@link robot_logic.RadialEnergyField#getLowestEnergyAngle()}.
   */
  @Test
  public void testGetLowestEnergyAngle() {
    RadialEnergyField field = getRadialEnergyField();
    field.generateField(new Vec2(5f, 0f));
    assertEquals(0f, field.getLowestEnergyAngle(), 0.01);
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getPointCount()}.
   */
  @Test
  public void testGetPointCount() {
    RadialEnergyField field = getRadialEnergyField();
    field.setPointCount(10);
    assertEquals(10, field.getPointCount());
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#setPointCount(int)}.
   */
  @Test
  public void testSetPointCount() {
    RadialEnergyField field = getRadialEnergyField();
    field.setPointCount(10);
    assertEquals(10, field.getPointCount());
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getRadius()}.
   */
  @Test
  public void testGetRadius() {
    RadialEnergyField field = getRadialEnergyField();
    field.setRadius(2);
    assertEquals(2, field.getRadius(), 0.01);
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#setRadius(float)}.
   */
  @Test
  public void testSetRadius() {
    RadialEnergyField field = getRadialEnergyField();
    field.setRadius(2);
    assertEquals(2, field.getRadius(), 0.01);
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getBuffer()}.
   */
  @Test
  public void testGetBuffer() {
    RadialEnergyField field = getRadialEnergyField();
    field.setBuffer(2f);
    assertEquals(2f, field.getBuffer(), 0.01);
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#setBuffer()}.
   */
  @Test
  public void testSetBuffer() {
    RadialEnergyField field = getRadialEnergyField();
    field.setBuffer(2f);
    assertEquals(2f, field.getBuffer(), 0.01);
  }
}
