class weldJoint {

  WeldJointDef def;
  WeldJoint wj;

  weldJoint(Body b1,Body b2)
  {
    def = new WeldJointDef();
    def.collideConnected=true;

    Vec2 worldCoordsAnchorPoint = b1.getWorldPoint(new Vec2(0.0f,0.0f));

    def.bodyA = b1;
    def.bodyB = b2;

    def.localAnchorA.set( def.bodyA.getLocalPoint(worldCoordsAnchorPoint) );
    def.referenceAngle = def.bodyB.getAngle() - def.bodyA.getAngle();


    def.initialize(def.bodyA, def.bodyB, worldCoordsAnchorPoint);

    wj = (WeldJoint) box2d.createJoint(def);

  }

  void display() {
    if (def != null) {
      // We can get the two anchor points
      Vec2 v1 = new Vec2(def.bodyA.getPosition());
      wj.getAnchorA(v1);
      Vec2 v2 = new Vec2(def.bodyA.getPosition());
      wj.getAnchorB(v2);
      // Convert them to screen coordinates
      v1 = box2d.coordWorldToPixels(v1);
      v2 = box2d.coordWorldToPixels(v2);
      // And just draw a line
      stroke(0);
      strokeWeight(1);
      line(v1.x,v1.y,v2.x,v2.y);
    }
  }
  public WeldJoint getJoint()
  {
    return wj;
  }
}
