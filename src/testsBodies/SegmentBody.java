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

  public SegmentBody() {
    this(0f, new Vec2(0f, 0f), 0f, null);
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
   * Sets the length to the end effector.
   * @param segments The other segments between this segment and the end
   *     effector, including the end effector segment.
   */
  public void updateLengthToEndEffector(List<SegmentBody> segments) {
    lengthToEndEffector = getJointRadius() * 2.0f;
    
    int segIndex = 1;
    for (SegmentBody seg : segments) {
      if (segIndex == segments.size()) {
        break;
      }
      lengthToEndEffector += seg.getLength() + seg.getJointRadius() * 2.0f;
    }
  }

  /**
   * Sets the length to the end effector.
   * @param prevSegment The previous segment in the line of segments
   *     to the end effector.
   */
  public void updateLengthToEndEffector(SegmentBody prevSegment) {
    lengthToEndEffector = prevSegment.getLengthToEndEffector() -
        prevSegment.getJointRadius() * 2.0f - getLength();
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
}
