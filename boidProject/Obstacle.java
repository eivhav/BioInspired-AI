package boidProject;

public class Obstacle {
	
	private int xPos; 
	private int yPos; 
	private int radius; 
	public int[] boidsInRange; 
	
	public Obstacle(int xPos, int yPos, int radius, int noOfBoids){ 
		this.setxPos(xPos); 
		this.setyPos(yPos); 
		this.setRadius(radius); 
		this.boidsInRange = new int[noOfBoids]; 
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	

}
