class staticBodyRect
{

  Body body;
  float w;
  float h;
  float col;
  //
  staticBodyRect(float x_, float y_,float w_,float h_)
  {
    float x = x_;
    float y = y_;
    w = w_;
    h = h_;
    col = 175;
    makeBody(new Vec2(x,y),w,h);
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


  void display(float a,float h_,float w_)
  {

    Vec2 mouse = new Vec2(mouseX,mouseY);

    rectMode(PConstants.CENTER);
    pushMatrix();
    translate(mouse.x,mouse.y);
    rotate(a);
    fill(col);
    stroke(255,0,0);
    rect(0,0,w_,h_);
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
    rect(0,0,w,h);
    popMatrix();
  }
  void clear()
  {
    box2d.destroyBody(body);

  }

  // This function adds the rectangle to the box2d world
  void makeBody(Vec2 center, float w_, float h_) {
    // Define and create the body
    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;
    bd.position.set(box2d.coordPixelsToWorld(center));
    body = box2d.createBody(bd);
    // Define a polygon (this is what we use for a rectangle)
    PolygonShape sd = new PolygonShape();
    float box2dW = box2d.scalarPixelsToWorld(w_/2);
    float box2dH = box2d.scalarPixelsToWorld(h_/2);
    sd.setAsBox(box2dW, box2dH);

    // Define a fixture
    FixtureDef fd = new FixtureDef();
    fd.shape = sd;
    // Parameters that affect physics
    fd.friction = 1;

    body.createFixture(fd);


  }

  Body getBody()
  {
    return body;
  }


}
