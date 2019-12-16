package com.zgame.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	private JFrame frame;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 120;
	public static final int SCALE = 3;
	
	private Thread thread;
	private boolean isRunnable;
	
	private BufferedImage image;
	
	public Game() {
		this.setPreferredSize(new Dimension(Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE));
		this.image = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_ARGB);
		initFrame();
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
		this.isRunnable = true;
		this.thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		
	}
	
	public void tick() {
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) { 
			this.createBufferStrategy(3); 
			return; 
		} 
		Graphics g = this.image.getGraphics(); 
		
		// limpando o fundo
		g.setColor(new Color(0,0,0)); 
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		// renderizando
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
		double lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		while (isRunnable) {
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

}
