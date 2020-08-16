package com.cragios.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

import com.cragios.game.graphics.Screen;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	public static final String TITLE = "Game v0.1";

	private Thread thread;
	private boolean running = false;

	private Screen screen;
	private BufferedImage img;
	private int[] pixels;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
//		setMinimumSize(size);
		// Initiate screen with display dimensions
		screen = new Screen(WIDTH, HEIGHT);
		// initiate final image that is rendered
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// Gets the writable array of pixels that make up the final image
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
		System.out.println("Working...");
	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {
		int frames = 0; // Frames
		double unprocessedSeconds = 0; // Amount of time that has not ticked
		double secondsPerTick = 1 / 60.0; // Desired time between each tick
		long previousTime = System.nanoTime();
		boolean ticked = false;
		int tickCount = 0;

		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				ticked = true;
				unprocessedSeconds -= secondsPerTick;
				tickCount++;
				if (tickCount % 60 == 0) {
					System.out.println(frames + " FPS");
					frames = 0;
				}
			}
			if (ticked) {
				render();
				frames++;
				ticked = false;
			}
		}
	}

	private void tick() {
		// Create TV static effect
		Random random = new Random();
		for (int i = 0; i < screen.test.width * screen.test.height; i++) {
			int R = random.nextInt(255), G = random.nextInt(255), B = random.nextInt(255);
			screen.test.pixels[i] = (R * 65536) + (G * 256) + B;
		}
	}

	private void render() {
		// Create BufferStrategy if null
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// Update the pixels on the screen
		screen.render();

		// Copy the screen pixels to image pixels
		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}

		// Draw the image
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.pack();
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);

		System.out.println("Running...");

		game.start();
	}

}
