package boidProject;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class buttonPanel  extends Frame implements ActionListener  {

	ArrayList<Predator> preds; 
	ArrayList<Obstacle> obstacles; 
	JButton obstacleAdd;
	JButton obstacleRemove; 
	JButton predatorAdd; 
	JButton predatorRemove; 
	ArrayList<Boid> boids; 
	int[] dp; 
	
	
	public buttonPanel(ArrayList<Predator> preds, ArrayList<Obstacle> obstacles, ArrayList<Boid> boids, int[] dp){
		
		this.preds = preds; 
		this.obstacles = obstacles;
		this.boids = boids; 
		this.dp = dp; 
		
		JFrame buttonsFrame = new JFrame();
	    JPanel buttonsPanel = new JPanel(); 
	    
	    GridLayout buttonsLayout = new GridLayout(2,2);
	    buttonsPanel.setLayout(buttonsLayout);
	
	    obstacleAdd = new JButton("+Obstacle");   
	    obstacleAdd.addActionListener(this);  
	    obstacleRemove = new JButton("-Obstacle");
	    obstacleRemove.addActionListener(this); 
	    predatorAdd = new JButton("+Predator");
	    predatorAdd.addActionListener(this);
	    predatorRemove = new JButton("-Predator"); 
	    predatorRemove.addActionListener(this); 
	    
	    buttonsPanel.add(obstacleAdd);
	    buttonsPanel.add(obstacleRemove);
	    buttonsPanel.add(predatorAdd);
	    buttonsPanel.add(predatorRemove);
	    
	    buttonsFrame.add(buttonsPanel); 
	    
	    buttonsFrame.setSize(250, 120);
	    buttonsFrame.setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == obstacleAdd) {
			addRandomObstcale(); 
		}
		else if(e.getSource() == obstacleRemove){
			removeRandomObstacle(); 
		}
		else if (e.getSource() == predatorAdd) {
			addRandomPredator(); 
		}
		else if(e.getSource() == predatorRemove){
			removeRandomPredator(); 
		}
		
		
	}
	
	public void addRandomObstcale(){
		Random rndX = new Random(); 
		int xPos = rndX.nextInt(900) + 150; 
		Random rndY = new Random(); 
		int yPos = rndY.nextInt(600) + 100;
		Random rndRadius =  new Random(); 
		int radius = rndRadius.nextInt(30) +30;  
		System.out.println(radius); 
		Obstacle obstacle = new Obstacle(xPos, yPos, radius, boids.size()); 
		obstacles.add(obstacle); 

	}
	
	public void addRandomPredator(){
		Random rndX = new Random();  
		Random rndY = new Random(); 
		double[] startPos = {(double) rndX.nextInt(1200), (double) rndY.nextInt(800)}; 
		Random rndsX =  new Random(); 
		Random rndsY =  new Random(); 
		double xspeed = ((double) (rndsX.nextInt(80) - 40)) / 10; 
		double yspeed = ((double) (rndsY.nextInt(80) - 40)) / 10; 
		double[] speed = {xspeed,yspeed}; 
		Parameters predParams = new Parameters(); 
		predParams.setPredParams(); 
		Predator pred = new Predator(preds.size(), startPos, speed, dp, predParams); 
		preds.add(pred); 
	
	}
	
	public void removeRandomObstacle(){
		if(obstacles.size() > 0){
			Random rnd =  new Random(); 
			int i = rnd.nextInt(obstacles.size()); 
			obstacles.remove(i); 
		}
	}
	
	public void removeRandomPredator(){
		if(preds.size() > 0){
			Random rnd =  new Random(); 
			int i = rnd.nextInt(preds.size()); 
			preds.remove(i); 
		}
	}
	
	
	

}
