package com.zgame.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.zgame.entities.Player;
import com.zgame.main.Game;

public class UI {
	public void render(Graphics g) {
		
		g.setColor(Color.RED);
		g.fillRect(5, 4, 65,11);
		g.setColor(Color.GREEN);
		g.fillRect(5, 4, (int)((Game.player.life/Player.MAX_LIFE)*65),11);
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,10));
		g.drawString((int)Game.player.life+"/"+(int)Player.MAX_LIFE, 18, 13);
	}
}
