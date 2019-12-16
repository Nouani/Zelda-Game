package com.zgame.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Entity {
	protected double x;
	protected double y;
	
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	public Entity(int x, int y, int widght, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = widght;
		this.height = height;
		this.sprite = sprite;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
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
		g.drawImage(this.sprite, (int)this.x, (int)this.y, null);
	}
}
