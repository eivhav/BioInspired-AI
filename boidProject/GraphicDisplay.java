package boidProject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

public class GraphicDisplay extends JComponent {
	
	private ArrayList<Boid> boids = new ArrayList<Boid>(); 
	private ArrayList<Predator> predators = new ArrayList<Predator>(); 
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();  
 
	
	public GraphicDisplay(ArrayList<Boid> boids, ArrayList<Predator> predators, ArrayList<Obstacle> obstacles, int xDisplaySize, int yDisplaySize){
		this.boids = boids; 
		this.predators = predators; 
		this.obstacles = obstacles; 
	}
	
	public void repaint(Graphics g){
		paint(g); 
	}
	
	
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		for(int i = 0; i < boids.size(); i++){ 
			drawBoid(boids.get(i), g2d);	
		}
		for (int i = 0; i < predators.size(); i++){
			drawPredator(predators.get(i), g2d); 	
		}
		
		for (int i = 0; i < obstacles.size(); i++){
			drawObstacles(obstacles.get(i), g2d); 	
		}
	
	}
	

	
	private void drawBoid(Boid boid, Graphics g){
		Graphics g2d = g; 
		g2d.setColor(new Color(0, 162, 232)); 
		g2d.fillOval((int) boid.pos[0] -7, (int)boid.pos[1] -7, 14, 14); 
		g2d.setColor(Color.white);
		int[] dirLinePos = getLinePos(boid, 7); 
		g2d.drawLine((int) boid.pos[0], (int)boid.pos[1], (int)boid.pos[0] + dirLinePos[0], (int)boid.pos[1] + dirLinePos[1]);  
	}
	
	
	private void drawPredator(Predator predator, Graphics g) {
		Graphics g2d = g; 
		g2d.setColor(new Color(183, 0, 0)); 
		g2d.fillOval((int) predator.pos[0] -15, (int)predator.pos[1] -15, 30, 30); 
		g2d.setColor(Color.black);  
		int[] dirLinePos = getLinePos(predator, 15); 
		g2d.drawLine((int) predator.pos[0], (int)predator.pos[1], (int)predator.pos[0] + dirLinePos[0], (int)predator.pos[1] + dirLinePos[1]);  
	}
	
	private void drawObstacles(Obstacle obstacle, Graphics g) {
		Graphics g2d = g; 
		g2d.setColor(new Color(37, 152,31)); 
		int radius = (int) (obstacle.getRadius() / 1.5) ; 
		g2d.fillOval((int) obstacle.getxPos() -(radius), (int)obstacle.getyPos() -(radius), radius*2, radius*2); 
		g2d.setColor(new Color(0, 0,31)); 
		g2d.fillOval((int) obstacle.getxPos(), (int)obstacle.getyPos(), 2, 2);
		
	}
	
	private int[] getLinePos(Boid boid, int factor){ 
		int[] pos = new int[2]; 
		double absXVelocity = Math.sqrt((boid.velocity[0]*boid.velocity[0])); 
		double absYVelocity = Math.sqrt((boid.velocity[1]*boid.velocity[1])); 
		double absTotal = absXVelocity+ absYVelocity; 
		pos[0] = (int)(factor*(boid.velocity[0] / absTotal)); 
		pos[1] = (int)(factor*(boid.velocity[1] / absTotal)); 
		return pos; 
		
	}


		
		

	

		
	
	
	
	


	
	
	
	

}
