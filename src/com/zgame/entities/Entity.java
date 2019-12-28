package com.zgame.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.zgame.main.Game;
import com.zgame.world.Camera;

public class Entity {
	// declarações das sprites estaticas para melhor desempenho
	public static BufferedImage LIFEPACK = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage WEAPON = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage BULLET = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(9*16, 16, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(8*16, 0, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(9*16, 0, 16, 16);
	
	protected double x;
	protected double y;
	protected double z;
	
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	private int maskX, maskY, maskW, maskH;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskX = 0;
		this.maskY = 0;
		this.maskW = width;
		this.maskH = height;
	}
	
	public void setMask(int maskX, int maskY, int maskW, int maskH) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskW = maskW;
		this.maskH = maskH;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getZ() {
		return (int)this.z;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle rectE1 = new Rectangle(e1.getX() + e1.maskX, e1.getY() - e1.getZ() + e1.maskY, e1.maskW, e1.maskH);
		Rectangle rectE2 = new Rectangle(e2.getX() + e2.maskX, e2.getY() - e2.getZ() + e2.maskY, e2.maskW, e2.maskH);
		
		return rectE1.intersects(rectE2);
	}
	
	public void render(Graphics g) {
		g.drawImage(this.sprite, (int)this.x - Camera.x, (int)this.y - Camera.y, null);
		
	}
}
