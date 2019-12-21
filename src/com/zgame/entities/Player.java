package com.zgame.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.zgame.graficos.Spritesheet;
import com.zgame.main.Game;
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
	
	private boolean hasGun = false;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
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
		if (right && World.isFree((int)(this.x + this.speed), this.getY())) { // se apertou tecla para direita
			moved = true; // está em movimento
			dir = rightDir; // codigo da tecla atual em movimento
			this.x += this.speed; // movimenta o player
		} 
		else if (left && World.isFree((int)(this.x - this.speed), this.getY())) { // se apertou tecla para esquerda
			moved = true; // está em movimento
			dir = leftDir; // codigo da tecla atual em movimento
			this.x -= this.speed; // movimenta o player
		}
		
		if (up && World.isFree(this.getX(), (int)(this.y - this.speed))) { // se apertou tecla para cima
			moved = true; // está em movimento
			dir = upDir; // codigo da tecla atual em movimento
			this.y -= this.speed; // movimenta o player
		} 
		else if (down && World.isFree(this.getX(), (int)(this.y + this.speed))) { // se apertou tecla para baixo
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
			this.damageFrames++;
			if (this.damageFrames == 10) {
				this.damageFrames = 0;
				this.isDamaged = false;
			}
		}
		
		if (this.life <= 0) {
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32, 0, 16, 16));
			Game.entities.add(Game.player);
			Game.world = new World("/map.png");
			return;
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
					//Game.entities.remove(atual);
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
					this.ammo+=10;
					//Game.entities.remove(atual);
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
					//Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if (!this.isDamaged) {
			if (dir == rightDir) { // verifica se o codigo é igual o de tecla para direita
				g.drawImage(this.rightPlayer[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null); // atualiza a posição do player
				if (this.hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 2 - Camera.x, this.getY() + 1 - Camera.y, null);
					this.ultimaDirecao = "right";
				}
			} else if (dir == leftDir) {
				g.drawImage(this.leftPlayer[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (this.hasGun) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - 2 - Camera.x, this.getY() + 2 - Camera.y, null);
					this.ultimaDirecao = "left";
				}
			} else if (dir == upDir) { 
				if (this.hasGun) {
					BufferedImage gun = null;
					System.out.println(this.ultimaDirecao);
					if (this.ultimaDirecao == "right" || this.ultimaDirecao == "downRight" || this.ultimaDirecao == "upLeft") {
						gun = Entity.GUN_LEFT;
						this.ultimaDirecao = "upLeft";
					} else if (this.ultimaDirecao == "left" || this.ultimaDirecao == "downLeft" || this.ultimaDirecao == "upRight"){
						gun = Entity.GUN_RIGHT;
						this.ultimaDirecao = "upRight";
					}
					g.drawImage(gun, this.getX() - Camera.x, this.getY() + 1 - Camera.y, null);
				}
				g.drawImage(this.upPlayer[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null); 
			} else if (dir == downDir) { 
				g.drawImage(this.downPlayer[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (this.hasGun) {
					BufferedImage gun = null;
					if (this.ultimaDirecao == "upRight" || this.ultimaDirecao == "right" || this.ultimaDirecao == "downLeft") {
						gun = Entity.GUN_LEFT;
						this.ultimaDirecao = "downLeft";
					} else if (this.ultimaDirecao == "upLeft" || this.ultimaDirecao == "left" || this.ultimaDirecao == "downRight"){
						gun = Entity.GUN_RIGHT;
						this.ultimaDirecao = "downRight";
					} 
					g.drawImage(gun, this.getX() - Camera.x, this.getY() + 1 - Camera.y, null);
				}
			}
		} else {
			g.drawImage(this.playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
