package org.haldokan.edge.interviewquest.google;

import java.util.HashMap;
import java.util.Map;

/**
 * Not resolved yet.
 * <p>
 * You are given a list of n float numbers x_1, x_2, x_3, ... x_n, where x_i > 0.
 * A traveler starts at point (0, 0) and moves x_1 metres to the north, then x_2 metres to the west, x_3 to the south, x_4 to the east and so on (after each lastMove his direction changes counter-clockwise)
 * Write an single-pass algorithm that uses O(1) memory to determine, if the travelers path crosses itself, or not (i.e. if it's self-intersecting)
 * e.g.
 * 2 1 1 2 -> crosses
 * 1 2 3 4 -> doesn't cross
 * <p>
 * Created by haytham.aldokanji on 9/8/15.
 */
public class SelfIntersectingPath {
    public Map<Direction, Edge> edgeByDirection = new HashMap<>();

    public static void main(String[] args) {
        SelfIntersectingPath driver = new SelfIntersectingPath();
    }

    public boolean move(Direction dir, float x1, float y1, float x2, float y2) {
        Edge edge = new Edge(x1, y1, x2, y2);
        return false;
    }

    public enum Direction {N, S, E, W}

    public enum EdgeType {HORIZONTAL, VERTICAL}

    private static class Edge {
        private float x1, y1, x2, y2;
        private EdgeType type;

        public Edge(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.type = getEdgeType();
        }

        private EdgeType getEdgeType() {
            if (x1 == x2)
                return EdgeType.VERTICAL;
            else return EdgeType.HORIZONTAL;
        }

        public boolean intersect(Edge edge) {
            if (this.type == edge.type) {
                return false;
            } else {
                // TODO CAN be made more concise
                return x1 >= edge.x1 && y1 >= edge.y1 && x2 <= edge.x2 && y2 <= edge.y2 ||
                        x1 <= edge.x1 && y1 <= edge.y1 && x2 >= edge.x2 && y2 >= edge.y2;
            }
        }
    }
}
