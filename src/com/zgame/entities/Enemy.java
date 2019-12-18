package com.zgame.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zgame.main.Game;
import com.zgame.world.Camera;
import com.zgame.world.World;

public class Enemy extends Entity{
	private int speed = 1;

	public Enemy(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
	}
	
	public void tick() {
		if (this.x < Game.player.getX() && World.isFree((int)(this.x + this.speed), this.getY())) {
			this.x += this.speed;
		} else if (this.x > Game.player.getX() && World.isFree((int)(this.x - this.speed), this.getY())) {
			this.x -= this.speed;
		}
		
		if (this.y < Game.player.getY() && World.isFree(this.getX(), (int)(this.y + this.speed))) {
			this.y += this.speed;
		} else if (this.y > Game.player.getY() && World.isFree(this.getX(), (int)(this.y - this.speed))) {
			this.y -= this.speed;
		}
	}
}
