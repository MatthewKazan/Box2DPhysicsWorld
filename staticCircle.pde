class staticCircle
{
  Body body;
  float r;
  float col;
  //
  staticCircle(float x_, float y_,float r_)
  {
    float x = x_;
    float y = y_;
    r = r_;
    col = 175;

    makeBody(new Vec2(x,y),r);
    body.setUserData(this);

  }

  void killBody()
  {
    box2d.destroyBody(body);
  }

  boolean contains(float x, float y)
  {
    Vec2 worldPoint = box2d.coordPixelsToWorld(x, y);
    Fixture f = body.getFixtureList();
    boolean inside = f.testPoint(worldPoint);

    return inside;
  }


  void display(float a,float r_)
  {

    Vec2 mouse = new Vec2(mouseX,mouseY);

    rectMode(PConstants.CENTER);
    pushMatrix();
    translate(mouse.x,mouse.y);
    rotate(a);
    fill(col);
    stroke(255,0,0);
    ellipse(0,0,r_*2,r_*2);
    popMatrix();
  }
  void show()
  {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();

    rectMode(PConstants.CENTER);
    pushMatrix();
    translate(pos.x,pos.y);
    rotate(-a);
    fill(col);
    stroke(0);
    ellipse(0,0,r*2,r*2);
    popMatrix();
  }
  void clear()
  {
    box2d.destroyBody(body);

  }

  // This function adds the rectangle to the box2d world
  void makeBody(Vec2 center, float r) {
    // Define and create the body
    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;
    bd.position.set(box2d.coordPixelsToWorld(center));
    body = box2d.world.createBody(bd);
    // Define a polygon (this is what we use for a rectangle)
    CircleShape cs = new CircleShape();
    cs.m_radius = box2d.scalarPixelsToWorld(r);

    // Define a fixture
    FixtureDef fd = new FixtureDef();
    fd.shape = cs;
    // Parameters that affect physics
    fd.friction = 1;

    body.createFixture(fd);
  }

  Body getBody()
  {
    return body;
  }


}
