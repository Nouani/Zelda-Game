package com.zgame.graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {
	
	private BufferedImage spritesheet; 
	
	public Spritesheet(String path) {
		try {
			this.spritesheet = ImageIO.read(getClass().getResource(path)); // armazena a o buffer da imagem passada no construtor
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) { // retorna o buffer da sprite
		return this.spritesheet.getSubimage(x, y, width, height);
	}
}
