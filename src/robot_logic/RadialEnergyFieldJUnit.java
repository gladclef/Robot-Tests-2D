/**
 * 
 */
package robot_logic;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author gladc_000
 *
 */
public class RadialEnergyFieldJUnit {

  public static RadialEnergyField getRadialEnergyField() {
    return getRadialEnergyField(360, 4f);
  }
  
  public static RadialEnergyField getRadialEnergyField(int pointCount, float radius) {
    return new RadialEnergyField(pointCount, radius);
  }
  
  /**
   * Test method for {@link robot_logic.RadialEnergyField#RadialEnergyField(int, float)}.
   */
  @Test
  public void testRadialEnergyField() {
    RadialEnergyField field = getRadialEnergyField();
    assertEquals(360, field.getPointCount());
    assertEquals(4f, field.getRadius(), 0.01);
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#generateField(org.jbox2d.common.Vec2)}.
   */
  @Test
  public void testGenerateFieldVec2() {
    
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#generateField(org.jbox2d.common.Vec2, java.util.List)}.
   */
  @Test
  public void testGenerateFieldVec2ListOfVec2() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getEnergy(java.lang.Integer)}.
   */
  @Test
  public void testGetEnergy() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getPositionForAngle(float)}.
   */
  @Test
  public void testGetPositionForAngle() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getAngleForPosition(int)}.
   */
  @Test
  public void testGetAngleForPosition() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getLowestEnergyPosition()}.
   */
  @Test
  public void testGetLowestEnergyPosition() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getPointCount()}.
   */
  @Test
  public void testGetPointCount() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#setPointCount(int)}.
   */
  @Test
  public void testSetPointCount() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#getRadius()}.
   */
  @Test
  public void testGetRadius() {
    fail("Not yet implemented");
  }

  /**
   * Test method for {@link robot_logic.RadialEnergyField#setRadius(float)}.
   */
  @Test
  public void testSetRadius() {
    fail("Not yet implemented");
  }

}
