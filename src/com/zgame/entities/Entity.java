package com.zgame.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Entity {
	private int x;
	private int y;
	
	private int width;
	private int height;
	
	private BufferedImage sprite;
	
	public Entity(int x, int y, int widght, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = widght;
		this.height = height;
		this.sprite = sprite;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.drawImage(this.sprite, this.x, this.y, null);
	}
}
