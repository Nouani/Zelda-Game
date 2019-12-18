package com.zgame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.zgame.main.Game;
import com.zgame.world.Camera;
import com.zgame.world.World;

public class Enemy extends Entity{
	private int speed = 1;
	private int maskX = 8, maskY = 8,
			 	maskW = 8, maskH = 8;

	public Enemy(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
	}
	
	public void tick() {
		if (Game.rand.nextInt(100) < 70) {
			if (this.x < Game.player.getX() && World.isFree((int)(this.x + this.speed), this.getY()) && !this.isColliding((int)(this.x + this.speed), this.getY())) {
				this.x += this.speed;
			} else if (this.x > Game.player.getX() && World.isFree((int)(this.x - this.speed), this.getY()) && !this.isColliding((int)(this.x - this.speed), this.getY())) {
				this.x -= this.speed;
			}
			
			if (this.y < Game.player.getY() && World.isFree(this.getX(), (int)(this.y + this.speed)) && !this.isColliding(this.getX(), (int)(this.y + this.speed))) {
				this.y += this.speed;
			} else if (this.y > Game.player.getY() && World.isFree(this.getX(), (int)(this.y - this.speed)) && !this.isColliding(this.getX(), (int)(this.y - this.speed))) {
				this.y -= this.speed;
			}
		}
	}
	
	public boolean isColliding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext + this.maskX, yNext + this.maskY, this.maskW, this.maskH);
		for (Enemy e : Game.enemies) {
			if (e == this)
				continue;
			Rectangle enemyTarget = new Rectangle(e.getX() + this.maskX, e.getY() + this.maskY, this.maskW, this.maskH);
			if (enemyCurrent.intersects(enemyTarget))
				return true;
		}
		return false;
	}
	
	public void render(Graphics g) {
		super.render(g);
		//g.setColor(Color.BLUE);
		//g.fillRect(this.getX() + this.maskX - Camera.x, this.getY() + this.maskY - Camera.y, this.maskW, this.maskH);
	}
}
