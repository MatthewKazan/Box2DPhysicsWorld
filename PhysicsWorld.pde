import shiffman.box2d.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
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

ArrayList<cValue> dynamic;
ArrayList<cValue> staticB;

ArrayList<cValue> distance;
cValue gravity;

void setup() {
  size(1560,800);
  smooth();

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
  dynamic.add(new cValue(70, 400, 30, 30, "Restitution",.1));
  dynamic.add(new cValue(70, 500, 30, 30, "Friction",.1));

  staticB.add(new cValue(70, 500, 30, 30, "Friction",.1));


  distance.add(new cValue(80, 300, 30, 30, "Length",1));
  distance.add(new cValue(80, 400, 30, 30, "FrequencyHz",.1));
  distance.add(new cValue(80, 500, 30, 30, "Damping",.1));

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
  // earth = new button(width/2+300,125,30,30,"√",20);
  scale = new button(100,750,(int)box2d.scalarWorldToPixels(10),20,"10 Meters",15);
  move= new button(width/2+300,125,80,40,"Move",30);

  boundaries = new ArrayList();
  boundaries.add(new Boundary(width/2,height-5,width,10,0));
  boundaries.add(new Boundary(width-5,height/2,10,height*10,0));
  boundaries.add(new Boundary(200,height/2,10,height*10,0));



  grav = -9.8;
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


void draw()
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




void changeType()
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
void changeValue()
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
void mousePressed()
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
void mouseReleased()
{
  mJ.destroy();
}

void keyPressed()
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
void showAll()
{
  ground.show();

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

void select()
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
void  colorC()
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
void resizeR()
{

}
float getDensity()
{
  return dynamic.get(0).clicked();
}
float getRestitution()
{
  return dynamic.get(1).clicked();
}
float getFriction()
{
  return dynamic.get(2).clicked();
}
