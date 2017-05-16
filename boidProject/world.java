package boidProject;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;


public class world {
	
	public static Parameters param = new Parameters();   
	public static int[] dispSize = {1200,800}; 
	public static int timeIntervall = 17; 

	public static ArrayList<Boid> boids = new ArrayList<Boid>(); 
	public static ArrayList<Predator> predators = new ArrayList<Predator>(); 
	public static ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>(); 
	
	public static void main(String[] args){
		param.setParams(); 
		simulateBehavour(true); 
	}
	
	public static void simulateBehavour(boolean running){
		createBoids(225);
		System.out.println("NoOfBoids:" + boids.size()); 
		
	    JFrame frame = new JFrame();
	    GraphicDisplay GD = new GraphicDisplay(boids, predators, obstacles, dispSize[0], dispSize[1]);
	    frame.add(GD); 
	    frame.setSize(dispSize[0] + 50, dispSize[1]+50);
	    frame.setVisible(true);
	    GD.repaint();
	    
	    SliderPanel sp = new SliderPanel(param); 
	    buttonPanel bp = new buttonPanel(predators, obstacles, boids, dispSize); 
	   
	    double time = System.currentTimeMillis(); 
	    
	    while(running){
	    	if(System.currentTimeMillis() > time +timeIntervall){
	    		sp.updateValues(); 
	    		
	    		for(int i = 0; i < boids.size(); i++){
	    			assignNeighbours(boids.get(i), i+1, false);
	    			assignNearbyObstcles(boids.get(i)); 
	    			assignNearbyPredators(boids.get(i)); 
	    			boids.get(i).uppdateVelocity(); 
				}  
	    		
	    		for(int i = 0; i < predators.size(); i++){
	    			assignNeighbours(predators.get(i), 0, true);
	    			assignNearbyObstcles(predators.get(i)); 
	    			assignNearbyPredators(predators.get(i)); 
	    			predators.get(i).uppdateVelocity();  
	    			
	    		}
	    		
	    		for(int i = 0; i < boids.size(); i++){
					boids.get(i).updatePosition();   
				}  
	    		for(int i = 0; i < predators.size(); i++){
	    			predators.get(i).updatePosition(); 
	    		}
	    		
	    		
	    		GD.repaint(); 
	    		time =  System.currentTimeMillis();
			}
	    	}
	    	
	}
	
	
	public static void createBoids(int numBoids){
		for(int i = 0; i < numBoids; i++){
			Random rndX = new Random();  
			Random rndY = new Random(); 
			double[] startPos = {(double) rndX.nextInt(1200), (double) rndY.nextInt(800)}; 
			Random rndsX =  new Random(); 
			Random rndsY =  new Random(); 
			double xspeed = ((double) (rndsX.nextInt(80) - 40)) / 10; 
			double yspeed = ((double) (rndsY.nextInt(80) - 40)) / 10; 
			double[] speed = {xspeed,yspeed}; 
			Boid boid = new Boid(i, startPos, speed, dispSize, param); 
			boids.add(boid); 
		}
	}
	

	
	
	public static void assignNeighbours(Boid boid, int k, boolean isPredator){ 
		for(int i = k; i < boids.size(); i++){; 
			if(calcDistance(boid.pos[0], boid.pos[1], boids.get(i).pos[0], boids.get(i).pos[1]) < boid.params.neighRadius && boid != boids.get(i)){
				boid.neigh.add(boids.get(i));
				if(!isPredator){
					boids.get(i).neigh.add(boid); 
				}
			}
		}
	}
	
	public static void assignNearbyObstcles(Boid boid){
		for(int i = 0; i < obstacles.size(); i++){
			if(calcDistance(boid.pos[0], boid.pos[1], obstacles.get(i).getxPos(), obstacles.get(i).getyPos()) < param.neighRadius + obstacles.get(i).getRadius()){
				boid.nearbyObstacles.add(obstacles.get(i)); 
				if(obstacles.get(i).boidsInRange[boid.id] == 0){
					obstacles.get(i).boidsInRange[boid.id] = 1; 
				}
			}	
			else{
				obstacles.get(i).boidsInRange[boid.id] = 0; 
			}
		}
	}
	
	public static void assignNearbyPredators(Boid boid){
		for(int i = 0; i < predators.size(); i++){
			if(calcDistance(boid.pos[0], boid.pos[1], predators.get(i).pos[0], predators.get(i).pos[1]) < param.neighRadius && boid != predators.get(i)){
				boid.nearbyPredators.add(predators.get(i)); 
			}
		}
	}
		
	public static double calcDistance(double item1Xpos, double item1Ypos, double item2Xpos, double item2Ypos){
		double xDist = item1Xpos - item2Xpos; 
		double yDist = item1Ypos - item2Ypos; 
		double dist = Math.sqrt((xDist*xDist) + (yDist*yDist));
		return dist; 
	}


	
	

	
	
		
	
	
	
	
	


	

}
