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
  void display()
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
  void shade()
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
  void mouseAction()
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
  int getShape()
  {
    return dhs;
  }

}
