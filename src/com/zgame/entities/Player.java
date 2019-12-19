package com.zgame.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

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

	public Player(int x, int y, int widght, int height, BufferedImage sprite) {
		super(x, y, widght, height, sprite);
		
		// inicializando os objetos
		this.rightPlayer = new BufferedImage[4];
		this.leftPlayer = new BufferedImage[4];
		this.upPlayer = new BufferedImage[4];
		this.downPlayer = new BufferedImage[4];
		
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
		
		Camera.x = Camera.clamp((this.getX() - (Game.WIDTH/2)), 0, ((World.WIDTH*16) - Game.WIDTH));
		Camera.y = Camera.clamp((this.getY() - (Game.HEIGHT/2)), 0, ((World.HEIGHT*16) - Game.HEIGHT));
	}
	
	public void render(Graphics g) {
		if (dir == rightDir) { // verifica se o codigo é igual o de tecla para direita
			g.drawImage(this.rightPlayer[this.index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null); // atualiza a posição do player
		} else if (dir == leftDir) { 
			g.drawImage(this.leftPlayer[this.index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		} else if (dir == upDir) { 
			g.drawImage(this.upPlayer[this.index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null); 
		} else if (dir == downDir) { 
			g.drawImage(this.downPlayer[this.index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
		}
	}
}
