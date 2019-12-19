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
	private static final int MAX_FRAMES = 5, // quantidade máxima de vezes para troca de animacao
							 MAX_INDEX = 1; // quantidade de sprites pertencentes a animacao
	
	private BufferedImage[] spritesEnemy;

	public Enemy(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
		
		this.spritesEnemy = new BufferedImage[2];
		for (int i = 0; i < this.spritesEnemy.length; i++) {
			this.spritesEnemy[i] = Game.spritesheet.getSprite(16*(i+7), 16, 16, 16);
		}
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

		this.frames++; // soma quantidade de vezes que esta em movimento
		if (this.frames == Enemy.MAX_FRAMES) { // se a quantidade de vezes for igual a maxima para troca de sprite
			this.frames = 0; // zera
			this.index++; // soma indice do vetor de sprite para tal lado
			if (this.index > Enemy.MAX_INDEX) { // se indice é maior que a quantidade total de sprites para animacao
				index = 0; // volta para primeira sprite
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
		g.drawImage(this.spritesEnemy[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}
