package com.main;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {

	public static final int SIZE = 16;

	private int x, y;
	private int xVel, yVel;
	private double speed = 5;

	public Ball() {
		reset();
	}

	private void reset() {

		x = Game.WIDTH / 2 - SIZE / 2;
		y = Game.HEIGHT / 2 - SIZE / 2;

		xVel = Game.sign(Math.random() * 2.0 - 1);
		yVel = Game.sign(Math.random() * 2.0 - 1);
	}

	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, SIZE, SIZE);
	}

	public void update(Paddle lp, Paddle rp) {

		x += xVel * speed;
		y += yVel * speed;

		if (y + SIZE >= Game.HEIGHT || y <= 0)
			changeYDir();

		if (x + SIZE >= Game.WIDTH) {
			lp.addPoint();
			this.speed = this.speed + (0.5);
			reset();
		}
		if (x <= 0) {
			rp.addPoint();
			this.speed = this.speed + (0.5);
			reset();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void changeXDir() {
		xVel *= -1;
	}

	public void changeYDir() {
		yVel *= -1;
	}

}
