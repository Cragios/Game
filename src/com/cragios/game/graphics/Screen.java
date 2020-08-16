package com.cragios.game.graphics;

public class Screen extends Render {

	public Render test;

	public Screen(int width, int height) {
		// The screen is just a big render
		super(width, height);

		test = new Render(256, 256);

	}

	public void render() {
		// Make all pixels black
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}

		// Render the render
		draw(test, (width - test.width) / 2, (height - test.height) / 2);
	}

}
