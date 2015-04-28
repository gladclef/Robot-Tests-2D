/**
 * 
 */
package robot_logic;

import java.util.List;

import org.jbox2d.common.Vec2;

/**
 * @author Benjamin
 * An energy field class for generating an energy field around a position.
 */
public class RadialEnergyField implements EnergyField<Integer> {
  private float radius;
  private Vec2 lastGoalPosition;
  private float[] lastEnergyField;
  private int pointCount, lastLowestEnergyPosition;
  
  /**
   * Create a new energy field
   * @param pointCount The effective resolution of the field. Must be even.
   * @param radius The radius of the circle around the origin and around the
   *     goal position (assumed that the origin is actually an arm on a pivot
   *     that is trying to bring the end of the arm as close as possible to the
   *     goal position).
   */
  public RadialEnergyField(int pointCount, float radius) {
    if (pointCount % 2 == 1) {
      throw new IllegalArgumentException("pointCount must be even");
    }
    setPointCount(pointCount);
    setRadius(radius);
  }
  
  public void generateField(Vec2 goalLocalPos) {
    // check for a cached value
    if (lastGoalPosition != null &&
        lastGoalPosition.equals(goalLocalPos)) {
      return;
    }
    
    // find the potential energy of the field, where
    // 1 = maximum possible
    // 0 = on the circle
    float directAngle = findAngle(goalLocalPos);
    float distance1 = calculateDistance(directAngle, goalLocalPos);
    float distance2 = calculateDistance(directAngle + (float)Math.PI, goalLocalPos);
    float minDistance = Math.min(distance1, distance2);
    float maxDistance = Math.max(distance1, distance2) - minDistance;
    
    // find the potential energy of all points
    if (lastEnergyField.length != pointCount) {
      lastEnergyField = new float[pointCount];
    }
    int halfPointCount = pointCount / 2;
    for (int i = 0; i < halfPointCount; i++) {
      float angle = getAngleForPosition(i);
      float distance = calculateDistance(angle, goalLocalPos) - minDistance;
      lastEnergyField[i] = distance / maxDistance;
      angle += Math.PI;
      distance = calculateDistance(angle, goalLocalPos) - minDistance;
      lastEnergyField[i + halfPointCount] = distance / maxDistance;
    }
  }
  
  /**
   * Calculates the distance between the point on the origin circle given by
   *     the angle from the origin and the circle around the goal position. 
   * @param angleFromOrigin
   * @param goalLocalPos
   * @return
   */
  private float calculateDistance(float angleFromOrigin, Vec2 goalLocalPos) {
    
    // get the position on the origin circle
    Vec2 originLocalPosition = new Vec2(
        (float) (radius * Math.cos(angleFromOrigin)),
        (float) (radius * Math.sin(angleFromOrigin)));
    
    // find the distance between this position and the goal circle
    float distanceToGoal = (float) Math.sqrt(
        Math.pow(originLocalPosition.x - goalLocalPos.x, 2) +
        Math.pow(originLocalPosition.y - goalLocalPos.y, 2));
    return Math.abs(distanceToGoal - radius);
  }

  private float findAngle(Vec2 position) {
    return (float) Math.atan(position.y / position.x);
  }

  public void generateField(Vec2 goalLocalPos, List<Vec2> obstacleLocalPositions) {
    // TODO Auto-generated method stub
  }

  /**
   * Get the energy at the given position.
   * @param position Can be gathered from getPositionForAngle().
   */
  public float getEnergy(Integer position) {
    if (lastGoalPosition == null) {
      throw new IllegalStateException("Generate a field before finding the energy of a point in the field.");
    }
    return lastEnergyField[position];
  }
  
  public Integer getPositionForAngle(float angle) {
    double twoPi = Math.PI * 2.0;
    double units = (angle % twoPi) / twoPi;
    int position = (int) Math.round(units * lastEnergyField.length);
    return new Integer(position);
  }
  
  public float getAngleForPosition(int position) {
    double twoPi = Math.PI * 2.0;
    double units = position / lastEnergyField.length;
    return (float) (units * twoPi);
  }
  
  /**
   * Get the position of lowest energy.
   */
  public Integer getLowestEnergyPosition() {
    if (lastGoalPosition == null) {
      throw new IllegalStateException("Generate a field before finding the energy of a point in the field.");
    }
    return new Integer(lastLowestEnergyPosition);
  }

  public int getPointCount() {
    return pointCount;
  }

  public void setPointCount(int pointCount) {
    this.pointCount = pointCount;
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }
}
