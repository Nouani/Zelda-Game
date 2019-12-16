package com.zgame.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zgame.main.Game;
import com.zgame.world.Camera;
import com.zgame.world.World;

public class Player extends Entity{
	public boolean right, up, left, down;
	public int rightDir = 0, leftDir = 1, upDir = 2, downDir = 3;
	public int dir = rightDir;
	public double speed = 1.0;
	
	private int frames = 0, index;
	private static final int MAX_FRAMES = 5, MAX_INDEX = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;

	public Player(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
		
		this.rightPlayer = new BufferedImage[4];
		this.leftPlayer = new BufferedImage[4];
		this.upPlayer = new BufferedImage[4];
		this.downPlayer = new BufferedImage[4];
		
		for (int i = 0; i < rightPlayer.length; i++) {
			this.rightPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 0, 16, 16);
			this.leftPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 16, 16, 16);
			this.upPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 32, 16, 16);
			this.downPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 48, 16, 16);
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
			dir = upDir;
			this.y -= this.speed;
		} 
		else if (down) {
			moved = true;
			dir = downDir;
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
		
		Camera.x = Camera.clamp((this.getX() - (Game.WIDTH/2)), 0, ((World.WIDTH*16) - Game.WIDTH));
		Camera.y = Camera.clamp((this.getY() - (Game.HEIGHT/2)), 0, ((World.HEIGHT*16) - Game.HEIGHT));
	}
	
	public void render(Graphics g) {
		if (dir == rightDir) {
			g.drawImage(this.rightPlayer[this.index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		} else if (dir == leftDir) {
			g.drawImage(this.leftPlayer[this.index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		} else if (dir == upDir) {
			g.drawImage(this.upPlayer[this.index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		} else if (dir == downDir) {
			g.drawImage(this.downPlayer[this.index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		}
	}
}
