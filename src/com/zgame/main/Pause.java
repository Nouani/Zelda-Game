package com.zgame.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.zgame.world.World;

public class Pause {
	private String[] options = {"Sair para Menu","Voltar"};
	
	private int currentOption = 0;
	private int maxOption = options.length - 1;

	
	public boolean up, down, enter, escape;
	
	public void tick() {
		if (up) {
			this.up = false;
			this.currentOption--;
			if (this.currentOption < 0) {
				this.currentOption = this.maxOption;
			}
		}
		
		if (down) {
			this.down = false;
			this.currentOption++;
			if (this.currentOption > this.maxOption) {
				this.currentOption = 0;
			}
		}
		if (enter) {
			this.enter = false;
			if(this.options[this.currentOption] == "Voltar") {
				this.escape = false;
				Game.gameState = "NORMAL";
			} else if (this.options[this.currentOption] == "Sair para Menu") {
				this.escape = false;
				Game.gameState = "MENU";
			} 
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0,0,0,100));
		//g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.ORANGE);
		g.setFont(new Font("arial",Font.BOLD,46));
		g.drawString("Paused Game",((Game.WIDTH*Game.SCALE)/2)-155,((Game.HEIGHT*Game.SCALE)/2)-140);
		
		// Opcoes de menu
		//g.setColor(Color.ORANGE);
		//g.fillRect(0, ((Game.HEIGHT*Game.SCALE)/2)-95, Game.WIDTH*Game.SCALE, 200);
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,30));
		for (int i = 0; i < this.options.length; i++) {
			String title = this.options[i];
			int tamanho = title.length();
			if (tamanho * 2 != 0) {
				tamanho++;
			}
			int offsetX = (tamanho / 2) * 16;
			int offsetY = (((3 * i)*16) + (-40));
			g.drawString(title, ((Game.WIDTH*Game.SCALE)/2)-offsetX, ((Game.HEIGHT*Game.SCALE)/2)+offsetY);
			
			if (this.options[this.currentOption] == title) {
				g.drawString(">", ((Game.WIDTH*Game.SCALE)/2)-offsetX-20, ((Game.HEIGHT*Game.SCALE)/2)+offsetY);
			}
		}
	}
}
