package testsBodies;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * @author Benjamin
 * Represents the collection of bodies that make up this segment and joint.
 */
public class SegmentBody {
  /** length of this segment, in meters */
  private float length;
  /** initial position in the game for this segment, in meters */
  private Vec2 initPosition;
  /** length of combined joints and segments between this segment and the end
   *  effector segment (includes this segment's joint), in meters */
  private float lengthToEndEffector;
  /** radius of the joint at the beginning of this segment, in meters */
  private float jointRadius;
  /** The body of the segment */
  private Body body;
  /** The next segment in line.
   *  If null, then this must be the end effector. */
  private SegmentBody nextSegmentBody;
  /** The previous segment in line. */
  private SegmentBody prevSegmentBody;

  public SegmentBody() {
    this(0f, new Vec2(), 0f, null);
  }
  
  public SegmentBody(float length, Vec2 initPosition, float jointRadius, Body body) {
    this.setLength(length);
    this.setInitPosition(initPosition);
    this.setJointRadius(jointRadius);
    this.setBody(body);
  }

  public float getLength() {
    return length;
  }

  public void setLength(float length) {
    this.length = length;
  }

  public Vec2 getInitPosition() {
    return initPosition;
  }

  public void setInitPosition(Vec2 initPosition) {
    this.initPosition = initPosition;
  }

  public float getLengthToEndEffector() {
    return lengthToEndEffector;
  }

  /**
   * Calculates and sets the length to the end effector, not including the end
   *     effector, from the end of this segment body onward.
   * @param segments The other segments between this segment and the end
   *     effector, including the end effector segment.
   */
  public void updateLengthToEndEffector(List<SegmentBody> segments) {
    int segIndex = 1;
    for (SegmentBody seg : segments) {
      if (segIndex == segments.size()) {
        break;
      }
      segIndex++;
      lengthToEndEffector += seg.getLength() + seg.getJointRadius() * 2.0f;
    }
  }

  /**
   * Sets the length to the end effector based on the length calculated
   * by the next segment.
   * @param prevSegment The previous segment in the line of segments
   *     to the end effector.
   */
  public void updateLengthToEndEffector() {
    if (isEndEffector() ||
        nextSegmentBody.isEndEffector()) {
      lengthToEndEffector = 0f;
    } else {
      lengthToEndEffector = nextSegmentBody.getLengthToEndEffector() +
          getJointRadius() * 2.0f + getLength();
    }
  }
  
  public float getJointRadius() {
    return jointRadius;
  }

  public void setJointRadius(float jointRadius) {
    this.jointRadius = jointRadius;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }

  public SegmentBody getNext() {
    return nextSegmentBody;
  }

  public void setNext(SegmentBody nextSegmentBody) {
    this.nextSegmentBody = nextSegmentBody;
    if (nextSegmentBody != null) {
      nextSegmentBody.setPrev(this); 
    }
  }

  public SegmentBody getPrev() {
    return prevSegmentBody;
  }

  protected void setPrev(SegmentBody prevSegmentBody) {
    this.prevSegmentBody = prevSegmentBody;
  }
  
  public boolean isEndEffector() {
    return (nextSegmentBody == null);
  }
  
  /**
   * Calculates the position of the next revolute joint relative to the
   * position of this segment's revolute joint, where
   * Revolute Joint of Segment = pivot point that controls the segment.
   * @return The revolute point controlling the next segment, or the goal
   *     position if this is the end effector.
   */
  public Vec2 getLocalNextJointPos() {
    
    // get some information about the next body
    float nextJointRadius = 0;
    if (nextSegmentBody != null) {
      nextJointRadius = nextSegmentBody.getJointRadius();
    }
    
    // get the position based on the axis position
    float lengthToNextJoint = jointRadius + length + nextJointRadius;
    return getLocalAxisPosition(lengthToNextJoint);
  }
  
  /**
   * Gets the world position for the controlling joint of this segment.
   * @return The world position.
   */
  public Vec2 getWorldJointPosition() {
    float lengthToJoint = length / -2.0f - jointRadius;
    Vec2 localJointPosition = getLocalAxisPosition(lengthToJoint);
    return localJointPosition.add(body.getWorldPoint(new Vec2()));
  }
  
  /**
   * Get the position along a certain length of the axis.
   * @param lengthOnAxis The length along the axis to travel,
   *     where +length is towards the end effector and -length is towards the
   *     base.
   * @return The local position, reference point the controlling joing of the
   *     segment.
   */
  private Vec2 getLocalAxisPosition(float lengthOnAxis) {
    
    // get some information about this body
    float angle = body.getAngle();
    
    // calculate the offset (some basic trigonometry)
    float y = lengthOnAxis * (float)Math.sin(angle);
    float x = lengthOnAxis * (float)Math.cos(angle);
    
    // return the relative position
    return new Vec2(x, y);
  }
}
