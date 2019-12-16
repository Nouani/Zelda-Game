package com.zgame.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zgame.main.Game;

public class Player extends Entity{
	public boolean right, up, left, down;
	public int rightDir = 0, leftDir = 1;
	public int dir = rightDir;
	public double speed = 0.7;
	
	private int frames = 0, index;
	private static final int MAX_FRAMES = 5, MAX_INDEX = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;

	public Player(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
		
		this.rightPlayer = new BufferedImage[4];
		this.leftPlayer = new BufferedImage[4];
		
		for (int i = 0; i < rightPlayer.length; i++) {
			this.rightPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 0, 16, 16);
			this.leftPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 16, 16, 16);
		}
	}
	
	public void tick() {
		moved = false;
		if (right) {
			moved = true;
			dir = rightDir;
			this.x += this.speed;
		} 
		else if (left) {
			moved = true;
			dir = leftDir;
			this.x -= this.speed;
		}
		
		if (up) {
			moved = true;
			this.y -= this.speed;
		} 
		else if (down) {
			moved = true;
			this.y += this.speed;
		}
		
		if (moved) {
			this.frames++;
			if (this.frames == Player.MAX_FRAMES) {
				this.frames = 0;
				this.index++;
				if (this.index > Player.MAX_INDEX) {
					index = 0;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if (dir == rightDir) {
			g.drawImage(this.rightPlayer[this.index], (int)this.getX(), (int)this.getY(), null);
		} else if (dir == leftDir) {
			g.drawImage(this.leftPlayer[this.index], (int)this.getX(), (int)this.getY(), null);
		}
	}
}
