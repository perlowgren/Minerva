package net.spirangle.minerva.path;

public abstract class HorizHexMap implements PathMap {
    private static final short[][] xcoords = {{  0, 1, 0,-1,-1,-1 },{  1, 1, 1, 0,-1, 0 }};
    private static final short[] ycoords = { -1, 0, 1, 1, 0,-1 };

    @Override
    public int getMapDir(Path p,PathPoint c1,PathPoint c2) {
        PathPoint p1 = new PathPoint(c1),p2 = new PathPoint(c2);
        p.adjustDir(p1,p2);
        p2.y -= p1.y+(p1.x&1);
        if(Math.abs(p2.x-p1.x)==2) return p2.x<p1.x? Path.W : Path.E;
        return p2.x<p1.x? (p2.y<0? Path.NW : Path.SW) : (p2.y<0? Path.NE : Path.SE);
    }

    @Override
    public int getMapHeuristic(Path p,PathPoint c) {
        PathPoint p1 = new PathPoint(c),p2 = p.getDestination();
        p.adjustDir(p1,p2);
        p1.y = (short)(Math.abs(p1.y-p2.y)*2);
        p1.x = (short)Math.abs(p1.x-p2.x);
        return p1.y>p1.x? p1.y+p1.x/2 : p1.x+p1.y/2;
    }

    @Override
    public int movePathPoint(Path p,PathNode n,PathPoint c,int i) {
        c.set(n.x+xcoords[n.y&1][i],n.y+ycoords[i]);
        p.adjustMove(c);
        return i<5? 1 : -1;
    }
}

