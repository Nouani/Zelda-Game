package com.zgame.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
					if (pixelAtual == 0xFF000000) {
						System.out.println("1");
						tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					} 
					else if (pixelAtual == 0xFFFFFFFF) {
						tiles[xx + (yy * World.WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					} 
					else if (pixelAtual == 0xFF0026FF) {
						System.out.println("2");
						tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					} 
					else {
						System.out.println("3");
						tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		for (int xx = 0; xx < World.WIDTH ;xx++) {
			for (int yy = 0; yy < World.HEIGHT; yy++) {
				Tile tile = this.tiles[xx + (yy*World.HEIGHT)];
				tile.render(g);
			}
		}
	}
}
