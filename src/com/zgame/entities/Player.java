package com.zgame.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity{
	public boolean right, up, left, down;
	
	public double speed = 0.7;

	public Player(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
	}
	
	public void tick() {
		if (right) {
			this.x += this.speed;
		} else if (left) {
			this.x -= this.speed;
		}
		
		if (up) {
			this.y -= this.speed;
		} else if (down) {
			this.y += this.speed;
		}
	}

}
