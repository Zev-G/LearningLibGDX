package com.me.tmw;

import com.badlogic.gdx.Gdx;

import java.util.List;
import java.util.function.Supplier;

public abstract class Body {

    // Two dimension position, x and y.
    protected Vector pos = Vector.ZERO;

    // Two dimensional vectors, x/s, y/s
    protected Vector vel = Vector.ZERO;
    protected Vector accel = Vector.ZERO;
    protected Vector maxVel = null;
    protected Vector minVel = null;

    public Vector getVelocity() {
        return vel;
    }

    public Vector getAcceleration() {
        return accel;
    }

    public void setVelocity(Vector vel) {
        this.vel = vel;
    }

    public void setAcceleration(Vector accel) {
        this.accel = accel;
    }

    public void setMaxVelocity(Vector maxVel) {
        this.maxVel = maxVel;
    }

    public void setMinVelocity(Vector minVel) {
        this.minVel = minVel;
    }

    public void setRelativeTo(Vector vector) {
        setRelativeTo(() -> vector);
    }
    public void setRelativeTo(Supplier<Vector> relativeTo) {
        this.relativeTo = relativeTo;
    }
    protected Supplier<Vector> relativeTo = () -> Vector.ZERO;

    public float getX() {
        return pos.getX() + relativeTo.get().getX();
    }

    public float getY() {
        return pos.getY() + relativeTo.get().getY();
    }
    public Vector getRelativePos() {
        return new Vector(getX(), getY());
    }

    public void update(long ms) {
        float s = ms / 1000f;

        Vector relativeTo = this.relativeTo.get();
        Vector relativePos = getRelativePos();

        if (relativePos.getY() > Gdx.graphics.getHeight() || relativePos.getY() < 0) {
            vel = new Vector(vel.getX(), -vel.getY());
        }
        if (relativePos.getX() > Gdx.graphics.getWidth() || relativePos.getX() < 0) {
            vel = new Vector(-vel.getX(), vel.getY());
        }

        // d = vit + 1/2at^2
        Vector displacement = getVelocity().add(new Vector(0.5f).multiply(getAcceleration()).multiply(new Vector(s*s)));

        pos = pos.add(displacement);

        Vector newVel = getVelocity().add(getAcceleration());
        if (maxVel != null) {
            newVel = new Vector(Math.min(newVel.getX(), maxVel.getX()), Math.min(newVel.getY(), maxVel.getY()));
        }
        if (minVel != null) {
            newVel = new Vector(Math.max(newVel.getX(), minVel.getX()), Math.max(newVel.getY(), minVel.getY()));
        }
        vel = newVel;
    }

}
