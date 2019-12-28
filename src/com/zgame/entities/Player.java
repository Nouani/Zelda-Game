package com.zgame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.zgame.graficos.Spritesheet;
import com.zgame.main.Game;
import com.zgame.main.Sound;
import com.zgame.world.Camera;
import com.zgame.world.World;

public class Player extends Entity{
	public boolean right, up, left, down; // para verificar se foi apertada
	public int rightDir = 0, leftDir = 1, upDir = 2, downDir = 3; // codigo de cada tecla 
	public int dir = rightDir; // codigo da tecla apertada no momento
	public double speed = 1.5; // velocidade do jogador
	
	private int frames = 0, // quantidade de vezes que está ocorrendo um movimento
				index; // qual o indice da sprite a ser capturada no momento
	private static final int MAX_FRAMES = 5, // quantidade máxima de vezes para troca de animacao
							 MAX_INDEX = 3; // quantidade de sprites pertencentes as animações de cada lado do jogador
	private boolean moved = false;
	
	// sprite do jogador
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage playerDamage;
	
	public static final int MAX_LIFE = 100;
	public double life = Player.MAX_LIFE;

	public int ammo = 0;
	
	public boolean hasGun = false;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public boolean keyShoot = false,
				   mouseShoot = false;
	public int mx, my;
	
	public boolean jump = false;
	private boolean isJumping = false,
					jumpUp = false, jumpDown = false;
	private int jumpFrames = 25, jumpCurrent = 0,
				jumpSpeed = 2;
	
	private String ultimaDirecao = "frente";

	public Player(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
		
		// inicializando os objetos
		this.rightPlayer = new BufferedImage[4];
		this.leftPlayer = new BufferedImage[4];
		this.upPlayer = new BufferedImage[4];
		this.downPlayer = new BufferedImage[4];
		this.playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for (int i = 0; i < rightPlayer.length; i++) { // adicionando dinamicamente todas as sprites
			this.rightPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 0, 16, 16);
			this.leftPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 16, 16, 16);
			this.upPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 32, 16, 16);
			this.downPlayer[i] = Game.spritesheet.getSprite(32+(16*i), 48, 16, 16);
		}
	}
	
	public void tick() { 
		moved = false; // ainda não esta em movimento
		
		if (this.jump) {
			if (!this.isJumping) {
				this.jump = false;
				this.isJumping = true;
				this.jumpUp = true;
			}
		}
		if (this.isJumping) {
				if (this.jumpUp) {
					this.jumpCurrent += this.jumpSpeed;
					if (this.jumpCurrent >= this.jumpFrames) {
						this.jumpUp = false;
						this.jumpDown = true;
					}
				} else if (this.jumpDown) {
					this.jumpCurrent -= this.jumpSpeed;
					if (this.jumpCurrent <= 0) {
						this.isJumping = false;
						this.jumpDown = false;
						this.jumpUp = false;
					}
				}
				this.z = this.jumpCurrent;
		}
		
		if (right && World.isFree((int)(this.x + this.speed), this.getY(), this.getZ())) { // se apertou tecla para direita
			moved = true; // está em movimento
			dir = rightDir; // codigo da tecla atual em movimento
			this.x += this.speed; // movimenta o player
		} 
		else if (left && World.isFree((int)(this.x - this.speed), this.getY(), this.getZ())) { // se apertou tecla para esquerda
			moved = true; // está em movimento
			dir = leftDir; // codigo da tecla atual em movimento
			this.x -= this.speed; // movimenta o player
		}
		
		if (up && World.isFree(this.getX(), (int)(this.y - this.speed), this.getZ())) { // se apertou tecla para cima
			moved = true; // está em movimento
			dir = upDir; // codigo da tecla atual em movimento
			this.y -= this.speed; // movimenta o player
		} 
		else if (down && World.isFree(this.getX(), (int)(this.y + this.speed), this.getZ())) { // se apertou tecla para baixo
			moved = true; // está em movimento
			dir = downDir; // codigo da tecla atual em movimento
			this.y += this.speed; // movimenta o player
		}
		
		if (moved) { // se esta em movimento
			this.frames++; // soma quantidade de vezes que esta em movimento
			if (this.frames == Player.MAX_FRAMES) { // se a quantidade de vezes for igual a maxima para troca de sprite
				this.frames = 0; // zera
				this.index++; // soma indice do vetor de sprite para tal lado
				if (this.index > Player.MAX_INDEX) { // se indice é maior que a quantidade total de sprites para animacao daquele lado
					index = 0; // volta para primeira sprite
				}
			}
		}
		
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionGun();
		
		if (this.isDamaged) {
			Sound.hurt.play();
			this.damageFrames++;
			if (this.damageFrames == 10) {
				this.damageFrames = 0;
				this.isDamaged = false;
			}
		}
		
		if (this.keyShoot) {
			this.keyShoot = false;
			if (this.hasGun) {
				if (this.ammo > 0) {
					if (this.dir == this.rightDir || this.dir == this.leftDir) {
						this.ammo--;
						int dx = 0;
						int px = 0;
						if (this.dir == this.rightDir) {
							dx = 1;
							px = 13;
						} else if (this.dir == this.leftDir){
							dx = -1;
							px = -1;
						}
						BulletShoot bullet = new BulletShoot(this.getX() + px , this.getY() + 8, 3, 3, null, dx, 0);
						Game.bullets.add(bullet);
					} else {
						System.out.println("BLOQUEADO!");
					}
				}
			}
		}
		
		if (mouseShoot) {
			this.mouseShoot = false;
			if (this.hasGun) {
				if (this.ammo > 0) {
					if (this.dir == this.rightDir || this.dir == this.leftDir) {
						this.ammo--;
						
						int px = 0;
						if (this.dir == this.rightDir) {
							px = 13;
						} else if (this.dir == this.leftDir){
							px = -1;
						}
						
						double angle = Math.atan2(this.my - (this.getY() + 8 - Camera.y), this.mx - (this.getX() + px - Camera.x));
						double dx = Math.cos(angle);
						double dy = Math.sin(angle);

						BulletShoot bullet = new BulletShoot(this.getX() + px , this.getY() + 8, 3, 3, null, dx, dy);
						Game.bullets.add(bullet);
					} else {
						System.out.println("BLOQUEADO!");
					}
					
				}
			}
		}
		
		if (this.life <= 0) {
			this.life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		Camera.x = Camera.clamp((this.getX() - (Game.WIDTH/2)), 0, ((World.WIDTH*16) - Game.WIDTH));
		Camera.y = Camera.clamp((this.getY() - (Game.HEIGHT/2)), 0, ((World.HEIGHT*16) - Game.HEIGHT));
	}
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++)
		{
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColliding(this, atual))
				{
					this.hasGun = true;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++)
		{
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColliding(this, atual))
				{
					this.ammo+=300;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++)
		{
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColliding(this, atual))
				{
					this.life += 10;
				
					if(this.life > 100)
						this.life = 100;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if (!this.isDamaged) {
			if (dir == rightDir) { // verifica se o codigo é igual o de tecla para direita
				g.drawImage(this.rightPlayer[this.index], this.getX() - Camera.x, this.getY() - Camera.y - this.getZ(), null); // atualiza a posição do player
				if (this.hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 2 - Camera.x, this.getY() + 1 - Camera.y - this.getZ(), null);
					this.ultimaDirecao = "right";
				}
			} else if (dir == leftDir) {
				g.drawImage(this.leftPlayer[this.index], this.getX() - Camera.x, this.getY() - Camera.y - this.getZ(), null);
				if (this.hasGun) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - 2 - Camera.x, this.getY() + 2 - Camera.y - this.getZ(), null);
					this.ultimaDirecao = "left";
				}
			} else if (dir == upDir) { 
				if (this.hasGun) {
					BufferedImage gun = null;
					if (this.ultimaDirecao == "right" || this.ultimaDirecao == "downRight" || this.ultimaDirecao == "upLeft") {
						gun = Entity.GUN_LEFT;
						this.ultimaDirecao = "upLeft";
					} else if (this.ultimaDirecao == "left" || this.ultimaDirecao == "downLeft" || this.ultimaDirecao == "upRight"){
						gun = Entity.GUN_RIGHT;
						this.ultimaDirecao = "upRight";
					}
					g.drawImage(gun, this.getX() - Camera.x, this.getY() + 1 - Camera.y - this.getZ(), null);
				}
				g.drawImage(this.upPlayer[this.index], this.getX() - Camera.x, this.getY() - Camera.y - this.getZ(), null); 
			} else if (dir == downDir) { 
				g.drawImage(this.downPlayer[this.index], this.getX() - Camera.x, this.getY() - Camera.y - this.getZ(), null);
				if (this.hasGun) {
					BufferedImage gun = null;
					if (this.ultimaDirecao == "upRight" || this.ultimaDirecao == "right" || this.ultimaDirecao == "downLeft") {
						gun = Entity.GUN_LEFT;
						this.ultimaDirecao = "downLeft";
					} else if (this.ultimaDirecao == "upLeft" || this.ultimaDirecao == "left" || this.ultimaDirecao == "downRight"){
						gun = Entity.GUN_RIGHT;
						this.ultimaDirecao = "downRight";
					} 
					g.drawImage(gun, this.getX() - Camera.x, this.getY() + 1 - Camera.y - this.getZ(), null);
				}
			}
		} else {
			g.drawImage(this.playerDamage, this.getX() - Camera.x, this.getY() - Camera.y - this.getZ(), null);
		}
	}
}
