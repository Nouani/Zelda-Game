package com.zgame.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.zgame.entities.Entity;
import com.zgame.entities.Player;
import com.zgame.graficos.Spritesheet;

public class Game extends Canvas implements Runnable, KeyListener{
	private JFrame frame;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private Thread thread;
	private boolean isRunning;
	
	private BufferedImage image;
	
	public List<Entity> entities;
	public static Spritesheet spritesheet;
	
	private Player player;
	
	public Game() {
		this.addKeyListener(this);
		this.setPreferredSize(new Dimension(Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE));
		initFrame();
		
		// Inicializando objetos
		this.image = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_ARGB);
		this.entities = new ArrayList<Entity>();
		this.spritesheet = new Spritesheet("/spritesheet.png");
		
		player = new Player(0,0,16,16,this.spritesheet.getSprite(32, 0, 16, 16));
		this.entities.add(player);
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
		this.thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		
	}
	
	public void tick() {
		for (Entity e : this.entities) {
			e.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) { 
			this.createBufferStrategy(3); 
			return; 
		} 
		Graphics g = this.image.getGraphics(); 
		
		// Limpando o fundo
		g.setColor(new Color(0,255,0)); 
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		// Renderização do jogo
		// Graphics2D g2 = (Graphics2D)g;
		for (Entity e : this.entities) {
			e.render(g);
		}
		
		// Renderização do fFundo
		g.dispose(); // metodo de otimização
		g = bs.getDrawGraphics(); 
		g.drawImage(this.image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null); 
		bs.show();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	@Override
	public void run() {
		requestFocus();
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
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer += 1000;
			}
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
			this.player.right = true;
		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			this.player.left = true;
		}
		
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			this.player.up = true;
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			this.player.down = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
			this.player.right = false;
		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
			this.player.left = false;
		}
		
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			this.player.up = false;
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			this.player.down = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
