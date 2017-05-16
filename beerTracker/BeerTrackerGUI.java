package beerTracker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class BeerTrackerGUI extends JComponent{
	
	

	int[][] board; 
	int agentPos; 
	int dir; 
	int objectSize; 
	Color lines = new Color(229,229,229); 
	Color pullC = new Color(112,0,223); 
	boolean pull = false; 
	
	
	public BeerTrackerGUI(){
			
	}
		
	public void updateInfo(int[][] world, int agentPos, int objectSize, boolean pull){
		this.board = world; 
		this.agentPos = agentPos; 
		this.objectSize = objectSize;
		this.pull = pull; 
		
	}
	
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		int posY = 335 - 22;  
		
		for(int y = 0; y < 15; y++) { //Iterates through each column
			
			int posX = 0;  	
			for(int x = 0; x < 30; x++ ) { //Iterates through each row
				
				// Sets the color of the square, based on: obstacle, unseen, open, closed, startpoint or goalpoint 
				
			
				
				
				if(board[x][y] == 2){
					g2d.setColor((Color.red)); 
				}
				else if(board[x][y] == 1){
					if(objectSize > 4){
						g2d.setColor((Color.orange)); 
					}
					else{
						g2d.setColor((Color.green)); 
					}
						
				}
				else if (board[x][y] == 3){
					g2d.setColor((Color.red));
					if(pull){
						g2d.setColor(pullC);
					}
				}
				
				else{
					g2d.setColor((Color.gray));
				}
				
				
				g2d.fill(new Rectangle(posX,posY,22,22));
				
				// Sets letter based on weather it is start/goal/path/open/closed -marked node
				g2d.setColor(Color.black); 
			
			
				
				if(board[x][y] == 2 ){
					g2d.setColor(Color.black);
					g2d.drawLine(posX, posY, posX +22, posY); 
					g2d.drawLine(posX, posY, posX, posY +22);  
				}
				else if (board[x][y] == 3){
					g2d.setColor((Color.green)); 
					g2d.drawLine(posX, posY, posX +22, posY); 
					g2d.drawLine(posX, posY, posX, posY +22);
				}

				
				posX = posX + 22;  
			}
			
			posY = posY - 22;   	
			 
		
		}		 
}
	

}
