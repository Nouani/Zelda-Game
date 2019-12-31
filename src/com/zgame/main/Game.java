package com.zgame.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.zgame.entities.BulletShoot;
import com.zgame.entities.Enemy;
import com.zgame.entities.Entity;
import com.zgame.entities.Player;
import com.zgame.graficos.Spritesheet;
import com.zgame.graficos.UI;
import com.zgame.world.Camera;
import com.zgame.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	private JFrame frame;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private Thread thread;
	private boolean isRunning;
	
	private BufferedImage image;
	
	public static int currentLevel = 1;
	private static final int MAX_LEVEL = 2;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	public static final String LEVEL_INICIAL = "level1.png";
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public Menu menu;
	
	public Pause pause;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
	public Font newFont;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true, changeEnter = true;
	private int framesGameOver = 0, framesEnter = 0, framesRodando;
	private String color = "WHITE";
	private boolean restartGame = false;
	
	private int mx, my;
	
	/*private int[] pixels;
	int xx, yy;*/
	
	public Game() {
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE));
		initFrame();
		
		// Inicializando objetos
		Game.rand = new Random();
		this.ui = new UI();
		this.menu = new Menu();
		this.pause = new Pause();
		this.image = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_ARGB);
		//this.pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.bullets = new ArrayList<BulletShoot>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+Game.LEVEL_INICIAL);
		
		try {
			this.newFont = Font.createFont(Font.TRUETYPE_FONT, this.stream).deriveFont(40F);
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	public void initFrame() {
		this.frame = new JFrame("The Legend of Zelda");
		this.frame.add(this);
		this.frame.setResizable(false);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
	}
	
	public synchronized void start() {
		this.isRunning = true; 
		this.thread = new Thread(this); // criando uma thread
		thread.start(); // iniciando
	}
	
	public synchronized void stop() {
		
	}
	
	public void tick() {
		if (Game.gameState == "NORMAL") {
			this.restartGame = false;
			
			for (int i = 0; i < Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				e.tick();
			}
			for (int i = 0; i < Game.bullets.size(); i++) {
				BulletShoot b = Game.bullets.get(i);
				b.tick();
			}
			if (Game.enemies.size() == 0) {
				Game.currentLevel++;
				if (Game.currentLevel > Game.MAX_LEVEL) {
					Game.currentLevel = 1;
				}
				String newWorld = "level"+Game.currentLevel+".png";
				World.restartGame(newWorld);
			}
		} else if (Game.gameState == "GAME_OVER") {
			this.framesRodando++;
			
			this.framesGameOver++;
			if (this.framesGameOver == 55) {
				this.framesGameOver = 0;
				if (this.showMessageGameOver) 
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}
			
			this.framesEnter++;
			if (this.framesEnter == 25) {
				this.framesEnter = 0;
				if (this.changeEnter) 
					this.changeEnter = false;
				else
					this.changeEnter = true;
			}
			
			if (restartGame) {
				Game.currentLevel = 1;
				this.restartGame = false;
				Game.gameState = "NORMAL";
				String newWorld = "level"+Game.currentLevel+".png";
				World.restartGame(newWorld);
			}
		} else if (Game.gameState == "MENU") {
			this.menu.tick();
		} else if (Game.gameState == "PAUSE") {
			this.pause.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy(); // otimização
		if (bs == null) { 
			this.createBufferStrategy(3); 
			return; 
		} 
		Graphics g = this.image.getGraphics(); 
		
		// Limpando o fundo
		g.setColor(new Color(0,0,0)); 
		g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
		
		// Renderização do jogo
		// Graphics2D g2 = (Graphics2D)g;
		Game.world.render(g);
		for (Entity e : Game.entities) {
			e.render(g);
		}
		for (BulletShoot bullet : Game.bullets) {
			bullet.render(g);
		}
		this.ui.render(g);
		
		// Renderização do fFundo
		g.dispose(); // metodo de otimização
		g = bs.getDrawGraphics();
		//this.drawRectangle(this.xx, this.yy);
		g.drawImage(this.image, 0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE, null);
		g.setFont(new Font("Arial",Font.BOLD,20));
		if (Game.gameState == "NORMAL") {
			g.setColor(Color.WHITE);
			g.setFont(this.newFont);
			g.drawString("Munição: "+Game.player.ammo, 580, 30);
		}
		
		if (Game.gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			
			g.setFont(new Font("Arial",Font.BOLD,60));
			g.setColor(Color.WHITE);
			
			if (showMessageGameOver) {
				g.drawString("Game Over", ((Game.WIDTH*Game.SCALE)/2)-155, ((Game.HEIGHT*Game.SCALE)/2)-5);
			}
			
			if (this.framesRodando > 25) {
				g.setFont(new Font("Arial",Font.BOLD,30));
				g.drawString("Pressione                   para Reiniciar", ((Game.WIDTH*Game.SCALE)/2)-235, ((Game.HEIGHT*Game.SCALE)/2)+30);
				if (this.changeEnter) {
					if (color == "WHITE") {
						g.setColor(Color.BLUE);
					} else {
						g.setColor(Color.WHITE);
					}
				}
				
				g.drawString(">ENTER<", ((Game.WIDTH*Game.SCALE)/2)-85, ((Game.HEIGHT*Game.SCALE)/2)+30);
			}
		} else if (Game.gameState == "MENU") {
			this.menu.render(g);
			
			/*
			Graphics2D g2 = (Graphics2D)g;
			double angleMouse = Math.atan2(60+25 - this.my, 60+25 - this.mx);
			g2.rotate(angleMouse, 60+25,60+25);
			g.setColor(Color.red);
			g.fillRect(60, 60, 50, 50);
			*/
		} else if (Game.gameState == "PAUSE") {
			this.pause.render(g);
		}
		
		bs.show();
	}
	
	/*
	public void drawRectangle(int xOff, int yOff) {
		for (int xx = 0; xx < 32; xx++) {
			for (int yy = 0; yy < 32; yy++) {
				int x = xx + xOff;
				int y = yy + yOff;
				if (x < 0 || y < 0 || x >= Game.WIDTH || y >= Game.HEIGHT)
					continue;
				else
					this.pixels[x + (y * Game.WIDTH)] = 0xFFFF0000;
			}
		}
	}
	*/
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	@Override
	public void run() {
		this.requestFocus();
		double lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		while (isRunning) {
			double now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) { // se deu 1 segundo
				tick(); // logica
				render(); // renderização
				frames++;
				delta--;
			}
			
			// verificação de fps
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer += 1000;
			}
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) { // ativar movimentação
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_Z) {
			Game.player.jump = true;
		}
		
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) { 
			Game.player.right = true;
		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			Game.player.left = true;
		}
		
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			Game.player.up = true;
			
			if (Game.gameState == "MENU") {
				this.menu.up = true;
			} else if (Game.gameState == "PAUSE") {
				this.pause.up = true;
			}
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			Game.player.down = true;
			
			if (Game.gameState == "MENU") {
				this.menu.down = true;
			} else if (Game.gameState == "PAUSE") {
				this.pause.down = true;
			}
		}
		
		if (code == KeyEvent.VK_SPACE) {
			Game.player.keyShoot = true;
		}
		
		if (code == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			
			if (Game.gameState == "MENU") {
				this.menu.enter = true;
			} else if (Game.gameState == "PAUSE") {
				this.pause.enter = true;
			}
		}
		
		if (code == KeyEvent.VK_ESCAPE) {
			if (this.pause.escape) {
				Game.gameState = "NORMAL";
				this.pause.escape = false;
			} else {
				Game.gameState = "PAUSE";
				this.pause.escape = true;
			}					
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { // desativa movimentação
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
			Game.player.right = false;
		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			Game.player.left = false;
		}
		
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			Game.player.up = false;
			
			if (Game.gameState == "MENU") {
				this.menu.up = false;
			}
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			Game.player.down = false;
			
			if (Game.gameState == "MENU") {
				this.menu.down = false;
			}
		}
		
		if (code == KeyEvent.VK_SPACE) {
			Game.player.keyShoot = false;
		}
		
		if (code == KeyEvent.VK_ENTER) {
			this.restartGame = false;
			
			if (Game.gameState == "MENU") {
				this.menu.enter = false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int code = e.getButton();
		if (code == MouseEvent.BUTTON1) {
			Game.player.mouseShoot = true;
			Game.player.mx = e.getX() / 3;
			Game.player.my = e.getY() / 3;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	}

}
