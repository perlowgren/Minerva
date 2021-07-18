package net.spirangle.minerva.path;

public class Trail {
    public static class Step {
        public short x;
        public short y;
        public byte dir;

        public Step(int x,int y,int dir) {
            this.x = (short)x;
            this.y = (short)y;
            this.dir = (byte)dir;
        }
    }

    protected Step[] trail;
    protected int ind;
    protected int len;
    protected Object obj;

    public Trail() {
        trail = null;
        ind = 0;
        len = 0;
        obj = null;
    }

    public Trail(Object o,int l) {
        trail = new Step[l];
        ind = 0;
        len = l;
        obj = o;
    }

    public void setStep(int i,int x,int y,int dir) {
        trail[i] = new Step(x,y,dir);
    }

    public Step[] getSteps() {
        return trail;
    }

    public void setSteps(Step[] s,int l,int i) {
        trail = s;
        ind = i;
        len = l;
    }

    public int getX() {
        return ind<len? trail[ind].x : -1;
    }

    public int getY() {
        return ind<len? trail[ind].y : -1;
    }

    public int getDir() {
        return ind<len? trail[ind].dir : -1;
    }

    public Object getObject() {
        return obj;
    }

    public int index() {
        return ind;
    }

    public int setIndex(int i) {
        return ind = i>=0 && i<len? i : 0;
    }

    public void first() {
        ind = 0;
    }

    public void next() {
        if(ind<len-1) ++ind;
    }

    public void previous() {
        if(ind>0) --ind;
    }

    public void last() {
        if(len>0) ind = len-1;
    }

    public int length() {
        return len;
    }

    public int steps() {
        return len-ind;
    }

    public boolean hasMoreSteps() {
        return ind<len-1;
    }
}

