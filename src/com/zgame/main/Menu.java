package com.zgame.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.zgame.world.World;

public class Menu {
	private String[] options = {"Novo Jogo","Carregar","Sair"};
	
	private int currentOption = 0;
	private int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
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
			if(this.options[this.currentOption] == "Novo Jogo") {
				Game.gameState = "NORMAL";
				Game.currentLevel = 1;
				File file = new File("save.txt");
				file.delete();
				World.restartGame(Game.LEVEL_INICIAL);
			} else if (this.options[this.currentOption] == "Carregar") {
				File file = new File("save.txt");
				if (file.exists()) {
					String saver = Menu.loadGame(10);
					Menu.applySave(saver);
				}
			} else if (this.options[this.currentOption] == "Sair") {
				int posse = -1;
				if (Game.player.hasGun)
					posse = 1;
				else
					posse = 0;
				
				String[] options = {"level","life","gun"};
				int[] options2 = {Game.currentLevel, (int)Game.player.life, Game.player.ammo, posse};
				Menu.saveGame(options, options2, 10);
				System.out.println("JOGO SALVO!");
				System.exit(1);
			}
		} else {
			//Sound.music.loop();
		}
	} 
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for (int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
			case "level":
				World.restartGame("level"+spl2[1]+".png");
				Game.gameState = "NORMAL";
				break;
			case "life":
				Game.player.life = Double.parseDouble(spl2[1]);
				break;
			case "gun":
				if (Integer.parseInt(spl2[1]) > 0)
					Game.player.hasGun = true;
			}

		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if (file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for (int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				}
				catch(Exception e) {}
			}
			catch(Exception e) {}
		}
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		 BufferedWriter write = null;
		 try {
			 write = new BufferedWriter(new FileWriter("save.txt"));
		 }
		 catch(Exception e) {}
		 
		 for (int i = 0; i < val1.length; i++) {
			 String current = val1[i];
			 current += ":";
			 char[] value = Integer.toString(val2[i]).toCharArray();
			 for (int k = 0; k < value.length; k++) {
				 value[k]+=encode;
				 current+=value[k];
			 }
			 try {
				 write.write(current);
				 if (i < val1.length - 1)
					 write.newLine();
			 }
			 catch(Exception e) {}
		 }
		 try {
			 write.flush();
			 write.close();
		 }
		 catch(Exception e) {}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0,0,0,100));
		//g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.ORANGE);
		g.setFont(new Font("arial",Font.BOLD,46));
		g.drawString("The Legend of Zelda",((Game.WIDTH*Game.SCALE)/2)-220,((Game.HEIGHT*Game.SCALE)/2)-140);
		
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
		/*g.drawString("Novo Jogo", ((Game.WIDTH*Game.SCALE)/2)-72, ((Game.HEIGHT*Game.SCALE)/2)-40);
		g.drawString("Carregar Jogo", ((Game.WIDTH*Game.SCALE)/2)-100, ((Game.HEIGHT*Game.SCALE)/2)+10);
		g.drawString("Sair", ((Game.WIDTH*Game.SCALE)/2)-30, ((Game.HEIGHT*Game.SCALE)/2)+60);*/
		
		
		/*if (options[this.currentOption] == "Novo Jogo") {
			g.drawString(">", ((Game.WIDTH*Game.SCALE)/2)-100, ((Game.HEIGHT*Game.SCALE)/2)-40);
		} else if (options[this.currentOption] == "Carregar") {
			g.drawString(">", ((Game.WIDTH*Game.SCALE)/2)-85, ((Game.HEIGHT*Game.SCALE)/2)+8);
		} else if (options[this.currentOption] == "Sair") {
			g.drawString(">", ((Game.WIDTH*Game.SCALE)/2)-50, ((Game.HEIGHT*Game.SCALE)/2)+57);
		}*/
	}
}
