package com.me.tmw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball extends Shape {

    private int radii = 50;

    public Ball(ShapeRenderer shape) {
        super(shape);
    }

    @Override
    public void render() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        float width = Gdx.graphics.getWidth() / 2f;
        float height = Gdx.graphics.getHeight() / 2f;
        float x = pos.getX();
        float y = pos.getY();
        double maxDist = Math.sqrt(width * width + height * height);
        float dist = 1f - (float) (Math.sqrt(x * x + y * y) / maxDist);
        Vector pos = getRelativePos();
        shape.setColor(hsvToRgb(dist, dist, 1));
        shape.circle(pos.getX(), pos.getY(), radii);
        shape.end();
    }

    public static Color hsvToRgb(float hue, float saturation, float value) {

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return new Color(value, t, p, 1);
            case 1: return new Color(q, value, p, 1);
            case 2: return new Color(p, value, t, 1);
            case 3: return new Color(p, q, value, 1);
            case 4: return new Color(t, p, value, 1);
            case 5: case 6: return new Color(value, p, q, 1);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    public int getRadii() {
        return radii;
    }

    public void setRadii(int radii) {
        this.radii = radii;
    }

}
