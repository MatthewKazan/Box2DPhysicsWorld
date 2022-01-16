import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.joints.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.collision.shapes.Shape; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PhysicsWorld extends PApplet {








Box2DProcessing box2d;
staticBodyRect ground;
cValue hs1, hs2;
mouseJ mJ;
shapeSelector staticS;
shapeSelector dynamicS;

int type;
public float grav;
float rotation;
int w;
int h;
int r;
boolean sel;
boolean playing;
boolean editing;
boolean moving;

ArrayList boundaries;

ArrayList<Body> selection;

ArrayList<staticBodyRect> sBR;
ArrayList<staticCircle> sC;

ArrayList<dynamicBodyRect> dR;
ArrayList<dynamicCircle> dC;
//
ArrayList<distJoint> distanceJoint;
//
 ArrayList<weldJoint> wJoint;

ArrayList<button> b;
button back;
button set;
button select;
button edit;
button place;
button start;
button stop;
button earth;
button scale;
button move;
person p1;
car c1;

ArrayList<cValue> dynamic;
ArrayList<cValue> staticB;

ArrayList<cValue> distance;
cValue gravity;

public void setup() {
  
  

  box2d = new Box2DProcessing(this);
  box2d.createWorld();


  b = new ArrayList<button>();
  sBR = new ArrayList<staticBodyRect>();
  sC = new ArrayList<staticCircle>();
  dR = new ArrayList<dynamicBodyRect>();
  dC = new ArrayList<dynamicCircle>();
  selection = new ArrayList<Body>();
  distanceJoint = new ArrayList<distJoint>();
  wJoint = new ArrayList<weldJoint>();
  dynamic = new ArrayList<cValue>();
  distance = new ArrayList<cValue>();
  staticB = new ArrayList<cValue>();

  dynamic.add(new cValue(70, 300, 30, 30, "Density",1));
  dynamic.add(new cValue(70, 400, 30, 30, "Restitution",.1f));
  dynamic.add(new cValue(70, 500, 30, 30, "Friction",.1f));

  staticB.add(new cValue(70, 500, 30, 30, "Friction",.1f));


  distance.add(new cValue(80, 300, 30, 30, "Length",1));
  distance.add(new cValue(80, 400, 30, 30, "FrequencyHz",.1f));
  distance.add(new cValue(80, 500, 30, 30, "Damping",.1f));

  gravity = new cValue(width/2+150,125,30,30,"Gravity",1);


  b.add(new button(100,80,150,80,"Static",35));
  b.add(new button(100,170,150,80,"Dynamic",35));
  b.add(new button(100,260,150,80,"Spring",35));
  b.add(new button(100,350,150,80,"Weld",35));
  b.add(new button(width/2,100,200,100,"Stop",40));

  back = new button(100,height-100,100,50,"Back",30);
  set = new button(85,550,50,30,"Set",20);
  select = new button(width/2-75,100,100,50,"Select",20);
  place = new button(width/2+75,100,100,50,"Place",20);
  edit = new button(width/2+75,100,100,50,"Edit",20);
  start = new button(width/2,100,200,100,"Start",40);
  stop = new button(width/2,100,200,100,"Stop",40);
  p1 = new person();
  c1 = new car();
  // earth = new button(width/2+300,125,30,30,"√",20);
  scale = new button(100,750,(int)box2d.scalarWorldToPixels(10),20,"10 Meters",15);
  move= new button(width/2+300,125,80,40,"Move",30);

  boundaries = new ArrayList();
  boundaries.add(new Boundary(width/2,height-5,width,10,0));
  boundaries.add(new Boundary(width-5,height/2,10,height*10,0));
  boundaries.add(new Boundary(200,height/2,10,height*10,0));



  grav = -9.8f;
  rotation = 0;
  w = 50;
  h = 50;
  r = 25;
  type = -1;
  sel = false;
  playing = false;
  editing = false;
  moving = false;

  box2d.setGravity(0, grav);
  gravity.setValue(grav);
  ground = new staticBodyRect(530,800,10600,20);
  mJ = new mouseJ();
  staticS = new shapeSelector();
  dynamicS = new shapeSelector();
}


public void draw()
{
  background(255);
  box2d.step();
  keyTyped();
  showAll();
  colorC();
  fill(0);
  scale.disB();
  mJ.update(mouseX,mouseY);
  mJ.display();

  for (int i = 0; i < boundaries.size(); i++)
  {
    Boundary wall = (Boundary) boundaries.get(i);
    wall.display();
  }

  if(type==-1)
  {

    for(button bs:b)
    {
      bs.disB();
    }
    if(playing==false)
    {
      box2d.setGravity(0, 0);
      start.disB();
      move.disB();
      p1.stopP();
      c1.stopP();
      for(dynamicBodyRect b:dR)
      {
        b.getBody().setAwake(false);
      }
      for(dynamicCircle b:dC)
      {
        b.getBody().setAwake(false);
      }
      gravity.display();
      //earth.disB();
    }
    else
    {
      box2d.setGravity(0, grav);
      stop.disB();
      gravity.display();
      move.disB();
      p1.startP();
      c1.startP();
      //earth.disB();
      for(dynamicBodyRect b:dR)
      {
        b.getBody().setAwake(true);
      }
      for(dynamicCircle b:dC)
      {
        b.getBody().setAwake(true);
      }
    }
  }
  else
  {
    back.disB();
    set.disB();
    select.disB();
  }
  switch(type)
  {
    case 0:
      editing = false;
      staticS.display();
      switch(staticS.getShape())
      {
        case 0:
          for(staticBodyRect b:sBR)
          {
            if(sel==false)
            {
              b.display(rotation,h,w);
            }
          }
          break;
        case 1:
          for(staticCircle b:sC)
          {
            if(sel==false)
            {
              b.display(rotation,r);
            }
          }
          break;

      }
      place.disB();
      for(cValue b:staticB)
      {
        b.display();
      }

      break;
    case 1:
      editing = false;
      dynamicS.display();
      switch(dynamicS.getShape())
      {
        case 0:
          for(dynamicBodyRect b:dR)
          {
            if(sel==false)
            {
              b.display(rotation,h,w);
            }
          }
          for(dynamicBodyRect b:dR)
          {
            fill(255);
            textSize(15);
            textAlign(CORNER);
            Vec2 posxy = box2d.coordWorldToPixels(b.getBody().getPosition());
            text(b.getBody().getFixtureList().getDensity(),posxy.x-30,posxy.y-4);
            text(b.getBody().getFixtureList().getRestitution(),posxy.x-30,posxy.y+8);
            text(b.getBody().getFixtureList().getFriction(),posxy.x-30,posxy.y+20);
            textAlign(CENTER);
          }
          break;
        case 1:
          for(dynamicCircle b:dC)
          {
            if(sel==false)
            {
              b.display(rotation,r);
            }
          }
          for(dynamicCircle b:dC)
          {
            fill(255);
            textSize(15);
            textAlign(CORNER);
            Vec2 posxy = box2d.coordWorldToPixels(b.getBody().getPosition());
            text(b.getBody().getFixtureList().getDensity(),posxy.x-30,posxy.y-4);
            text(b.getBody().getFixtureList().getRestitution(),posxy.x-30,posxy.y+8);
            text(b.getBody().getFixtureList().getFriction(),posxy.x-30,posxy.y+20);
            textAlign(CENTER);
          }
          break;
        }

      for(cValue b:dynamic)
      {
        b.display();
      }

      //selection.clear();
      place.disB();
      break;

    case 2:
        if(selection.size()==2)
        {
          float length = dist(box2d.coordWorldToPixels(selection.get(0).getPosition()).x,
                              box2d.coordWorldToPixels(selection.get(0).getPosition()).y,
                              box2d.coordWorldToPixels(selection.get(1).getPosition()).x,
                              box2d.coordWorldToPixels(selection.get(1).getPosition()).y);
          if(sel==true)
          {
            distanceJoint.add(new distJoint(selection.get(0),selection.get(1),length,distance.get(1).clicked(),distance.get(2).clicked()));
            selection.clear();
            distance.get(0).setValue(length);

          }
          else if(editing)
          {

          }
        }

      for(cValue b:distance)
      {
        b.display();
      }
      edit.disB();
    break;

    case 3:
      sel = true;

      if(selection.size()==2)
      {
        float length = dist(box2d.coordWorldToPixels(selection.get(0).getPosition()).x,
                            box2d.coordWorldToPixels(selection.get(0).getPosition()).y,
                            box2d.coordWorldToPixels(selection.get(1).getPosition()).x,
                            box2d.coordWorldToPixels(selection.get(1).getPosition()).y);

        wJoint.add(new weldJoint(selection.get(0),selection.get(1)));
        selection.clear();
      }
    break;
  }

  //System.out.println(type);
}




public void changeType()
{
  if(type==-1)
  {
    for(int i = 0;i<b.size();i++)
    {
      if(b.get(i).mouse())
      {
        type = i;

      }
    }
    if(start.mouse()&&playing==false)
    {
      playing=true;
      type = -1;
      selection.clear();
    }
    else if(stop.mouse()&&playing==true)
    {
      playing=false;
      type = -1;
      selection.clear();
    }
    if(move.mouse()&&moving==false)
    {
      moving = true;
      sel = false;
      editing = false;
      move.change(true);

    }
    else if(move.mouse()&&moving==true)
    {
      moving = false;
      sel = false;
      editing = false;
      move.change(false);
    }
  }
  else
  {
    if(back.mouse())
    {
      type = -1;
      sel = false;
      editing = false;
      select.change(false);
      edit.change(false);
      selection.clear();

    }
    if(place.mouse())
    {
      sel = false;
      editing = false;
      selection.clear();
      place.change(true);
      select.change(false);
      edit.change(false);
    }
    if(select.mouse())
    {
      sel = true;
      editing = false;
      select.change(true);
      edit.change(false);
      place.change(false);

    }
    if(edit.mouse())
    {
      sel = false;
      editing = true;
      select.change(false);
      edit.change(true);
    }
  }
}
public void changeValue()
{
  if(type==1&&set.mouse())
  {
    if(selection.size()>0)
    {
      for(int i = 0;i<selection.size();i++)
      {
        selection.get(i).getFixtureList().setDensity(dynamic.get(0).clicked());
        selection.get(i).getFixtureList().setRestitution(dynamic.get(1).clicked());
        selection.get(i).getFixtureList().setFriction(dynamic.get(2).clicked());
        selection.get(i).resetMassData();
        //selection.get(i).resizeR();
      }
    }
  }
  if(type==2&&set.mouse()&&selection.size()==2)
  {
    for(int i = 0;i<distanceJoint.size();i++)
    {
      if((distanceJoint.get(i).bd1()==selection.get(0)||distanceJoint.get(i).bd1()==selection.get(0))&&
            (distanceJoint.get(i).bd2()==selection.get(1)||distanceJoint.get(i).bd2()==selection.get(1)))
      {
        Body b1 = distanceJoint.get(i).bd1();
        Body b2 = distanceJoint.get(i).bd2();
        box2d.world.destroyJoint(distanceJoint.get(i).getJoint());
        distanceJoint.remove(i);
        distanceJoint.add(new distJoint(selection.get(0),selection.get(1),distance.get(0).clicked(),distance.get(1).clicked(),distance.get(2).clicked()));
      }
    }
  }
}
public void mousePressed()
{
  changeType();
  changeValue();
  grav = gravity.clicked();
  if(type==0)
  {
    staticS.mouseAction();
  }
  if(type==1)
  {
    dynamicS.mouseAction();
  }
  for(cValue b:dynamic)
  {
    b.clicked();
  }
  for(cValue b:distance)
  {
    b.clicked();
  }
  if(moving==true&&type==-1)
  {
    for(dynamicBodyRect box:dR)
    {
      if(box.contains(mouseX, mouseY))
      {
        mJ.bind(mouseX,mouseY,box.getBody());
      }
    }
    for(dynamicCircle box:dC)
    {
      if(box.contains(mouseX, mouseY))
      {
        mJ.bind(mouseX,mouseY,box.getBody());
      }
    }


    if(p1.contains(mouseX,mouseY))
    {
      mJ.bind(mouseX,mouseY,p1.getHead());
    }
    if(c1.contains(mouseX,mouseY))
    {
      mJ.bind(mouseX,mouseY,c1.getCar());
    }
  }
  if(sel==true||editing==true)
  {
    select();
  }
  else if(sel==false)
  {
    if(type==0)
    {
      staticS.mouseAction();
      switch(staticS.getShape())
      {
        case 0:
          if(mouseX>200&&mouseX<width-200&&mouseY>200)
          {
            Vec2 mouse = new Vec2(mouseX,mouseY);
            mouse.x = mouse.x;// + (ppp.x)-width/2;
            mouse.y = mouse.y;// + (ppp.y)-height/2;
            sBR.add(new staticBodyRect(mouse.x,mouse.y,w,h));
            sBR.get(sBR.size()-1).getBody().setTransform(sBR.get(sBR.size()-1).getBody().getPosition(),-rotation);
          }
          break;
        case 1:
          if(mouseX>200&&mouseX<width-200&&mouseY>200)
          {
            Vec2 mouse = new Vec2(mouseX,mouseY);
            mouse.x = mouse.x;// + (ppp.x)-width/2;              mouse.y = mouse.y;// + (ppp.y)-height/2;
            sC.add(new staticCircle(mouse.x,mouse.y,r));
            sC.get(sC.size()-1).getBody().setTransform(sC.get(sC.size()-1).getBody().getPosition(),-rotation);
          }
          break;
      }

    }
    if(type==1)
    {
      dynamicS.mouseAction();
      switch(dynamicS.getShape())
      {
        case 0:
          if(mouseX>200&&mouseX<width-200&&mouseY>200)
          {
            Vec2 mouse = new Vec2(mouseX,mouseY);
            mouse.x = mouse.x;// + (ppp.x)-width/2;
            mouse.y = mouse.y;// + (ppp.y)-height/2;
            dR.add(new dynamicBodyRect(mouse.x,mouse.y,w,h));
            dR.get(dR.size()-1).getBody().setTransform(dR.get(dR.size()-1).getBody().getPosition(),-rotation);
          }
          break;
        case 1:
          if(mouseX>200&&mouseX<width-200&&mouseY>200)
          {
            Vec2 mouse = new Vec2(mouseX,mouseY);
            mouse.x = mouse.x;// + (ppp.x)-width/2;              mouse.y = mouse.y;// + (ppp.y)-height/2;
            dC.add(new dynamicCircle(mouse.x,mouse.y,r));
            dC.get(dC.size()-1).getBody().setTransform(dC.get(dC.size()-1).getBody().getPosition(),-rotation);
          }
          break;
      }
    }
  }
  // if(dR.size()>5)
  // {
  //   for(int i = 0;i<distanceJoint.size();i++)
  //   {
  //     if(distanceJoint.get(i).bd1()==dR.get(0).getBody()||distanceJoint.get(i).bd2()==dR.get(0).getBody())
  //     {
  //       Body b1 = distanceJoint.get(i).bd1();
  //       Body b2 = distanceJoint.get(i).bd2();
  //       box2d.world.destroyJoint(distanceJoint.get(i).getJoint());
  //       distanceJoint.remove(i);
  //     }
  //   }
  //   box2d.destroyBody(dR.get(0).getBody());
  //   dR.remove(0);
  // }
  // if(sBR.size()>5)
  // {
  //   box2d.destroyBody(sBR.get(0).getBody());
  //   sBR.remove(0);
  // }
}
public void mouseReleased()
{
  mJ.destroy();
}

public void keyPressed()
{
  if(key=='c')
  {
    for(distJoint j:distanceJoint)
    {
      box2d.world.destroyJoint(j.getJoint());
    }
    for(weldJoint w:wJoint)
    {
      box2d.world.destroyJoint(w.getJoint());
    }
    for(staticBodyRect b:sBR)
    {
      box2d.destroyBody(b.getBody());
    }
    for(staticCircle b:sC)
    {
      box2d.destroyBody(b.getBody());
    }
    for(dynamicBodyRect b:dR)
    {
      box2d.destroyBody(b.getBody());
    }
    for(dynamicCircle b:dC)
    {
      box2d.destroyBody(b.getBody());
    }
    sBR.clear();
    sC.clear();
    dR.clear();
    dC.clear();
    distanceJoint.clear();
    wJoint.clear();
  }
  if(key=='d')
  {
    switch(type)
    {
      case 0:
        switch(staticS.getShape())
        {
          case 0:
            w++;
            break;
          case 1:
            r++;
            break;
        }
        break;
      case 1:
        switch(dynamicS.getShape())
        {
          case 0:
            w++;
            break;
          case 1:
            r++;
            break;
        }
        break;
    }
  }
  if(key=='a')
  {
    switch(type)
    {
      case 0:
        switch(staticS.getShape())
        {
          case 0:
            w--;
            break;
          case 1:
            r--;
            break;
        }
        break;
      case 1:
        switch(dynamicS.getShape())
        {
          case 0:
            w--;
            break;
          case 1:
            r--;
            break;
        }
        break;
    }
  }
  if(key=='w')
  {
    switch(type)
    {
      case 0:
        switch(staticS.getShape())
        {
          case 0:
            h++;
            break;
          case 1:
            r++;
            break;
        }
        break;
      case 1:
        switch(dynamicS.getShape())
        {
          case 0:
            h++;
            break;
          case 1:
            r++;
            break;
        }
        break;
    }
  }
  if(key=='s')
  {
    switch(type)
    {
      case 0:
        switch(staticS.getShape())
        {
          case 0:
            h--;
            break;
          case 1:
            r--;
            break;
        }
        break;
      case 1:
        switch(dynamicS.getShape())
        {
          case 0:
            h--;
            break;
          case 1:
            r--;
            break;
        }
        break;
    }
  }
  if(key == CODED)
  {
    if(keyCode == LEFT)
    {
      rotation-=PI/24;
    }
    if(keyCode == RIGHT)
    {
      rotation+=PI/24;

    }
  }
}
public void showAll()
{
  ground.show();
  p1.show();
  c1.show();

  for(staticBodyRect b:sBR)
  {
    b.show();
  }
  for(staticCircle b:sC)
  {
    b.show();
  }
  for(dynamicBodyRect b:dR)
  {
    b.show();
  }
  for(dynamicCircle b:dC)
  {
    b.show();
  }
  for(distJoint j:distanceJoint)
  {
    j.display();
  }
  for(weldJoint w:wJoint)
  {
    w.display();
  }

}

public void select()
{
  for(int i = 0;i<sBR.size();i++)
  {
    if(sBR.get(i).contains(mouseX,mouseY))
    {
      selection.add(sBR.get(i).getBody());
    }
  }
  for(int i = 0;i<sC.size();i++)
  {
    if(sC.get(i).contains(mouseX,mouseY))
    {
      selection.add(sC.get(i).getBody());
    }
  }
  for(int i = 0;i<dR.size();i++)
  {
    if(dR.get(i).contains(mouseX,mouseY))
    {
      selection.add(dR.get(i).getBody());
    }
  }
  for(int i = 0;i<dC.size();i++)
  {
    if(dC.get(i).contains(mouseX,mouseY))
    {
      selection.add(dC.get(i).getBody());
    }
  }
  for(int i = 0;i<selection.size()-1;i++)
  {
    for(int j = i+1;j<selection.size();j++)
    {
      if(selection.get(i)==selection.get(j))
      {
        selection.remove(i);
        selection.remove(j-1);
      }
    }
  }
}
public void  colorC()
{
  if(selection.size()>0)
  {
    for(int i = 0;i<selection.size();i++)
    {
      Vec2 pos = box2d.getBodyPixelCoord(selection.get(i));
      float a = selection.get(i).getAngle();
      Fixture f = selection.get(i).getFixtureList();

      rectMode(PConstants.CENTER);
      pushMatrix();
      translate(pos.x,pos.y);
      rotate(-a);
      fill(255,0,0);
      stroke(0);
      ellipse(0,0,25,25);
      popMatrix();
    }
  }

}
public void resizeR()
{

}
public float getDensity()
{
  return dynamic.get(0).clicked();
}
public float getRestitution()
{
  return dynamic.get(1).clicked();
}
public float getFriction()
{
  return dynamic.get(2).clicked();
}
// The Nature of Code
// <http://www.shiffman.net/teaching/nature>
// Spring 2010
// Box2DProcessing example

// A fixed boundary class (now incorporates angle)

class Boundary {

  // A boundary is a simple rectangle with x,y,width,and height
  float x;
  float y;
  float w;
  float h;
  // But we also have to make a body for box2d to know about it
  Body b;

 Boundary(float x_,float y_, float w_, float h_, float a) {
    x = x_;
    y = y_;
    w = w_;
    h = h_;

    // Define the polygon
    PolygonShape sd = new PolygonShape();
    // Figure out the box2d coordinates
    float box2dW = box2d.scalarPixelsToWorld(w/2);
    float box2dH = box2d.scalarPixelsToWorld(h/2);
    // We're just a box
    sd.setAsBox(box2dW, box2dH);


    // Create the body
    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;
    bd.angle = a;
    bd.position.set(box2d.coordPixelsToWorld(x,y));
    b = box2d.createBody(bd);

    // Attached the shape to the body using a Fixture
    b.createFixture(sd,1);
  }

  // Draw the boundary, if it were at an angle we'd have to do something fancier
  public void display() {
    noFill();
    stroke(0);
    strokeWeight(1);
    rectMode(CENTER);

    float a = b.getAngle();

    pushMatrix();
    translate(x,y);
    rotate(-a);
    rect(0,0,w,h);
    popMatrix();
  }

}
public class button
{
  private int he;
  private int wi;
  private float xPos;
  private float yPos;
  private String word;
  int textSize;
  int col;

  public button(int x, int y,int w, int h,String s,int ts)
  {
    he = h;
    wi = w;
    xPos = x;
    yPos = y;
    word = s;
    textSize = ts;
    col = 210;
  }
  public boolean mouse()
  {
    //System.out.println((xPos-(wi/2))+" "+(xPos+(wi/2))+" "+(yPos-(he/2))+" "+(yPos+(he/2)));
    if(mouseX>(xPos-(wi/2)) && mouseX <(xPos+(wi/2)) && mouseY>(yPos-(he/2)) && mouseY <(yPos+(he/2)))
    {
      return true;
    }
     return false;
  }
  public void disB()
  {
    rectMode(CENTER);
    textAlign(CENTER);
    stroke(0);
    fill(col);
    rect(xPos,yPos,wi,he);
    fill(0);
    textSize(textSize);
    text(word,xPos,yPos+he/10);


    rectMode(CORNER);
  }
  public Vec2 bPos()
  {
    return new Vec2(xPos,yPos);
  }
  public void change(boolean i)
  {
    if(i)
    {
      col = 100;
    }
    else
    {
      col = 210;
    }
  }
}
class car
{

  dynamicCircle wheel1;
  dynamicBodyRect body;
  dynamicCircle wheel2;
  weldJoint wheels;

  car()
  {
    body = new dynamicBodyRect(700,700,30,10);
    wheel1 = new dynamicCircle(500,100,25);
    wheel2 = new dynamicCircle(550,100,25);

  }
  public void show()
  {
    body.show();
    wheel1.show();
    wheel2.show();
  }
  public boolean contains(float x, float y)
  {
    if(body.contains(x,y)||wheel1.contains(x,y)||wheel2.contains(x,y))
    {
      return true;
    }
    return false;
  }
  public Body getCar()
  {
    return body.getBody();
  }
  public void stopP()
  {
    body.getBody().setAwake(false);
    wheel1.getBody().setAwake(false);
    wheel2.getBody().setAwake(false);
  }
  public void startP()
  {
    body.getBody().setAwake(true);
    wheel1.getBody().setAwake(true);
    wheel2.getBody().setAwake(true);

  }
  // Body getBody()
  // {
  //   return new Body = box2d.world.createBody(bd);;
  //   // return wheel1.getBody();
  // }
  // weldJoint getWJoint()
  // {
  //   // return wheels;
  // }
}
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

  public void display() {
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
class dynamicBodyRect
{


  Body body;
  float w;
  float h;
  float col;

  dynamicBodyRect(float x_, float y_,float w_,float h_) {
    float x = x_;
    float y = y_;
    w = w_;
    h = h_;
    col = 175;

    makeBody(new Vec2(x,y),w,h);
  }


  public void killBody() {
    box2d.destroyBody(body);
  }

  public boolean contains(float x, float y) {
    Vec2 worldPoint = box2d.coordPixelsToWorld(x, y);
    Fixture f = body.getFixtureList();
    boolean inside = f.testPoint(worldPoint);

    return inside;
  }


  public void display(float a,float h_,float w_) {

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
  public void show()
  {
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();

    rectMode(PConstants.CENTER);
    pushMatrix();
    translate(pos.x,pos.y);
    rotate(-a);
    fill(col);
    stroke(0,255,0);
    rect(0,0,w,h);
    popMatrix();
  }
  public void clear()
  {
    box2d.destroyBody(body);

  }

  // This function adds the rectangle to the box2d world
  public void makeBody(Vec2 center, float w_, float h_) {
    // Define and create the body
    BodyDef bd = new BodyDef();
    bd.type = BodyType.DYNAMIC;
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
    fd.density = getDensity();
    fd.friction = getFriction();
    fd.restitution = getRestitution();

    body.createFixture(fd);


  }

  public Body getBody()
  {
    return body;
  }

}
class dynamicCircle
{
    Body body;
    float r;
    float col;
    //
    dynamicCircle(float x_, float y_,float r_)
    {
      float x = x_;
      float y = y_;
      r = r_;
      col = 175;

      makeBody(new Vec2(x,y),r);
      body.setUserData(this);

    }

    public void killBody()
    {
      box2d.destroyBody(body);
    }

    public boolean contains(float x, float y)
    {
      Vec2 worldPoint = box2d.coordPixelsToWorld(x, y);
      Fixture f = body.getFixtureList();
      boolean inside = f.testPoint(worldPoint);

      return inside;
    }


    public void display(float a,float r_)
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
    public void show()
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
      line(0, 0, r, 0);
      popMatrix();
    }
    public void clear()
    {
      box2d.destroyBody(body);

    }

    // This function adds the rectangle to the box2d world
    public void makeBody(Vec2 center, float r) {
      // Define and create the body
      BodyDef bd = new BodyDef();
      bd.type = BodyType.DYNAMIC;
      bd.position.set(box2d.coordPixelsToWorld(center));
      body = box2d.world.createBody(bd);
      // Define a polygon (this is what we use for a rectangle)
      CircleShape cs = new CircleShape();
      cs.m_radius = box2d.scalarPixelsToWorld(r);

      // Define a fixture
      FixtureDef fd = new FixtureDef();
      fd.shape = cs;
      // Parameters that affect physics
      fd.density = getDensity();
      fd.friction = getFriction();
      fd.restitution = getRestitution();
      body.createFixture(fd);
    }

    public Body getBody()
    {
      return body;
    }
}
// The Nature of Code
// <http://www.shiffman.net/teaching/nature>
// Spring 2010
// Box2DProcessing example

// Class to describe the spring joint (displayed as a line)

class mouseJ {

  // This is the box2d object we need to create
  MouseJoint mouseJoint;

  mouseJ() {
    // At first it doesn't exist
    mouseJoint = null;
  }

  // If it exists we set its target to the mouse location
  public void update(float x, float y) {
    if (mouseJoint != null) {
      // Always convert to world coordinates!
      Vec2 mouseWorld = box2d.coordPixelsToWorld(x,y);
      mouseJoint.setTarget(mouseWorld);
    }
  }

  public void display() {
    if (mouseJoint != null) {
      // We can get the two anchor points
      Vec2 v1 = new Vec2(0,0);
      mouseJoint.getAnchorA(v1);
      Vec2 v2 = new Vec2(0,0);
      mouseJoint.getAnchorB(v2);
      // Convert them to screen coordinates
      v1 = box2d.coordWorldToPixels(v1);
      v2 = box2d.coordWorldToPixels(v2);
      // And just draw a line
      stroke(0);
      strokeWeight(1);
      line(v1.x,v1.y,v2.x,v2.y);
    }
  }


  // This is the key function where
  // we attach the spring to an x,y location
  // and the Box object's location
  public void bind(float x, float y, Body box) {
    // Define the joint
    MouseJointDef md = new MouseJointDef();
    // Body A is just a fake ground body for simplicity (there isn't anything at the mouse)
    md.bodyA = box2d.getGroundBody();
    // Body 2 is the box's boxy
    md.bodyB = box;
    // Get the mouse location in world coordinates
    Vec2 mp = box2d.coordPixelsToWorld(x,y);
    // And that's the target
    md.target.set(mp);
    // Some stuff about how strong and bouncy the spring should be
    md.maxForce = 1000.0f * box.m_mass;
    md.frequencyHz = 3.0f;
    md.dampingRatio = 0.9f;

    // Make the joint!
    mouseJoint = (MouseJoint) box2d.world.createJoint(md);
  }

  public void destroy() {
    // We can get rid of the joint when the mouse is released
    if (mouseJoint != null) {
      box2d.world.destroyJoint(mouseJoint);
      mouseJoint = null;
    }
  }

}
class person
{
  dynamicCircle head;
  dynamicBodyRect body;
  dynamicBodyRect armL;
  dynamicBodyRect armLConnect;
  dynamicBodyRect armR;
  dynamicBodyRect armRConnect;
  dynamicBodyRect legL;
  dynamicBodyRect legLConnect;
  dynamicBodyRect legR;
  dynamicBodyRect legRConnect;
  revJoint legLJoint;
  revJoint legRJoint;
  revJoint armLJoint;
  revJoint armRJoint;

  weldJoint hB;
  weldJoint armRCJ;
  weldJoint armLCJ;
  weldJoint legRCJ;
  weldJoint legLCJ;

  person()
  {
    head = new dynamicCircle(500,500,10);
    body = new dynamicBodyRect(500,530,16,36);
    armLConnect = new dynamicBodyRect(508,514,5,5);
    armL = new dynamicBodyRect(480,530,20,8);
    armRConnect = new dynamicBodyRect(492,514,5,5);
    armR = new dynamicBodyRect(520,530,20,8);
    legLConnect = new dynamicBodyRect(508,548,5,5);
    legL = new dynamicBodyRect(480,530,8,20);
    legRConnect = new dynamicBodyRect(492,548,5,5);
    legR = new dynamicBodyRect(5,530,8,20);
    legLJoint = new revJoint(legL.getBody(),legLConnect.getBody(),legLConnect.getBody().getWorldCenter());
    legRJoint = new revJoint(legR.getBody(),legRConnect.getBody(),legRConnect.getBody().getWorldCenter());
    armLJoint = new revJoint(armL.getBody(),armLConnect.getBody(),armLConnect.getBody().getWorldCenter());
    armRJoint = new revJoint(armR.getBody(),armRConnect.getBody(),armRConnect.getBody().getWorldCenter());

    hB = new weldJoint(head.getBody(),body.getBody());
    armRCJ = new weldJoint(armRConnect.getBody(),body.getBody());
    armLCJ = new weldJoint(armLConnect.getBody(),body.getBody());
    legLCJ = new weldJoint(legLConnect.getBody(),body.getBody());
    legRCJ = new weldJoint(legRConnect.getBody(),body.getBody());



  }
  public void show()
  {
    head.show();
    body.show();
    armL.show();
    armR.show();
    legL.show();
    legR.show();
    armRConnect.show();
    armLConnect.show();
    legLConnect.show();
    legRConnect.show();

    //hB.display();
  }
  public boolean contains(int x,int y)
  {
    if(head.contains(x,y)||body.contains(x,y)||armL.contains(x,y)||armR.contains(x,y)||legL.contains(x,y)||legR.contains(x,y))
    {
      return true;
    }
    return false;
  }
  public Body getHead()
  {
    return head.getBody();
  }
  public void stopP()
  {
    head.getBody().setAwake(false);
    body.getBody().setAwake(false);
    armL.getBody().setAwake(false);
    armR.getBody().setAwake(false);
    legL.getBody().setAwake(false);
    legR.getBody().setAwake(false);
  }
  public void startP()
  {
    head.getBody().setAwake(true);
    body.getBody().setAwake(true);
  }
}
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

    public void display() {

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
class cValue {
  int swidth, sheight;    // width and height of bar
  float pxpos, ypos;       // x and y position of bar
  float mxpos;    // x position of slider
  float sposMin, sposMax; // max and min values of slider
  int loose;              // how loose/heavy
  boolean over;           // is the mouse over the slider?
  boolean locked;
  float ratio;
  String name;
  float inc;
  float value;

  cValue(float xp, float yp, int sw, int sh, String n,float i)
  {
    pxpos = xp;
    mxpos = xp+sw+5;
    ypos = yp;
    swidth = sw;
    sheight = sh;
    name = n;
    inc = i;
    value = .5f;
  }
  cValue(float xp, float yp, int sw, int sh, String n,float i,int v)
  {
    pxpos = xp;
    mxpos = xp+sw+5;
    ypos = yp;
    swidth = sw;
    sheight = sh;
    name = n;
    inc = i;
    value = v;
  }

  public void display()
  {
    noStroke();
    fill(255, 0 ,0 );
    rectMode(CENTER);

    rect(pxpos, ypos, swidth, sheight);
    rect(mxpos, ypos, swidth, sheight);
    fill(0);
    textSize(30);
    text("+",pxpos,ypos+8);
    text("-",mxpos,ypos+8);

    text(name,pxpos+swidth/2,ypos-sheight);
    textSize(15);
    text(value,mxpos+swidth*1.5f,ypos+8);


  }

  public float clicked()
  {
    //System.out.println((xPos-(wi/2))+" "+(xPos+(wi/2))+" "+(yPos-(he/2))+" "+(yPos+(he/2)));
    if(mouseX>(pxpos-(swidth/2)) && mouseX <(pxpos+(swidth/2)) &&
          mouseY>(ypos-(sheight/2)) && mouseY <(ypos+(sheight/2)))
    {
      value+=inc;
    }
    else if(mouseX>(mxpos-(swidth/2)) && mouseX <(mxpos+(swidth/2)) &&
          mouseY>(ypos-(sheight/2)) && mouseY <(ypos+(sheight/2)))
    {
      value-=inc;
    }
    return value;
  }

  public float getPos()
  {
    // Convert spos to be values between
    // 0 and the total width of the scrollbar
    return pxpos+10 * ratio;
  }
  public void setValue(float i)
  {
    value = i;
  }
}
abstract class shapes
{
  public abstract int shadeCol();

}
class shapeSelector
{
  int x;
  int y;
  boolean r,c,p;
  int rectC;
  int circleC;
  int polyC;
  int dhs;
  shapeSelector()
  {
    x = 50;
    y = 75;
    r = false;
    c = false;
    p = false;
    rectC = 210;
    circleC = 210;
    polyC = 210;
    dhs = -1;

  }
  public void display()
  {
    shade();
    fill(rectC);
    rect(x,y,94,26);
    fill(circleC);
    rect(x,y+30,94,26);
    fill(polyC);
    rect(x,y+60,94,26);
    fill(0);
    textSize(20);
    text("Rectangle",x+47,y+20);
    text("Circle",x+47,y+50);
    text("Polygon",x+47,y+80);
  }
  public void shade()
  {
    if(r)
    {
      rectC = 100;
      circleC = 210;
      polyC = 210;
      dhs = 0;

    }
    else if(c)
    {
      rectC = 210;
      circleC = 100;
      polyC = 210;
      dhs = 1;

    }
    else if(p)
    {
      rectC = 210;
      circleC = 210;
      polyC = 100;
      dhs = 2;

    }
  }
  public void mouseAction()
  {
    if(mouseX>=x&&mouseX<=x+94&&mouseY>y&&mouseY<y+26)
    {
      r = true;
      c = false;
      p = false;
    }
    else if(mouseX>=x&&mouseX<=x+94&&mouseY>y+30&&mouseY<y+56)
    {
      r = false;
      c = true;
      p = false;
    }
    else if(mouseX>=x&&mouseX<=x+94&&mouseY>y+60&&mouseY<y+86)
    {
      r = false;
      c = false;
      p = true;
    }
    shade();
  }
  public int getShape()
  {
    return dhs;
  }

}
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

  public void killBody()
  {
    box2d.destroyBody(body);
  }

  public boolean contains(float x, float y)
  {
    Vec2 worldPoint = box2d.coordPixelsToWorld(x, y);
    Fixture f = body.getFixtureList();
    boolean inside = f.testPoint(worldPoint);

    return inside;
  }


  public void display(float a,float h_,float w_)
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
  public void show()
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
  public void clear()
  {
    box2d.destroyBody(body);

  }

  // This function adds the rectangle to the box2d world
  public void makeBody(Vec2 center, float w_, float h_) {
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

  public Body getBody()
  {
    return body;
  }


}
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

  public void killBody()
  {
    box2d.destroyBody(body);
  }

  public boolean contains(float x, float y)
  {
    Vec2 worldPoint = box2d.coordPixelsToWorld(x, y);
    Fixture f = body.getFixtureList();
    boolean inside = f.testPoint(worldPoint);

    return inside;
  }


  public void display(float a,float r_)
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
  public void show()
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
  public void clear()
  {
    box2d.destroyBody(body);

  }

  // This function adds the rectangle to the box2d world
  public void makeBody(Vec2 center, float r) {
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

  public Body getBody()
  {
    return body;
  }


}
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

  public void display() {
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
  public void settings() {  size(1560,800);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PhysicsWorld" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
