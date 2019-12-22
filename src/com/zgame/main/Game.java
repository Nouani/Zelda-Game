package com.zgame.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
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

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{
	private JFrame frame;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private Thread thread;
	private boolean isRunning;
	
	private BufferedImage image;
	
	private int currentLevel = 1;
	private static final int MAX_LEVEL = 2;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public Game() {
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE));
		initFrame();
		
		// Inicializando objetos
		Game.rand = new Random();
		this.ui = new UI();
		this.image = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_ARGB);
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.bullets = new ArrayList<BulletShoot>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/level1.png");
	}
	
	public void initFrame() {
		this.frame = new JFrame("Legends of Zelda");
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
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			e.tick();
		}
		for (int i = 0; i < Game.bullets.size(); i++) {
			BulletShoot b = Game.bullets.get(i);
			b.tick();
		}
		if (Game.enemies.size() == 0) {
			this.currentLevel++;
			if (this.currentLevel > Game.MAX_LEVEL) {
				this.currentLevel = 1;
			} else {
				String newWorld = "level"+this.currentLevel+".png";
				World.restartGame(newWorld);
			}
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
		g.fillRect(0,0,WIDTH,HEIGHT);
		
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
		g.drawImage(this.image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.setColor(Color.WHITE);
		g.drawString("Munição: "+Game.player.ammo, 580, 30);
		bs.show();
	}
	
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
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) { 
			Game.player.right = true;
		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			Game.player.left = true;
		}
		
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			Game.player.up = true;
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			Game.player.down = true;
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
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			Game.player.down = false;
		}
		
		if (code == KeyEvent.VK_SPACE) {
			Game.player.keyShoot = true;
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

}
