package com.KvasicOkicIgra.PticaSkakalica;

import com.badlogic.gdx.Game;

public class FlappyBird extends Game {
	@Override
	public void create() {
		setScreen(new MainMenuScreen(this));
	}
}
