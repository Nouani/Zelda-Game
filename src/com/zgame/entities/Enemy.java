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
	
	private int frames = 0, // quantidade de vezes que está ocorrendo um movimento
			    index; // qual o indice da sprite a ser capturada no momento
	private int maxFrames = Game.rand.nextInt(20)+20; // quantidade máxima de vezes para troca de animacao
	private static final int MAX_INDEX = 1;
	
	private BufferedImage[] spritesEnemy;
	
	private int life = 10;
	
	private boolean isDamaged = false;
	private int damageFrames = 0;

	public Enemy(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
		
		this.spritesEnemy = new BufferedImage[2];
		for (int i = 0; i < this.spritesEnemy.length; i++) {
			this.spritesEnemy[i] = Game.spritesheet.getSprite(16*(i+7), 16, 16, 16);
		}
	}
	
	public void tick() {
		if (Game.rand.nextInt(100) < 70) {
			if (!this.isCollidingWithPlayer()) {
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
			} else {
				if (Game.rand.nextInt(100) < 10) {
					Game.player.life -= Game.rand.nextInt(5);
					Game.player.isDamaged = true;
				}
			}
		}

		this.frames++; // soma quantidade de vezes que esta em movimento
		if (this.frames == this.maxFrames) { // se a quantidade de vezes for igual a maxima para troca de sprite
			this.frames = 0; // zera
			this.index++; // soma indice do vetor de sprite para tal lado
			if (this.index > Enemy.MAX_INDEX) { // se indice é maior que a quantidade total de sprites para animacao
				index = 0; // volta para primeira sprite
			}
		}
		
		this.collidingBullet();
		if (this.life == 0) {
			this.destroySelf();
			return;
		}
		
		if (this.isDamaged) {
			this.damageFrames++;
			if (this.damageFrames == 10) {
				this.damageFrames = 0;
				this.isDamaged = false;
			}
		}
	}
	
	public void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}
	
	public void collidingBullet() {
		for (Entity e : Game.bullets) {
			if (Entity.isColliding(this, e)) {
				this.life--;
				this.isDamaged = true;
				Game.bullets.remove(e);
				return;
			}
		}
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + this.maskX, this.getY()+ this.maskY, this.maskW, this.maskH);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY() - Game.player.getZ(), Game.player.getWidth(), Game.player.getHeight());
		
		return enemyCurrent.intersects(player);
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
		if (!this.isDamaged) {
			g.drawImage(this.spritesEnemy[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null);	
		} else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
