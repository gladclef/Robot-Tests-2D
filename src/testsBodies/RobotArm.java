package testsBodies;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

public class RobotArm {
  
  /////////////////////////////////////////////////////////////////////////////
  // MUST BE SET BEFORE THE POPULATE METHOD IS USED
  /////////////////////////////////////////////////////////////////////////////
  
  public Body base;
  public World world;
  
  /** number of segments in the arm, including the end effector */
  public int segmentCount = -1;
  
  // in meters
  /** total length of the arm, not including the base */
  public float totalLength = -1;
  /** radius of the base */
  public float baseRadius = -1;
  /** radius of each joint between segments */
  public float jointRadius = -1;
  /** position of the base */
  public Vec2 basePos;
  /** width of each segment */
  public float segmentWidth = -1;
  
  /** ratio of lengths between each successive segment */
  public float splitRatio = -1;
  
  /////////////////////////////////////////////////////////////////////////////
  // SET INTERNALLY, DOES NOT NEED TO BE SET BY THE USER
  /////////////////////////////////////////////////////////////////////////////
  
  /** list of segments in the arm, including the end effector */
  private List<Segment> segments;
  /** list of joints in the arm, not including the base */
  private List<Body> joints;

  public RobotArm() {}
  
  private void checkInitialized() {
    if (base == null) {
      throw new InvalidParameterException("Robot arm base must be set before populating.");
    }
    if (world == null) {
      throw new InvalidParameterException("World must be set before populating robot arm.");
    }
    if (basePos == null) {
      throw new InvalidParameterException("basePos must be set before populating robot arm.");
    }
    if (segmentCount < 0) {
      throw new InvalidParameterException("segmentCount must be set before populating robot arm.");
    }
    if (totalLength < 0) {
      throw new InvalidParameterException("totalLength must be set before populating robot arm.");
    }
    if (baseRadius < 0) {
      throw new InvalidParameterException("baseRadius must be set before populating robot arm.");
    }
    if (jointRadius < 0) {
      throw new InvalidParameterException("jointRadius must be set before populating robot arm.");
    }
    if (segmentWidth < 0) {
      throw new InvalidParameterException("segmentWidth must be set before populating robot arm.");
    }
    if (splitRatio < 0) {
      throw new InvalidParameterException("splitRatio must be set before populating robot arm.");
    }
  }

  public List<Segment> getSegments() {
    return segments;
  }
  
  public List<Body> getJoints() {
    return joints;
  }

  private float[] calculateSegmentLengths() {
    float jointLengths = (segmentCount - 1) * jointRadius * 2.0f;
    float totalSegLength = totalLength - jointLengths;
    
    // The "split length" is the total length of all segments in
    // "split" units, where the shortest segment is 1 split unit
    // long, the next is SPLIT_RATIO split units long, the next
    // SPLIT_RATIO^2 units long, etc.
    float splitLength = 1;
    for (int i = 2; i < segmentCount + 1; i++) {
      splitLength += Math.pow(splitRatio, i);
    }
    
    // calculate the lengths
    float[] segmentLengths = new float[segmentCount];
    for (int i = 0; i < segmentCount; i++) {
      // length of the segment in Split Ratio units
      float segSRLength = (float) Math.pow(splitRatio, segmentCount - i);
      // length of the segment in meters
      segmentLengths[i] = totalSegLength * segSRLength / splitLength;
    }
    
    return segmentLengths;
  }
  
  private Vec2[] calculateSegmentPositions() {
    float[] segmentLengths = calculateSegmentLengths();
    
    float lengthSummation = 0;
    Vec2[] positions = new Vec2[segmentCount];
    for (int i = 0; i < segmentCount; i++) {
      positions[i] = new Vec2(
          basePos.x,
          basePos.y + baseRadius + lengthSummation + 
            (segmentLengths[i] / 2.0f) );
      lengthSummation += segmentLengths[i] + (jointRadius * 2.0f);
    }
    
    return positions;
  }
  
  private void populateSegments(float[] segLengths, Vec2[] segPositions) {
    for (int i = 0; i < segmentCount; i++) {
      
      // create the fixtures and shapes for the segments
      FixtureDef fixtureDef = new FixtureDef();
      PolygonShape shapeDef = new PolygonShape();
      shapeDef.setAsBox(segmentWidth / 2.0f, segLengths[i] / 2.0f);
      fixtureDef.shape = shapeDef;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.2f;

      // create the bodies for the segments
      BodyDef blockBody = new BodyDef();
      blockBody.position = segPositions[i];
      blockBody.type = BodyType.DYNAMIC;
      Body segBody = world.createBody(blockBody);
      segBody.createFixture(fixtureDef);
      
      // track the segments
      segments.add(new Segment(
          segLengths[i],
          segPositions[i],
          jointRadius,
          segBody));
    }
  }
  
  private void populateJoints(float[] segLengths, Vec2[] segPositions) {
    for (int i = 0; i < segmentCount - 1; i++) {
      
      // create the fixtures and shapes for the joints
      FixtureDef fixtureDef = new FixtureDef();
      CircleShape shapeDef = new CircleShape();
      shapeDef.setRadius(jointRadius);
      fixtureDef.shape = shapeDef;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.2f;

      // create the bodies for the joints
      BodyDef blockBody = new BodyDef();
      blockBody.position = new Vec2(
          segPositions[i].x,
          segPositions[i].y + (segLengths[i] / 2.0f) + jointRadius);
      blockBody.type = BodyType.DYNAMIC;
      Body jointBody = world.createBody(blockBody);
      jointBody.createFixture(fixtureDef);

      // track the joints
      joints.add(jointBody);
    }
  }
  
  private void populateMotors() {
    // TODO
  }
  
  public void populateRobotArm() {
    checkInitialized();
    
    // get stats on the segments
    float[] segLengths = calculateSegmentLengths();
    Vec2[] segPositions = calculateSegmentPositions();
    
    // create the new lists for the segments and joints
    segments = new ArrayList<Segment>();
    joints = new ArrayList<Body>();

    populateSegments(segLengths, segPositions);
    populateJoints(segLengths, segPositions);
    populateMotors();
  }
}
