package com.cragios.game.graphics;

import com.cragios.game.Display;

public class Render {

	public final int width;
	public final int height;
	public final int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	public void draw(Render render, int xOffset, int yOffset) {
		for (int y = 0; y < render.width; y++) {
			int yPix = y + yOffset;
			// Ignore OutOfBounds Exception
			if (yPix < 0 || yPix >= Display.HEIGHT)
				continue;
			for (int x = 0; x < render.width; x++) {
				int xPix = x + xOffset;
				// Ignore OutOfBounds Exception
				if (xPix < 0 || xPix >= Display.WIDTH)
					continue;

				// Copy the render pixels to screen pixels
				int alpha = render.pixels[(y * render.width) + x];
				if (alpha > 0)
					pixels[(yPix * width) + xPix] = alpha;
			}

		}
	}

}
