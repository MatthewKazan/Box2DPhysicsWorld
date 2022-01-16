class revJoint
{
    RevoluteJointDef rJD;
    RevoluteJoint rJ;
    float length;
    float fre;
    float damp;

    revJoint(Body b1,Body b2, Vec2 worldCenter)
    {

      rJD = new RevoluteJointDef();

      rJD.bodyA = b1;
      rJD.bodyB = b2;

      rJD.initialize(b1,b2,worldCenter);
      rJD.collideConnected = (false);
      rJD.motorSpeed = 0;
      rJD.maxMotorTorque = 10;

      rJ = (RevoluteJoint) box2d.world.createJoint(rJD);

    }

    void display() {

    }

    public RevoluteJoint getJoint()
    {
      return rJ;
    }

    public Body bd1()
    {
      return rJD.bodyA;
    }

    public Body bd2()
    {
      return rJD.bodyB;
    }
}
