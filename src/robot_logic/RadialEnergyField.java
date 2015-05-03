/**
 * 
 */
package robot_logic;

import java.util.List;

import math.Trig;

import org.jbox2d.common.Vec2;

/**
 * @author Benjamin An energy field class for generating an energy field around
 *         a position.
 */
public class RadialEnergyField implements EnergyField<Integer> {
  private float radius, lastGoalRadius, buffer;
  private Vec2 lastGoalPosition;
  private float[] lastEnergyField;
  private int pointCount, lastLowestEnergyPosition;

  /**
   * Create a new energy field
   * 
   * @param pointCount
   *          The effective resolution of the field. Must be even.
   * @param radius
   *          The radius of the circle around the origin and around the goal
   *          position (assumed that the origin is actually an arm on a pivot
   *          that is trying to bring the end of the arm as close as possible to
   *          the goal position).
   * @throws IllegalArgumentException If pointCount is not even.
   */
  public RadialEnergyField(int pointCount, float radius)
      throws IllegalArgumentException {
    if (pointCount % 2 == 1) {
      throw new IllegalArgumentException("pointCount must be even");
    }
    setPointCount(pointCount);
    setRadius(radius);
    setBuffer(radius * 0.1f);
  }

  /**
   * Generates the energy field, given the goal position relative to the local
   * position, where [0,0] is the center of the radial energy field to test
   * from.
   * 
   * @param goalLocalPos
   *          The position of the goal that the arm of length radius is trying
   *          to get to.
   */
  public void generateField(Vec2 goalLocalPos) {
    generateField(goalLocalPos, radius);
  }

  /**
   * Generates the energy field, given the goal position relative to the local
   * position, where [0,0] is the center of the radial energy field to test
   * from.
   * 
   * @param goalLocalPos
   *          The position of the goal that the arm of length radius is trying
   *          to get to.
   * @param goalRadius
   *          The radius of the goal that is trying to be achieved.
   */
  public void generateField(Vec2 goalLocalPos, float goalRadius) {
    // check for a cached value
    if (lastGoalPosition != null && lastGoalPosition.equals(goalLocalPos)
        && lastGoalRadius == goalRadius) {
      return;
    }

    // find the potential energy of the field, where
    // 1 = maximum possible
    // 0 = on the circle
    float directAngle = Trig.getAngle(goalLocalPos);
    float distance1 = calculateDistance(directAngle, goalLocalPos, goalRadius);
    float distance2 =
        calculateDistance(directAngle + (float) Math.PI, goalLocalPos,
            goalRadius);
    float minDistance = Math.min(distance1, distance2);
    float maxDistance = Math.max(distance1, distance2) - minDistance;

    // find the potential energy of all points
    if (lastEnergyField == null ||
        lastEnergyField.length != pointCount) {
      lastEnergyField = new float[pointCount];
    }
    int halfPointCount = pointCount / 2;
    for (int i = 0; i < halfPointCount; i++) {
      float angle = getAngleForPosition(i);
      float distance =
          calculateDistance(angle, goalLocalPos, goalRadius) - minDistance;
      lastEnergyField[i] = distance / maxDistance;
      angle += Math.PI;
      distance =
          calculateDistance(angle, goalLocalPos, goalRadius) - minDistance;
      lastEnergyField[i + halfPointCount] = distance / maxDistance;
    }
    
    // cache the results
    lastGoalPosition = goalLocalPos;
    lastGoalRadius = goalRadius;
  }
  
  /**
   * Find the minimum distance between 
   * @param goalLocalPos
   * @param goalRadius
   * @return
   */
  private float getMinDistanceToGoal(Vec2 goalLocalPos, float goalRadius) {
    float distance = goalLocalPos.length();
    if (distance < radius + goalRadius) {
      return 0;
    }
    return distance - radius - goalRadius;
  }

  /**
   * Calculates the distance between the point on the origin circle given by the
   * angle from the origin and the circle around the goal position.
   * 
   * @param angleFromOrigin
   *          describes the position on the origin circle to measure the
   *          distance from
   * @param goalLocalPos
   *          goal position, relative to the origin
   * @param goalRadius
   *          radius of the goal circle
   * @return shortest distance between the point on the origin circle and the
   *         goal circle
   */
  private float calculateDistance(
      float angleFromOrigin, Vec2 goalLocalPos, float goalRadius) {

    // get the position on the origin circle
    Vec2 originLocalPosition =
        new Vec2((float) (radius * Math.cos(angleFromOrigin)),
            (float) (radius * Math.sin(angleFromOrigin)));

    // find the distance between this position and the goal circle
    float distanceToGoal =
        Trig.getLength(originLocalPosition.x - goalLocalPos.x,
            originLocalPosition.y - goalLocalPos.y);
    return Math.abs(distanceToGoal - goalRadius);
  }

  public void
      generateField(Vec2 goalLocalPos, List<Vec2> obstacleLocalPositions) {
    // TODO Auto-generated method stub
  }

  /**
   * Get the energy at the given position.
   * 
   * @param position
   *          Can be gathered from getPositionForAngle().
   * @throws IllegalStateException
   *           When there hasn't been a call to {@link generateField}.
   * @return The potential energy (0 - 1)
   */
  public float getEnergy(Integer position) throws IllegalStateException {
    if (lastGoalPosition == null) {
      throw new IllegalStateException(
          "Generate a field before finding the energy of a point in the field.");
    }
    return lastEnergyField[position];
  }

  /**
   * Get the energy at the given angle.
   * 
   * @param angle
   *          Any measurement in radians
   * @throws IllegalStateException
   *           When there hasn't been a call to {@link generateField}.
   * @return The potential energy (0 - 1)
   */
  public float getEnergy(float angle) throws IllegalStateException {
    if (lastGoalPosition == null) {
      throw new IllegalStateException(
          "Generate a field before finding the energy of a point in the field.");
    }
    int position = getPositionForAngle(angle);
    return lastEnergyField[position];
  }

  /**
   * Get the position in the array of energy potentials for the given angle.
   * 
   * @param angle
   *          Any measurement in radians
   * @return The position
   */
  public Integer getPositionForAngle(float angle) {
    double twoPi = Math.PI * 2.0;
    double units = (angle % twoPi) / twoPi;
    int position = (int) Math.round(units * pointCount);
    return new Integer(position);
  }

  /**
   * Get the angle of a given position in the array of energy potentials.
   * 
   * @param position
   *          The position
   * @return The angle in radians (0 to 2PI)
   */
  public float getAngleForPosition(int position) {
    double twoPi = Math.PI * 2.0;
    double units = (double)position / (double)pointCount;
    return (float) (units * twoPi);
  }

  /**
   * Get the position of lowest energy.
   * 
   * @throws IllegalStateException
   *           When there hasn't been a call to {@link generateField}.
   * @return The position of the lowest energy potential
   */
  public Integer getLowestEnergyPosition() throws IllegalStateException {
    if (lastGoalPosition == null) {
      throw new IllegalStateException(
          "Generate a field before finding the energy of a point in the field.");
    }
    return new Integer(lastLowestEnergyPosition);
  }

  /**
   * Get the position of lowest energy.
   * 
   * @throws IllegalStateException
   *           When there hasn't been a call to {@link generateField}.
   * @return The angle of the lowest energy potential
   */
  public float getLowestEnergyAngle() throws IllegalStateException {
    int position = getLowestEnergyPosition();
    return getAngleForPosition(position);
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

  /**
   * Get the buffer to avoid obstacles by, if possible.
   * 
   * @return the buffer
   */
  public float getBuffer() {
    return buffer;
  }

  /**
   * @param buffer
   *          The buffer to avoid obstacles by, if possible.
   */
  public void setBuffer(float buffer) {
    this.buffer = buffer;
  }
}
