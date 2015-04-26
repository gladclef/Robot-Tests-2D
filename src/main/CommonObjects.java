package main;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class CommonObjects {
  public static void Grid(World world) {
    for (int i = -30; i < 50; i++) {
      FixtureDef fixtureDef = new FixtureDef();
      PolygonShape shapeDef = new PolygonShape();
      shapeDef.setAsBox(60f, 0.001f);
      fixtureDef.shape = shapeDef;
      fixtureDef.filter.maskBits = 0x0000;

      BodyDef blockBody = new BodyDef();
      blockBody.position = new Vec2(-30f, (float)i);
      world.createBody(blockBody).createFixture(fixtureDef);
    }
    
    for (int i = -30; i < 30; i++) {
      FixtureDef fixtureDef = new FixtureDef();
      PolygonShape shapeDef = new PolygonShape();
      shapeDef.setAsBox(0.001f, 80f);
      fixtureDef.shape = shapeDef;
      fixtureDef.filter.maskBits = 0x0000;

      BodyDef blockBody = new BodyDef();
      blockBody.position = new Vec2(i, 50f);
      world.createBody(blockBody).createFixture(fixtureDef);
    }
  }
}
