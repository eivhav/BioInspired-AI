package flatlandv1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;


public class FlatlandGUI extends JComponent {
	
	
	// This class defines the graphic of the map. 
	
		int[][] board; 
		int[] agentPos; 
		int dir; 
		
		
		public FlatlandGUI(){
				
		}
			
		public void updateInfo(int[][] board, int[] agentPos, int dir ){
			this.board = board; 
			this.agentPos = agentPos; 
			this.dir = dir; 		
		}
		
		public void paint(Graphics g) {
			
			Graphics2D g2d = (Graphics2D) g;
			int posY = 240 - 22;  
			
			for(int y = 0; y < 10; y++) { //Iterates through each column
				
				int posX = 0;  	
				for(int x = 0; x < 10; x++ ) { //Iterates through each row
					
					// Sets the color of the square, based on: obstacle, unseen, open, closed, startpoint or goalpoint 
					
					if(board[x][y] == -1){
						g2d.setColor((Color.red)); 
					}
					else if(board[x][y] == 1){
						g2d.setColor((Color.green)); 
					}
					else{
						g2d.setColor((Color.white));
					}
					
					if(agentPos[0] == x && agentPos[1] == y){
						g2d.setColor((Color.yellow));
					}
					
					g2d.fill(new Rectangle(posX,posY,22,22));
					
					// Sets letter based on weather it is start/goal/path/open/closed -marked node
					g2d.setColor(Color.black); 
					if(agentPos[0] == x && agentPos[1] == y){
						String d = ""; 
						if(dir == 0){
							d = "v"; 
						}
						else if(dir == 1){
							d = ">"; 
						}
						else if(dir == 2){
							d = "^"; 
						}
						else if(dir == 3){
							d = "<"; 
						}
						g2d.drawString(d, posX +14, posY +22);
					}
						
						
			
					
					//Creates a grid around the cell
					g2d.setColor(Color.gray); 
					g2d.drawLine(posX, posY, posX +22, posY); 
					g2d.drawLine(posX, posY, posX, posY +22); 
					
					posX = posX + 22;  
				}
				
				posY = posY - 22;   	
				 
			
			}		 
	}
		


}
