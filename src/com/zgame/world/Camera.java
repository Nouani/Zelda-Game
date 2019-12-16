package com.zgame.world;

public class Camera {
	public static int x = 0;
	public static int y = 0;
	
	public static int clamp(int atual, int min, int max) {
		if (atual < min) {
			atual = min;
		} 
		if (atual > max) {
			atual = max;
		}
		
		return atual;
	}
}
