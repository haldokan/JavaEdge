package org.haldokan.edge.graph;

/**
 * Edge in the graph
 * @author haldokan
 *
 * @param <V>
 */
public class Edge<V> implements IEdge<V> {
    private V v1, v2;
    private final double weight;

    public enum Direction {
	D1_2, D2_1
    };

    private Direction direction;

    public Edge(double weight) {
	this.weight = weight;
	this.direction = Direction.D1_2;
    }

    public V getStartVertex() {
	if (direction == Direction.D1_2)
	    return v1;
	else
	    return v2;
    }

    public V getEndVertex() {
	if (direction == Direction.D1_2)
	    return v2;
	else
	    return v1;
    }

    public double getWeight() {
	return weight;
    }

    @Override
    public void setV1(V v1) {
	this.v1 = v1;
    }

    @Override
    public void setV2(V v2) {
	this.v2 = v2;
    }

    public void setDirection(Direction d) {
	this.direction = d;
    }

    public Direction getDirection() {
	return direction;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
	result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Edge other = (Edge) obj;
	if (v1 == null) {
	    if (other.v1 != null)
		return false;
	} else if (!v1.equals(other.v1))
	    return false;
	if (v2 == null) {
	    if (other.v2 != null)
		return false;
	} else if (!v2.equals(other.v2))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return direction == Direction.D1_2 ? v1.toString() + v2.toString() + "(" + weight + ")" : v2.toString()
		+ v1.toString() + "(" + weight + ")";
    }
}