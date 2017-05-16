package boidProject;

public class Parameters {
	
	public double allignementParam;  //1 //1 //was realy 50
	public double cohesiontParam; //0.2 //1
	public double seperationParam; //10 //100
	public double obstacleForce; 
	public int neighRadius;  
	public int avoidanceRadius; 
	public double fleeForce;  
	public int maxSpeed; 
	
	public void setParams(){
		this.allignementParam = 30; //30
		this.cohesiontParam = 0.9; //0.9
		this.seperationParam = 100; //100
		this.obstacleForce = 1; 
		this.neighRadius = 70;   
		this.avoidanceRadius = 0; 
		this.fleeForce = 1000;  
		this.maxSpeed = 36; 
	}
	
	public void setPredParams(){
		this.allignementParam = 1;
		this.cohesiontParam = 100; 
		this.seperationParam = 0.0; 
		this.obstacleForce = 100; 
		this.neighRadius = 300;   
		this.avoidanceRadius = 30; 
		this.fleeForce = 1000;  
		this.maxSpeed = 45; 
	}

}
