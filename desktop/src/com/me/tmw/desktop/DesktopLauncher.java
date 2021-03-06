package com.me.tmw.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.tmw.Application;
import com.me.tmw.balls.BallProgram;
import com.me.tmw.tetris.Tetris;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "First Application";
		config.width = 800;
		config.height = 800;
		config.fullscreen = true;

		new LwjglApplication(new Application(), config);
	}

}
