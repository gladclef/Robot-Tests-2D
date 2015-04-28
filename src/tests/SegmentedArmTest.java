package tests;

import org.jbox2d.dynamics.Body;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import robot_logic.ArmLogic;
import robot_logic.MoveTowardsGoal;
import robot_logic.SegmentedArmPositions;
import testsBodies.RobotArmSegmented;

public class SegmentedArmTest extends RobotTest {
  
  // in meters
  private static final float BASE_RADIUS = 4f;
  private static final Vec2 BASE_POS = new Vec2(-26,0);
  
  private Body base;
  private RobotArmSegmented arm;
  private SegmentedArmPositions positions;
  private ArmLogic logic;
  
  public SegmentedArmTest() {
    positions = new SegmentedArmPositions();
  }

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
      setArm(new RobotArmSegmented());
      arm.base = base;
      arm.world = getWorld();
      arm.segmentCount = 3;
      arm.baseRadius = BASE_RADIUS;
      arm.totalLength = 45f;
      arm.segmentWidth = 1.5f;
      arm.jointRadius = 0.5f;
      arm.basePos = BASE_POS;
      arm.splitRatio = 1.5f;
      arm.strengthRatio = 2.0f;
      arm.populateRobotArm();
    }
    
    // set the logic type
    {
      logic = new MoveTowardsGoal(arm, positions);
    }
    
    // clear the goal and set an initial position
    {
      positions.goal = null;
      positions.setPosition(arm, SegmentedArmPositions.positions.HORIZONTAL);
      positions.setPosition(arm, SegmentedArmPositions.positions.MIDSPACE);
    }
  }

  @Override
  public String getTestName() {
    return "Segmented Arm - Phys";
  }

  public RobotArmSegmented getArm() {
    return arm;
  }

  public void setArm(RobotArmSegmented arm) {
    this.arm = arm;
  }

  @Override
  public void update() {
    logic.update();
  }
}
