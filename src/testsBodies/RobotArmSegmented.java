package testsBodies;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

public class RobotArmSegmented {
  
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
  /** ratio of maximum applicable torque to the torque necessary to hold each
   *  motor against the force of gravity */
  public float strengthRatio = -1;
  
  /////////////////////////////////////////////////////////////////////////////
  // OTHER VALUES, SET BY THE USER
  /////////////////////////////////////////////////////////////////////////////

  /** the end effector attempts to retrieve the goal */
  private Body goal;
  
  /////////////////////////////////////////////////////////////////////////////
  // SET INTERNALLY, DOES NOT NEED TO BE SET BY THE USER
  /////////////////////////////////////////////////////////////////////////////
  
  /** list of segments in the arm, including the end effector */
  private List<Segment> segments;

  public RobotArmSegmented() {}
  
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
    if (strengthRatio < 0) {
      throw new InvalidParameterException("strengthRatio must be set before populating robot arm.");
    }
  }

  public List<Segment> getSegments() {
    return segments;
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
          basePos.x + baseRadius + lengthSummation + 
              (segmentLengths[i] / 2.0f) ,
          basePos.y);
      lengthSummation += segmentLengths[i] + (jointRadius * 2.0f);
    }
    
    return positions;
  }
  
  private void populateSegments(float[] segLengths, Vec2[] segPositions) {
    for (int i = 0; i < segmentCount; i++) {
      
      // create the fixtures and shapes for the segments
      FixtureDef fixtureDef = new FixtureDef();
      PolygonShape shapeDef = new PolygonShape();
      shapeDef.setAsBox(segLengths[i] / 2.0f, segmentWidth / 2.0f);
      fixtureDef.shape = shapeDef;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.2f;

      // create the bodies for the segments
      BodyDef blockBody = new BodyDef();
      blockBody.position = segPositions[i];
      blockBody.type = BodyType.DYNAMIC;
      Body segBody = world.createBody(blockBody);
      segBody.createFixture(fixtureDef);
      
      // verify that everything was done correctly
      assert (segBody.getWorldPoint(new Vec2(0f, 0f)).equals(
          segPositions[i])) :
            "segment origin out of alignment with creation origin";
      
      // track the segments
      segments.add(new Segment(
          segLengths[i],
          segPositions[i],
          (i == 0 ? baseRadius : jointRadius),
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
      
      // verify that the origin is still oriented around the segment lever arm
      assert (segments.get(i).getBody()
          .getWorldPoint(new Vec2(0f, 0f)).equals(
          segPositions[i])) : "origin out of alignment with segment lever arm";

      // create the bodies for the joints and attach to the segments
      shapeDef.m_p.set(new Vec2(
          (segLengths[i] / 2.0f) + jointRadius,
          0f));
      segments.get(i).getBody().createFixture(fixtureDef);
    }
  }
  
  private void populateMotors() {
    Body b1 = null;
    Body b2 = base;
    
    for (int segIndex = 0; segIndex < segmentCount; segIndex++) {

      // get the segment
      Segment seg = segments.get(segIndex);
      Vec2 segPosition = seg.getBody().getWorldPoint(new Vec2(0f, 0f));
      
      // create the motor
      b1 = b2;
      b2 = seg.getBody();
      Vec2 worldPivotPoint = new Vec2(
          segPosition.x - seg.getLength()/2.0f - 
              seg.getJointRadius(),
          segPosition.y);
      Motor motor = new Motor(b1, b2, worldPivotPoint);
      seg.setMotor(motor);
      
      // calculate the total mass and
      // the center of mass relative to the seg origin
      float totalTqMass = 0;
      Vec2 localCenterOfMass = seg.getBody().getWorldCenter();
      for (int i = segIndex; i < segmentCount; i++) {
        Segment tempSeg = segments.get(i);
        
        // get the mass
        MassData data = new MassData();
        tempSeg.getBody().getMassData(data);
        totalTqMass += data.mass;
        
        // add to the center of mass
        Vec2 distance = tempSeg.getBody().getWorldCenter().sub(
            localCenterOfMass);
        float massRatio = data.mass / totalTqMass;
        TestPanelJ2D.log.debug(
            " d:" + distance +
            " c:" + data.center.add(distance) +
            " /m:" + data.center.add(distance).mul(massRatio));
        localCenterOfMass =
            localCenterOfMass.add(data.center.add(distance).mul(massRatio));
        TestPanelJ2D.log.debug(" --- com:" + localCenterOfMass);
      }
      localCenterOfMass = localCenterOfMass.sub(
          seg.getBody().getWorldCenter()).add(
          seg.getBody().getLocalCenter());
      
      // calculate the maximum torque necessary to hold horizontal against the
      // force of gravity
      Vec2 localPivotPoint = segPosition.sub(worldPivotPoint);
      Vec2 relCenterOfMass = localPivotPoint.add(localCenterOfMass);
      float totalTqLength = (float) Math.sqrt(
          Math.pow(relCenterOfMass.x, 2) + 
          Math.pow(relCenterOfMass.y, 2));
      float totalTqAccel = (float) Math.sqrt(
          Math.pow(world.getGravity().x, 2) +
          Math.pow(world.getGravity().y, 2));
      float totalTq = (float) (totalTqLength * totalTqAccel * totalTqMass);
      
      // set the absolute maximum possible torque of the motor
      // relative to the total applicable torque from gravity.
      motor.setActualMaxTorque(totalTq * strengthRatio);
      
      // create the motor in the world
      motor.createJoint(world);
    }
  }
  
  public void populateRobotArm() {
    checkInitialized();
    
    // get stats on the segments
    float[] segLengths = calculateSegmentLengths();
    Vec2[] segPositions = calculateSegmentPositions();
    
    // create the new lists for the segments and joints
    segments = new ArrayList<Segment>();

    populateSegments(segLengths, segPositions);
    populateJoints(segLengths, segPositions);
    populateMotors();
  }

  public Body getGoal() {
    return goal;
  }

  public void setGoal(Body goal) {
    this.goal = goal;
  }
}
