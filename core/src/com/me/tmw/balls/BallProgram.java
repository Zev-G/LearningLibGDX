package com.me.tmw.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.tmw.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BallProgram implements Program {

    public static final boolean SPOTLIGHT = false;

    ShapeRenderer shape;

    List<Body> bodies = new ArrayList<>();

    @Override
    public void create () {
        shape = new ShapeRenderer();

        Supplier<Vector> relativeTo = () -> new Vector(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

        float maxVel = 5;
        for (int balls = 0; balls < 7500; balls++) {
            Ball ball = new Ball(shape);
            ball.setRelativeTo(relativeTo);
            if (!SPOTLIGHT) {
                ball.setRadii((int) (Math.abs(randomNumber()) * 5));
            }
//			ball.setMaxVelocity(new Vector(maxVel));
//			ball.setMinVelocity(new Vector(-maxVel));
            ball.setAcceleration(new Vector(randomNumber() / 20, randomNumber() / 20));
            bodies.add(ball);
        }
    }

    private float randomNumber() {
        return (float) (Math.random() - 0.5);
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        long time = (long) (Gdx.graphics.getDeltaTime() * 1000);
        for (Body body : bodies) {
            body.update(time);
        }
    }

}
