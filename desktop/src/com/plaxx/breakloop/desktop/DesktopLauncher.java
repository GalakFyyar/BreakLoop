package com.plaxx.breakloop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.plaxx.breakloop.Break_Loop_DRV;

public class DesktopLauncher {
	private static final int WINDOW_WIDTH = 11*48;
	private static final int WINDOW_HEIGHT = 7*48;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Break Loop";
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		new LwjglApplication(new Break_Loop_DRV(), config);
	}
}
