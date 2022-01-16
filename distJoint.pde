class distJoint
{

  DistanceJointDef djd;
  DistanceJoint dj;
  float length;
  float fre;
  float damp;

  distJoint(Body b1,Body b2,float len,float f,float d)
  {

    djd = new DistanceJointDef();
    djd.collideConnected=true;

    djd.bodyA = b1;
    djd.bodyB = b2;

    djd.length = box2d.scalarPixelsToWorld(len);

    djd.frequencyHz = f;  // Try a value less than 5 (0 for no elasticity)
    djd.dampingRatio = d; // Ranges between 0 and 1

    dj = (DistanceJoint) box2d.world.createJoint(djd);

  }

  void display() {
    if (djd != null) {
      // We can get the two anchor points
      Vec2 v1 = new Vec2(djd.bodyA.getPosition());
      dj.getAnchorA(v1);
      Vec2 v2 = new Vec2(djd.bodyA.getPosition());
      dj.getAnchorB(v2);
      // Convert them to screen coordinates
      v1 = box2d.coordWorldToPixels(v1);
      v2 = box2d.coordWorldToPixels(v2);
      // And just draw a line
      stroke(0);
      strokeWeight(1);
      line(v1.x,v1.y,v2.x,v2.y);
    }
  }

  public DistanceJoint getJoint()
  {
    return dj;
  }

  public Body bd1()
  {
    return djd.bodyA;
  }

  public Body bd2()
  {
    return djd.bodyB;
  }
}
