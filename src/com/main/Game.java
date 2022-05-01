package com.main;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public final static int WIDTH = 1000;
	public final static int HEIGHT = WIDTH * 9 / 16;

	public boolean running = false;
	private Thread gameThread;
	private Ball ball;

	private Paddle leftPaddle;
	private Paddle rightPaddle;

	private MainMenu menu;

	public Game() {

		canvasSetup();

		new Window("Simple Pong", this);

		initialise();

		this.addKeyListener(new KeyInput(leftPaddle, rightPaddle));
		this.addMouseListener(menu);
		this.addMouseMotionListener(menu);
		this.setFocusable(true);

	}

	private void initialise() {
		ball = new Ball();
		leftPaddle = new Paddle(Color.green, true);
		rightPaddle = new Paddle(Color.red, false);

		menu = new MainMenu(this);
	}

	private void canvasSetup() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
	}

	@Override
	public void run() {

		this.requestFocus();

		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				delta--;
				draw();
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}

		stop();
	}

	public synchronized void start() {
		gameThread = new Thread(this);
		gameThread.start();
		running = true;
	}

	public void stop() {
		try {
			gameThread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void draw() {

		BufferStrategy buffer = this.getBufferStrategy();

		if (buffer == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = buffer.getDrawGraphics();
		drawBackground(g);

		if (menu.active)
			menu.draw(g);

		ball.draw(g);

		leftPaddle.draw(g);
		rightPaddle.draw(g);

		g.dispose();
		buffer.show();

	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.white);
		Graphics2D g2d = (Graphics2D) g;

		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10 }, 0);
		g2d.setStroke(dashed);
		g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
	}

	public void update() {

		if (!menu.active) {

			ball.update(leftPaddle, rightPaddle);

			leftPaddle.update(ball);
			rightPaddle.update(ball);
		}
	}

	public static int ensureRange(int value, int min, int max) {
		return Math.min(Math.max(value, min), max);
	}

	public static int sign(double d) {
		if (d <= 0)
			return -1;

		return 1;
	}

	public static void main(String[] args) {
		new Game();
	}

}
