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
  boolean mouse()
  {
    //System.out.println((xPos-(wi/2))+" "+(xPos+(wi/2))+" "+(yPos-(he/2))+" "+(yPos+(he/2)));
    if(mouseX>(xPos-(wi/2)) && mouseX <(xPos+(wi/2)) && mouseY>(yPos-(he/2)) && mouseY <(yPos+(he/2)))
    {
      return true;
    }
     return false;
  }
  void disB()
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
  void change(boolean i)
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
