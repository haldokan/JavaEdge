package org.haldokan.edge.graph;

/**
 * Vertex in the graph
 *
 * @param <E>
 * @author haldokan
 */
public class Vertex<E> {
    private final E id;
    private final double x, y;

    public Vertex(E id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public E getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Vertex<?> other = (Vertex<?>) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "V" + id;
    }

}
