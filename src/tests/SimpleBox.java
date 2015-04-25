package tests;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.testbed.framework.TestbedTest;

public class SimpleBox extends TestbedTest {

  @Override
  public boolean isSaveLoadEnabled() {
    return true;
  }

  public void initTest(boolean argDeserialized) {
    if(argDeserialized){
      return;
    }
    
    { // Floor
      FixtureDef fixtureDef = new FixtureDef();
      PolygonShape shapeDef = new PolygonShape();
      shapeDef.setAsBox(50.0f, 1.0f);
      fixtureDef.shape = shapeDef;

      BodyDef floorBody = new BodyDef();
      floorBody.position = new Vec2(0.0f, -20.0f);
      getWorld().createBody(floorBody).createFixture(fixtureDef);

    }

    { // free-floating block
      FixtureDef fixtureDef = new FixtureDef();
      PolygonShape shapeDef = new PolygonShape();
      shapeDef.setAsBox(1.0f, 1.0f);
      fixtureDef.shape = shapeDef;
      fixtureDef.density = 25.0f;
      fixtureDef.friction = 0.5f;

      BodyDef blockBody = new BodyDef();
      blockBody.position = new Vec2(0.0f, 5f);
      blockBody.type = BodyType.DYNAMIC;
      getWorld().createBody(blockBody).createFixture(fixtureDef);
    }
  }

  @Override
  public String getTestName() {
    return "Simple Block Test";
  }
}
