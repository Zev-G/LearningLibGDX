package com.me.tmw.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.tmw.tetris.Tetris;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "First Application";
		config.width = 800;
		config.height = 480;
//		config.fullscreen = true;

		new LwjglApplication(new Tetris(), config);
	}

}
