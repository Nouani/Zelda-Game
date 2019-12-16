package com.zgame.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.zgame.entities.Enemy;
import com.zgame.entities.Entity;
import com.zgame.main.Game;

public class World {
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth()*map.getHeight()];
			
			World.WIDTH = map.getWidth();
			World.HEIGHT = map.getHeight();
			this.tiles = new Tile[map.getWidth()*map.getHeight()];
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					
					tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
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
	
	public void render(Graphics g) {
		int xStart = Camera.x >> 4; // ">>" == dividir por 16
		int yStart = Camera.y >> 4;
		
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
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
