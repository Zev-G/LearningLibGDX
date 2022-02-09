package com.me.tmw.balls;

import java.util.Collection;
import java.util.Objects;

public class Vector {

    public static Vector ZERO = new Vector(0);
    public static Vector ONE = new Vector(1);

    private final float x;
    private final float y;

    public Vector(float xy) {
        this(xy, xy);
    }
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector sum(Collection<Vector> vectors) {
        if (vectors.isEmpty()) return ZERO;

        float x = 0;
        float y = 0;
        for (Vector vector : vectors) {
            x += vector.x;
            y += vector.y;
        }
        return new Vector(x, y);
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }
    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }
    public Vector multiply(Vector other) {
        return new Vector(x * other.x, y * other.y);
    }
    public Vector divide(Vector other) {
        return new Vector(x / other.x, y / other.y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
