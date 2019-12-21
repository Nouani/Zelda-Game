package com.zgame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zgame.main.Game;
import com.zgame.world.Camera;

public class BulletShoot extends Entity{
	private double dx, dy;
	private double speed = 4;
	
	private int currentLife = 0;
	private static final int MAX_LIFE = 10;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		this.x += this.dx*this.speed;
		this.y += this.dy*this.speed;
		this.currentLife++;
		if (this.currentLife == BulletShoot.MAX_LIFE) {
			//Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, this.width, this.height);
	}

}
