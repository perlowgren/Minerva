package net.spirangle.minerva.path;

public abstract class VertHexMap implements PathMap {
    private static final short[][] xcoords = {{  0, 0, 0, 0,-1,-1 },{  0, 1, 1, 0, 0, 0 }};
    private static final short[] ycoords = { -2,-1, 1, 2, 1,-1 };

    @Override
    public int getMapDir(Path p,PathPoint c1,PathPoint c2) {
        PathPoint p1 = new PathPoint(c1),p2 = new PathPoint(c2);
        p.adjustDir(p1,p2);
        p2.x -= p1.x+(p1.y&1);
        if(Math.abs(p2.y-p1.y)==2) return p2.y<p1.y? Path.N : Path.S;
        return p2.y<p1.y? (p2.x<0? Path.NW : Path.NE) : (p2.x<0? Path.SW : Path.SE);
    }

    @Override
    public int getMapHeuristic(Path p,PathPoint c) {
        PathPoint p1 = new PathPoint(c),p2 = p.getDestination();
        p.adjustDir(p1,p2);
        p1.x = (short)(Math.abs(p1.x-p2.x)*2);
        p1.y = (short)Math.abs(p1.y-p2.y);
        return p1.x>p1.y? p1.x+p1.y/2 : p1.y+p1.x/2;
    }

    @Override
    public int movePathPoint(Path p,PathNode n,PathPoint c,int i) {
        c.set(n.x+xcoords[n.y&1][i],n.y+ycoords[i]);
        p.adjustMove(c);
        return i<5? 1 : -1;
    }
}

