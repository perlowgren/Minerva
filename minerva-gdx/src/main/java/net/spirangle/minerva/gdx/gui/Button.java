package net.spirangle.minerva.gdx.gui;

public class Button extends Widget {

    public Button(int i,int st,int x,int y,int w,int h) {
        super(i,st,x,y,w,h);
    }

    @Override
    public void draw() {
        screen.drawBox(x,y,width,height,1);
    }
}
