/**
 * 
 */
package robot_logic;

import java.util.List;

import org.jbox2d.common.Vec2;

/**
 * @author Benjamin
 * A potential energy field.
 */
public interface EnergyField<E> {
  public void generateField(Vec2 goalLocalPos);
  public void generateField(Vec2 goalLocalPos, List<Vec2> obstacleLocalPositions);
  public float getEnergy(E e);
  public E getLowestEnergyPosition();
}