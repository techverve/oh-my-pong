package com.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.time.LocalTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class KeyInput extends KeyAdapter {

	private Paddle lp;
	private boolean lup = false;
	private boolean ldown = false;

	private Paddle rp;
	private boolean rup = false;
	private boolean rdown = false;

	public KeyInput(Paddle p1, Paddle p2) {

		lp = p1;
		rp = p2;

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_UP) {
			rp.switchDirections(-1);
			rup = true;
		}
		if (key == KeyEvent.VK_DOWN) {
			rp.switchDirections(1);
			rdown = true;
		}
		if (key == KeyEvent.VK_W) {
			lp.switchDirections(-1);
			lup = true;
		}
		if (key == KeyEvent.VK_S) {
			lp.switchDirections(1);
			ldown = true;

		}

		// exit
		if (key == KeyEvent.VK_ESCAPE) {
			try {
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pong_database", "root",
						"dailydue");

				String query = "INSERT INTO player_data values('" + LocalTime.now() + "','" + lp.score + "','"
						+ rp.score + "')";
				System.out.println(LocalTime.now());
				Statement sta = connection.createStatement();
				int x = sta.executeUpdate(query);
				connection.close();
			} catch (Exception exception) {
				System.out.println(exception);
			}
			System.exit(1);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_UP) {
			// rp.stop();
			rup = false;
		}
		if (key == KeyEvent.VK_DOWN) {
			// rp.stop();
			rdown = false;
		}
		if (key == KeyEvent.VK_W) {
			// lp.stop();
			lup = false;
		}
		if (key == KeyEvent.VK_S) {
			// lp.stop();
			ldown = false;
		}
		if (!lup && !ldown)
			lp.stop();
		if (!rup && !rdown)
			rp.stop();

	}

}
