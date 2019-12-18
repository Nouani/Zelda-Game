package com.zgame.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.zgame.entities.Enemy;
import com.zgame.entities.Entity;
import com.zgame.main.Game;

public class World {
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path)); // armazenando o bufffer do mapa
			
			int[] pixels = new int[map.getWidth()*map.getHeight()]; // inicializando o vetor de pixels
			this.tiles = new Tile[map.getWidth()*map.getHeight()]; // inicializando o vetor de tiles
			
			// setando "constantes"
			World.WIDTH = map.getWidth();
			World.HEIGHT = map.getHeight();
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());  // armazenando cada pixel no vetor
			
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())]; // pega o pixel atual
					
					tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR); // sempre armazena a grama
					
					// verifica cada pixel e sua cor se for um dos casos abaixo 
					// troca a posical atual de grama para o determinado 
					if (pixelAtual == 0xFF000000) { 
						tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					} 
					else if (pixelAtual == 0xFFFFFFFF) {
						tiles[xx + (yy * World.WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					} 
					else if (pixelAtual == 0xFF0026FF) {
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
					else if (pixelAtual == 0xFFFF0000) {
						Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY));
					}
					else if (pixelAtual == 0xFFFF6A00) {
						Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.WEAPON));
					}
					else if (pixelAtual == 0xFFFF7F7F) {
						Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.LIFEPACK));
					}
					else if (pixelAtual == 0xFFFFD800) {
						Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.BULLET));
					}
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
		// converte a posicao e pega o pixel do proximo x e y
		int x1 = xNext / World.TILE_SIZE;
		int y1 = yNext / World.TILE_SIZE;
		
		int x2 = (xNext + World.TILE_SIZE - 1) / World.TILE_SIZE;
		int y2 = yNext / World.TILE_SIZE;
		
		int x3 = xNext / World.TILE_SIZE;
		int y3 = (yNext + World.TILE_SIZE - 1) / World.TILE_SIZE;
		
		int x4 = (xNext + World.TILE_SIZE - 1) / World.TILE_SIZE;
		int y4 = (yNext + World.TILE_SIZE - 1) / World.TILE_SIZE;
		
		return !((World.tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				 (World.tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				 (World.tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				 (World.tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	public void render(Graphics g) {
		// ">>" == dividir por 16
		int xStart = Camera.x / World.TILE_SIZE; // pega o pixel do x
		int yStart = Camera.y / World.TILE_SIZE; //pega o pixel do y
		
		int xFinal = xStart + (Game.WIDTH / World.TILE_SIZE); // somando o x inicial com a largura em pixels
		int yFinal = yStart + (Game.HEIGHT / World.TILE_SIZE); // somando o y inicial com a altura em pixels
		
		for (int xx = xStart; xx <= xFinal; xx++) {
			for (int yy = yStart; yy <= yFinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= World.WIDTH || yy >= World.HEIGHT) {
					continue;
				}
				Tile tile = this.tiles[xx + (yy*World.WIDTH)];
				tile.render(g);
			}
		}
	}
}
