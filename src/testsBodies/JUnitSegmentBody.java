/**
 * 
 */
package testsBodies;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.junit.Test;

/**
 * @author Benjamin
 *
 */
public class JUnitSegmentBody {

  public static SegmentBody getNewSegmentBody() {
    World world = JUnitMotor.getNewWorld();
    return getNewSegmentBody(world);
  }
  
  public static SegmentBody getNewSegmentBody(World world) {
    Body body = JUnitMotor.getNewBody(world, new Vec2());
    return new SegmentBody(1f, new Vec2(), 1f, body);
  }
  
  /**
   * Test method for {@link testsBodies.SegmentBody#SegmentBody()}.
   */
  @Test
  public void testSegmentBody() {
    SegmentBody sg = new SegmentBody();
    assertEquals(sg.getLength(), 0f, 0.01f);
    assertEquals(sg.getInitPosition(), new Vec2());
    assertEquals(sg.getJointRadius(), 0f, 0.01f);
    assertNull(sg.getBody());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#SegmentBody(float, org.jbox2d.common.Vec2, float, org.jbox2d.dynamics.Body)}.
   */
  @Test
  public void testSegmentBodyFloatVec2FloatBody() {
    SegmentBody sg = getNewSegmentBody();
    assertEquals(sg.getLength(), 1.0f, 0.01f);
    assertEquals(sg.getInitPosition(), new Vec2());
    assertEquals(sg.getJointRadius(), 1.0f, 0.01f);
    assertNotNull(sg.getBody());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getLength()}.
   */
  @Test
  public void testGetLength() {
    SegmentBody sg = getNewSegmentBody();
    sg.setLength(2f);
    assertEquals(2f, sg.getLength(), 0.01f);
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#setLength(float)}.
   */
  @Test
  public void testSetLength() {
    SegmentBody sg = getNewSegmentBody();
    sg.setLength(2f);
    assertEquals(2f, sg.getLength(), 0.01f);
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getInitPosition()}.
   */
  @Test
  public void testGetInitPosition() {
    SegmentBody sg = getNewSegmentBody();
    Vec2 pos = new Vec2(3f, 3f);
    sg.setInitPosition(pos);
    assertEquals(pos, sg.getInitPosition());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#setInitPosition(org.jbox2d.common.Vec2)}.
   */
  @Test
  public void testSetInitPosition() {
    SegmentBody sg = getNewSegmentBody();
    Vec2 pos = new Vec2(3f, 3f);
    sg.setInitPosition(pos);
    assertEquals(pos, sg.getInitPosition());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getLengthToEndEffector()}.
   */
  @Test
  public void testGetLengthToEndEffector() {
    World world = JUnitMotor.getNewWorld();
    SegmentBody seg = getNewSegmentBody(world);
    
    // no other segments
    assertEquals(0f, seg.getLengthToEndEffector(), 0.01f);
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#updateLengthToEndEffector(java.util.List)}.
   */
  @Test
  public void testUpdateLengthToEndEffectorListOfSegmentBody() {
    World world = JUnitMotor.getNewWorld();
    SegmentBody seg = getNewSegmentBody(world);
    
    // three other segments with length 1 and joint radius 1
    List<SegmentBody> others = new ArrayList<SegmentBody>();
    others.add(getNewSegmentBody(world));
    others.add(getNewSegmentBody(world));
    others.add(getNewSegmentBody(world));
    seg.updateLengthToEndEffector(others);
    assertEquals(6f, seg.getLengthToEndEffector(), 0.01f);
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#updateLengthToEndEffector(testsBodies.SegmentBody)}.
   */
  @Test
  public void testUpdateLengthToEndEffectorSegmentBody() {
    World world = JUnitMotor.getNewWorld();
    
    // four segments with length 1 and joint radius 1
    List<SegmentBody> segments = new ArrayList<SegmentBody>();
    segments.add(getNewSegmentBody(world));
    segments.add(getNewSegmentBody(world));
    segments.add(getNewSegmentBody(world));
    segments.add(getNewSegmentBody(world));
    
    // connect and calculate length
    for (int i = 3; i > -1; i--) {
      SegmentBody seg = segments.get(i);
      SegmentBody next = (i == 3) ? null : segments.get(i + 1);
      seg.setNext(next);
      seg.updateLengthToEndEffector();
    }
    
    // check length
    for (int i = 0; i < 4; i++) {
      SegmentBody other = segments.get(i);
      float expected = Math.max(0f, 6f - 3f * i);
      assertEquals("for segment at index:<" + i + ">",
          expected, other.getLengthToEndEffector(), 0.01f);
    }
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getJointRadius()}.
   */
  @Test
  public void testGetJointRadius() {
    SegmentBody seg = getNewSegmentBody();
    assertEquals(1f, seg.getJointRadius(), 0.01f);
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#setJointRadius(float)}.
   */
  @Test
  public void testSetJointRadius() {
    SegmentBody seg = getNewSegmentBody();
    seg.setJointRadius(2f);
    assertEquals(2f, seg.getJointRadius(), 0.01f);
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getBody()}.
   */
  @Test
  public void testGetBody() {
    SegmentBody seg = getNewSegmentBody();
    assertNotNull(seg.getBody());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#setBody(org.jbox2d.dynamics.Body)}.
   */
  @Test
  public void testSetBody() {
    World world = JUnitMotor.getNewWorld();
    SegmentBody seg = getNewSegmentBody(world);
    Body body = JUnitMotor.getNewBody(world, new Vec2(10f, 10f));
    seg.setBody(body);
    assertEquals(body, seg.getBody());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getNext()}.
   */
  @Test
  public void testGetNext() {
    World world = JUnitMotor.getNewWorld();
    SegmentBody seg = getNewSegmentBody(world);
    SegmentBody next = getNewSegmentBody(world);
    seg.setNext(next);
    assertEquals(next, seg.getNext());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#setNext(testsBodies.SegmentBody)}.
   */
  @Test
  public void testSetNext() {
    World world = JUnitMotor.getNewWorld();
    SegmentBody seg = getNewSegmentBody(world);
    SegmentBody next = getNewSegmentBody(world);
    seg.setNext(next);
    assertEquals(next, seg.getNext());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getPrev()}.
   */
  @Test
  public void testGetPrev() {
    World world = JUnitMotor.getNewWorld();
    SegmentBody seg = getNewSegmentBody(world);
    SegmentBody prev = getNewSegmentBody(world);
    seg.setPrev(prev);
    assertEquals(prev, seg.getPrev());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#setPrev(testsBodies.SegmentBody)}.
   */
  @Test
  public void testSetPrev() {
    World world = JUnitMotor.getNewWorld();
    SegmentBody seg = getNewSegmentBody(world);
    SegmentBody prev = getNewSegmentBody(world);
    seg.setPrev(prev);
    assertEquals(prev, seg.getPrev());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#isEndEffector()}.
   */
  @Test
  public void testIsEndEffector() {
    World world = JUnitMotor.getNewWorld();
    
    // three segments with length 1 and joint radius 1
    List<SegmentBody> segments = new ArrayList<SegmentBody>();
    segments.add(getNewSegmentBody(world));
    segments.add(getNewSegmentBody(world));
    segments.add(getNewSegmentBody(world));
    
    // connect
    segments.get(0).setNext(segments.get(1));
    segments.get(1).setNext(segments.get(2));
    
    // test
    assertFalse(segments.get(0).isEndEffector());
    assertFalse(segments.get(1).isEndEffector());
    assertTrue(segments.get(2).isEndEffector());
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getLocalNextJointPos()}.
   */
  @Test
  public void testGetLocalNextJointPos() {
    World world = JUnitMotor.getNewWorld();
    SegmentBody seg1 = getNewSegmentBody(world);
    SegmentBody seg2 = getNewSegmentBody(world);
    seg1.setNext(seg2);
    assertEquals(3f, seg1.getLocalNextJointPos().x, 0.01f);
  }

  /**
   * Test method for {@link testsBodies.SegmentBody#getWorldJointPosition()}.
   */
  @Test
  public void testGetWorldJointPosition() {
    SegmentBody seg = getNewSegmentBody();
    assertEquals(-1.5f, seg.getWorldJointPosition().x, 0.01f);
  }

}
