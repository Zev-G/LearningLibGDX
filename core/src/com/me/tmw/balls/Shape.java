package com.me.tmw.balls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Shape extends Body implements Renderable {

    protected final ShapeRenderer shape;

    protected Color color = null;

    public Shape(ShapeRenderer shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void update(long ms) {
        super.update(ms);
        if (color != null) {
            shape.setColor(color);
        }
        render();
    }

}
