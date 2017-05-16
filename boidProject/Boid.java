package boidProject;

import java.util.ArrayList;
import java.util.Random;

public class Boid {
	
	public int id; 
	public double[] pos = new double[2]; 
	public double[] velocity = new double[2]; 
	public Parameters params; 
	public ArrayList<Boid> neigh = new ArrayList<Boid>(); 
	public ArrayList<double[]> neighDist = new ArrayList<double[]>(); 
	public ArrayList<Obstacle> nearbyObstacles = new ArrayList<Obstacle>(); 
	public ArrayList<Predator> nearbyPredators = new ArrayList<Predator>(); 
	int[] dispSize; 
	ArrayList<double[]> obstacleForces = new ArrayList<double[]>();  
	
	
	public Boid(int id, double[] startPos, double startVel[], int displaySize[], Parameters params){
		pos[0] = startPos[0]; 
		pos[1] = startPos[1]; 
		velocity[0] = startVel[0]; velocity[1] = startVel[1]; 	
		dispSize = displaySize; 
		this.params = params;
		this.id = id; 
	}

	
	
	public void updatePosition(){
		for(int i = 0; i < 2; i++){
			if(pos[i] + velocity[i] > dispSize[i]){
				pos[i] += velocity[i] - dispSize[i]; 
			}
			else if(pos[i] + velocity[i] < 0){
				pos[i] = dispSize[i] - pos[i] + velocity[i]; 
			}
			else{
				pos[i] = pos[i] + velocity[i]; 	
			}
		}
	}
	
	
	
	public void uppdateVelocity(){
		double[] force = calculateForce(); 
		velocity[0] = velocity[0] + force[0]; 
		velocity[1] = velocity[1] + force[1];
		
		double sqSpeed = (velocity[0]*velocity[0]) + (velocity[1]*velocity[1]); 
		if(sqSpeed > params.maxSpeed){
			double ratio = Math.sqrt((params.maxSpeed/sqSpeed));    
			velocity[0] = velocity[0]*ratio; 
			velocity[1] = velocity[1]*ratio;	
		}
		
	}
	
	
	
	public double[] calculateForce(){
		double force[]  = new double[2];
		calculteNeighDist(); 
		force[0] = ((double) params.allignementParam * calcAllignementForce()[0]) +  
					((double) params.cohesiontParam * calcCohesionForce()[0]) +  
					((double) params.seperationParam * calcSeperationForce()[0]) + 
					((double) params.obstacleForce * calcObstacleForce()[0]) + 
					((double) params.fleeForce * calcFleeForce()[0]); 
					
		
		force[1] = ((double) params.allignementParam * calcAllignementForce()[1]) + 
					((double) params.cohesiontParam * calcCohesionForce()[1]) +  
					((double) params.seperationParam * calcSeperationForce()[1]) +  
					((double) params.obstacleForce * calcObstacleForce()[1]) + 
					((double) params.fleeForce * calcFleeForce()[1]);
		neigh = new ArrayList<Boid>(); 
		neighDist = new ArrayList<double[]>();
		nearbyObstacles = new ArrayList<Obstacle>(); 
		nearbyPredators = new ArrayList<Predator>(); 
		return force; 	
	}
	
	
	
	private double[] calcFleeForce() {
		double[] force = new double[2];
		for(int i = 0; i < nearbyPredators.size(); i++){
			
			double[] distVector = calcDistanceVector(this.pos, nearbyPredators.get(i).pos);    
			double dist = getVectorSize(distVector);
			
			Random rndX = new Random();  
			Random rndY = new Random(); 
			double[] chaos = {(((double)rndX.nextInt(50)) +75)/100, ( ((double)rndY.nextInt(50))+75)/100}; 
			
			if(dist != 0 && dist < 30){
				force[0] = force[0] - (chaos[0] *distVector[0]/(dist));  
				force[1] = force[1] - (chaos[1] *distVector[1]/(dist)); 
			}
			else if(dist > 30){ 
				force[0] = force[0] - (chaos[0] *distVector[0]/(dist));  
				force[1] = force[1] - (chaos[0] *distVector[1]/(dist)); 
			}
		}
		return force; 
	}


	private double[] calcObstacleForce() {
		double[] force = new double[2];
		boolean addForce = false; 
		boolean pullForce = false; 
		for(int i = 0; i < nearbyObstacles.size(); i++){
			double[] d = calculateObstacleDist(nearbyObstacles.get(i)); 
			double dist = Math.sqrt((d[0]*d[0]) + (d[1]*d[1]));
			
			double[] nextPos = {pos[0] + velocity[0], pos[1] + velocity[1]}; 
			double[] nd = {nextPos[0]- nearbyObstacles.get(i).getxPos(), nextPos[1] - nearbyObstacles.get(i).getyPos()}; 
			double nextDistance = Math.sqrt((nd[0]*nd[0]) + (nd[1]*nd[1]));
			
			if(nextDistance <= dist && nearbyObstacles.get(i).boidsInRange[this.id] > 0){
				
				if(dist > 0 && nearbyObstacles.get(i).boidsInRange[this.id] >= 1 && collision(dist, nearbyObstacles.get(i).getRadius()-5)){//) && dist > (nearbyObstacles.get(i).getRadius())){ 
					addForce = true;
					force[0] = -velocity[0]; 
					force[1] = -velocity[1];
					double radius = params.avoidanceRadius + nearbyObstacles.get(i).getRadius(); 
					double ratio = radius / dist; 
					double[] r1 = {((-d[1])*ratio), (d[0]*ratio)}; 
					double[] r2 = {((d[1])*ratio), (-d[0]*ratio)}; 
					double[] p1 = {(nearbyObstacles.get(i).getxPos() + r1[0]), (nearbyObstacles.get(i).getyPos() + r1[1])};
					double[] p2 = {(nearbyObstacles.get(i).getxPos() + r2[0]), (nearbyObstacles.get(i).getyPos() + r2[1])};
					double speed = Math.sqrt((velocity[0]*velocity[0]) + (velocity[1]*velocity[1]));
					double ratio1 = speed / Math.sqrt(((p1[0]-pos[0])*(p1[0]-pos[0]) + (p1[1]-pos[1])*(p1[1]-pos[1]))); 
					double ratio2 = speed / Math.sqrt(((p2[0]-pos[0])*(p2[0]-pos[0]) + (p2[1]-pos[1])*(p2[1]-pos[1]))); 
					double[] v1 = {(p1[0]-pos[0])*(ratio1), (p1[1]-pos[1])*ratio1}; 
					double[] v2 = {(p2[0]-pos[0])*(ratio2), (p2[1]-pos[1])*ratio2}; 
					double[] d1 = {v1[0] - velocity[0], v1[1] - velocity[1]}; 
					double[] d2 = {v2[0] - velocity[0], v2[1] - velocity[1]}; 
					
					if(nearbyObstacles.get(i).boidsInRange[this.id] != 3 && ((d1[0]*d1[0]) + (d1[1]*d1[1])) > ((d2[0]*d2[0]) + (d2[1]*d2[1]))){ 
						force[0] = force[0] - 10*d1[0]; 
						force[1] = force[1] - 10*d1[1];
						nearbyObstacles.get(i).boidsInRange[this.id] = 2; 
						
					}
					else{
						force[0] = force[0] - 10*d2[0]; 
						force[1] = force[1] - 10*d2[1];
						nearbyObstacles.get(i).boidsInRange[this.id] = 3;
						 
					} 
				}		
			}
			else if(nextDistance > dist + 10){
				nearbyObstacles.get(i).boidsInRange[this.id] = -1; 
				pullForce = true; 
			}
			

			if(addForce){
				obstacleForces.add(force); 
			}
			else if(pullForce){
				if(obstacleForces.size() > 0){
					double[] f = obstacleForces.remove(obstacleForces.size()-1);   
					force[0] = -f[0]*2; 
					force[1] = -f[1]*2; 
				}
			}
		}
		
		if(!addForce && !pullForce && obstacleForces.size() > 0){
			double[] f = obstacleForces.remove(obstacleForces.size()-1);   
			force[0] = -f[0]*0.7; 
			force[1] = -f[1]*0.7; 	
		}
		
		return force; 
	}


	
	
	public boolean collision(double dist, double radius){
		double safePath2Length = (radius*radius) + (dist*dist); 
		double[] steringVector = {velocity[0]/getVectorSize(velocity), velocity[1]/getVectorSize(velocity)};   
		double steringVector2Length = (steringVector[0]*steringVector[0]) + (steringVector[1]*steringVector[1]); 
		if(steringVector2Length < safePath2Length){
			return true; 
		}
				
		return false;  
	}
	
	
	
	private double[] calcSeperationForce() {
		double[] force = new double[2];
		for(int i = 0; i < neighDist.size(); i++){
			double dist = getVectorSize(neighDist.get(i));   
			if(dist != 0 && dist < 10){
				force[0] = force[0] - (100*neighDist.get(i)[0]/(dist*dist));  
				force[1] = force[1] - (100*neighDist.get(i)[1]/(dist*dist)); 
			}
			else if(dist > 10){ 
				force[0] = force[0] - (neighDist.get(i)[0]/(dist*dist));  
				force[1] = force[1] - (neighDist.get(i)[1]/(dist*dist)); 
			}
		}
		return force; 
	}


	
	private double[] calcCohesionForce() {
		double center[] = calculateSenter(); 
		double force[] = new double[2]; 
		force[0] = center[0] - pos[0]; 
		force[1] = center[1] - pos[1]; 
		return force; 
	}
	


	public double[] calcAllignementForce(){
		double[] force = new double[2]; 
		for(int i = 0; i < neigh.size(); i++){
			force[0] = force[0] + neigh.get(i).velocity[0]; 
			force[1] = force[1] + neigh.get(i).velocity[1];
		}
		if(neigh.size() > 0){
			force[0] = force[0] / neigh.size(); 
			force[1] = force[1] / neigh.size(); 
		}
		return force; 
	}
	
	
	
	public void calculteNeighDist(){
		for(int i = 0; i < neigh.size(); i++){
			double[] dist = new double[2]; 
			dist[0] = (neigh.get(i).pos[0] - this.pos[0]); 
			dist[1] = (neigh.get(i).pos[1] - this.pos[1]);
			neighDist.add(dist); 
		}
	}
	
	public double[] calculateObstacleDist(Obstacle obs){
		double[] dist = new double[2]; 
		dist[0] = (obs.getxPos() - this.pos[0]); 
		dist[1] = (obs.getyPos() - this.pos[1]);
		return dist; 
	}
	
	
	
	public double[] calculateSenter(){
		double center[] = new double[2]; 
		for(int i = 0; i < neigh.size(); i++){
			center[0] = center[0] + neigh.get(i).pos[0]; 
			center[1] = center[1] + neigh.get(i).pos[1]; 
		}
		if(neigh.size() > 0){
			center[0] = center[0] / (neigh.size()); 
			center[1] = center[1] / (neigh.size()); 
		}
		return center; 
	}
	
	
	
	public double getVectorSize(double[] v){
		return Math.sqrt((v[0]*v[0]) + (v[1]*v[1]));
	}
	
	
	
	public double[] calcDistanceVector(double p1ThisItem[], double p2[]){ 
		double[] dist = new double[2]; 
		dist[0] = (p2[0] - p1ThisItem[0]); 
		dist[1] = (p2[1] - p1ThisItem[1]);
		return dist; 
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
