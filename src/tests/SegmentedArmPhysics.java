package tests;

import main.CommonObjects;

import org.jbox2d.dynamics.Body;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class SegmentedArmPhysics extends TestbedTest {
  // number of segments in the arm
  private static final int SEGMENT_COUNT = 3;
  
  // in meters
  private static final float BASE_RADIUS = 4f;
  private static final float TOTAL_LENGTH = 50f;
  private static final float SEGMENT_WIDTH = 1.5f;
  private static final float JOINT_RADIUS = 0.5f;
  private static final Vec2 BASE_POS = new Vec2(-20,-20);
  
  // ratio of lengths between each successive segment
  private static final float SPLIT_RATIO = 1.5f;
  
  private Body base = null;
  private Body[] segments = null;
  private Body[] joints = null;

  @Override
  public boolean isSaveLoadEnabled() {
    return false;
  }

  @Override
  public void initTest(boolean deserialized) {
    { // define the base
      FixtureDef fd = new FixtureDef();
      fd.shape = new CircleShape();
      fd.shape.m_radius = BASE_RADIUS;
      fd.density = 1.0f;
      fd.friction = 0.9f;
      
      PolygonShape sd = new PolygonShape();
      sd.setAsBox(50.0f, 1.0f);
      
      BodyDef bd = new BodyDef();
      bd.position.set(BASE_POS);
      
      base = m_world.createBody(bd);
      base.createFixture(fd);
    }
    
    // make a grid
    {
      //CommonObjects.Grid(getWorld());
    }
    
    // define each segment
    {
      PopulateRobotArm();
    }
  }

  @Override
  public String getTestName() {
    return "Segmented Arm - Phys";
  }

  private float[] calculateSegmentLengths() {
    float totalLength = TOTAL_LENGTH - BASE_RADIUS;
    float jointLengths = (SEGMENT_COUNT - 1) * JOINT_RADIUS * 2.0f;
    float totalSegLength = totalLength - jointLengths;
    
    // The "split length" is the total length of all segments in
    // "split" units, where the shortest segment is 1 split unit
    // long, the next is SPLIT_RATIO split units long, the next
    // SPLIT_RATIO^2 units long, etc.
    float splitLength = 1;
    for (int i = 2; i < SEGMENT_COUNT + 1; i++) {
      splitLength += Math.pow(SPLIT_RATIO, i);
    }
    
    // calculate the lengths
    float[] segmentLengths = new float[SEGMENT_COUNT];
    for (int i = 0; i < SEGMENT_COUNT; i++) {
      float splitRatio = (float) Math.pow(SPLIT_RATIO, SEGMENT_COUNT - i);
      segmentLengths[i] = totalSegLength * splitRatio / splitLength;
    }
    
    return segmentLengths;
  }
  
  private Vec2[] calculateSegmentPositions() {
    float[] segmentLengths = calculateSegmentLengths();
    
    float lengthSummation = 0;
    Vec2[] positions = new Vec2[SEGMENT_COUNT];
    for (int i = 0; i < SEGMENT_COUNT; i++) {
      positions[i] = new Vec2(
          BASE_POS.x,
          BASE_POS.y + BASE_RADIUS + lengthSummation + 
            (segmentLengths[i] / 2.0f) );
      lengthSummation += segmentLengths[i] + (JOINT_RADIUS * 2.0f);
    }
    
    return positions;
  }
  
  private void PopulateRobotArm() {
    
    // get stats on the segments
    float[] segLengths = calculateSegmentLengths();
    Vec2[] segPositions = calculateSegmentPositions();
    
    // create the new arrays for the segments and joints
    segments = new Body[SEGMENT_COUNT];
    joints = new Body[SEGMENT_COUNT - 1];
    
    // create the bodies for the segments
    for (int i = 0; i < SEGMENT_COUNT; i++) {
      FixtureDef fixtureDef = new FixtureDef();
      PolygonShape shapeDef = new PolygonShape();
      TestPanelJ2D.log.debug("" + segLengths[i]);
      shapeDef.setAsBox(SEGMENT_WIDTH / 2.0f, segLengths[i] / 2.0f);
      fixtureDef.shape = shapeDef;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.2f;

      BodyDef blockBody = new BodyDef();
      blockBody.position = segPositions[i];
      blockBody.type = BodyType.DYNAMIC;
      segments[i] = getWorld().createBody(blockBody);
      segments[i].createFixture(fixtureDef);
    }
    
    // create the bodies for the joints
    for (int i = 0; i < SEGMENT_COUNT - 1; i++) {
      FixtureDef fixtureDef = new FixtureDef();
      CircleShape shapeDef = new CircleShape();
      shapeDef.setRadius(JOINT_RADIUS);
      fixtureDef.shape = shapeDef;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.2f;

      BodyDef blockBody = new BodyDef();
      blockBody.position = new Vec2(
          segPositions[i].x,
          segPositions[i].y + (segLengths[i] / 2.0f) + JOINT_RADIUS);
      blockBody.type = BodyType.DYNAMIC;
      joints[i] = getWorld().createBody(blockBody);
      joints[i].createFixture(fixtureDef);
    }
  }
}
