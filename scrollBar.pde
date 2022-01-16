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
    value = .5;
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

  void display()
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
    text(value,mxpos+swidth*1.5,ypos+8);


  }

  float clicked()
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

  float getPos()
  {
    // Convert spos to be values between
    // 0 and the total width of the scrollbar
    return pxpos+10 * ratio;
  }
  void setValue(float i)
  {
    value = i;
  }
}
