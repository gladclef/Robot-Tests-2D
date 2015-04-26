package tests;

import org.jbox2d.dynamics.Body;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import testsBodies.RobotArm;

public class SegmentedArmPhysics extends TestbedTest {
  
  // in meters
  private static final float BASE_RADIUS = 4f;
  private static final Vec2 BASE_POS = new Vec2(-20,-20);
  
  private Body base;
  private RobotArm arm;

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
      setArm(new RobotArm());
      arm.base = base;
      arm.world = getWorld();
      arm.segmentCount = 3;
      arm.baseRadius = BASE_RADIUS;
      arm.totalLength = 50f;
      arm.segmentWidth = 1.5f;
      arm.jointRadius = 0.5f;
      arm.basePos = BASE_POS;
      arm.splitRatio = 1.5f;
      arm.populateRobotArm();
    }
  }

  @Override
  public String getTestName() {
    return "Segmented Arm - Phys";
  }

  public RobotArm getArm() {
    return arm;
  }

  public void setArm(RobotArm arm) {
    this.arm = arm;
  }
}
