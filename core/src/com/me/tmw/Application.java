package com.me.tmw;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.tmw.balls.Ball;
import com.me.tmw.balls.BallProgram;
import com.me.tmw.balls.Body;
import com.me.tmw.balls.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Application extends ApplicationAdapter {

	private final Program program = new BallProgram();

	@Override
	public void create () {
		program.create();
	}

	@Override
	public void render() {
		program.render();
	}

}
