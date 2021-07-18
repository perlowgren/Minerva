package net.spirangle.minerva.path;

public interface PathMap {
    int getMapWidth();
    int getMapHeight();
    int getMapStyle();
    int getMapDir(Path p,PathPoint c1,PathPoint c2);
    int getMapWeight(Path p,PathPoint fr,PathPoint to);
    int getMapHeuristic(Path p,PathPoint c);

    int movePathPoint(Path p,PathNode n,PathPoint c,int i);
    void capturePathStep(Path p,PathNode n);
}

