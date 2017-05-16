package flatlandv1;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;


public class FlatlandGame {
	
	int[][] grid = new int[10][10]; 
	int[] agentStartPos = {3,3}; 
	int agentStartDirection = 3; 
	int[][] food  = new int[33][2]; 
	int[][] poison  = new int[22][2]; 
	int simSpeed; 
	
	
	public void createRandomBoard(){ 
		grid = new int[10][10]; 
		food  = new int[33][2];
		poison  = new int[22][2]; 
		for(int i = 0; i < 33; i++){
			int[] pos = getRandomFreeCell(); 
			grid[pos[0]][pos[1]] = 1; 
			food[i][0] = pos[0];  
			food[i][1] = pos[1];
		}
		for(int i = 0; i < 22; i++){
			int[] pos = getRandomFreeCell(); 
			grid[pos[0]][pos[1]] = -1; 
			poison[i][0] = pos[0];  
			poison[i][1] = pos[1];
		}
	}
		
	
	private int[] getRandomFreeCell(){
		Random rnd = new Random(); 
		int x = rnd.nextInt(grid.length); 
		int y = rnd.nextInt(grid.length); 
		int[] pos = {x,y};
		if(grid[x][y] == 0 &&!isForbiden(pos)){ 
			return pos;   
		}
		else{
			return getRandomFreeCell(); 
		}
	}
	
	public boolean isForbiden(int[] pos){
		int[][] forbiddenList = {{3,3}, {3,4}, {3,2}, {3,5}, {4,3}, {2,3}};  
		for(int i = 0; i< forbiddenList.length; i++){
			if(pos[0] == forbiddenList[i][0] && pos[1] == forbiddenList[i][1]){
				return true; 
			}
		}
		return false; 
		
	}
	
	
	public double tryAgent(EANN agent, int timeSteps){
		double score = 0;
		int eatenPoison = 0; 
		
		int[] agentPos = {agentStartPos[0], agentStartPos[1]}; 
		int agentDirection = agentStartDirection; 
		
		for(int i = 0; i < timeSteps; i++){
			int[][] surrCordinates = getSurroundings(agentPos, agentDirection); 
			int[] sense = senseSurroundings(surrCordinates);
			int motordelta = agent.getMotorAction(sense);
			int[] newPos = surrCordinates[motordelta+1];
			
			if(grid[newPos[0]][newPos[1]] == 1){ // Food found!
				score++;
				grid[newPos[0]][newPos[1]] = 0; 
			}
			if(grid[newPos[0]][newPos[1]] == -1){ // Eats poison 
				grid[newPos[0]][newPos[1]] = 0;

				if(!(sense[0] == -1 && sense[1] == -1 && sense[2] == -1)){ // eats poison without needing to
					eatenPoison++;
					score -= (((eatenPoison+1) * (eatenPoison+1)) ); 
					if(eatenPoison == 4){
						return 0;
					}	
				}	
			}
			agentPos = newPos; 
			agentDirection = (agentDirection + motordelta) % 4; 
			if(agentDirection == -1){
				agentDirection = 3;  
			}
			
			//System.out.println(agentPos[0] + ","+agentPos[1]); 
			
			
			
			
		}
		
		
		
		return score; 
	}
	
	
	private int[] senseSurroundings(int[][] surr){
		int[] sense = new int[3]; 
		for(int i = 0; i< 3; i++){
			
			sense[i] = grid[surr[i][0]][surr[i][1]]; 
		}
		return sense; 
	}
	
	
	private int[][] getSurroundings(int[] agentPos, int direction){
		int[][] surr = {{agentPos[0], (agentPos[1] +9) % 10}, {(agentPos[0]+1) % 10, agentPos[1]}, {agentPos[0], (agentPos[1] +1)% 10}, {(agentPos[0]+9) % 10, agentPos[1]}};

		
		int[][] relativeSurr = new int[3][2]; 
		if(direction == 0){
			relativeSurr[0] = surr[3]; 
			relativeSurr[1] = surr[direction];
			relativeSurr[2] = surr[direction +1];
		}
		if(direction == 1 || direction == 2){
			relativeSurr[0] = surr[direction -1]; 
			relativeSurr[1] = surr[direction];
			relativeSurr[2] = surr[direction +1];
		}
		else if(direction == 3){
			relativeSurr[0] = surr[direction -1]; 
			relativeSurr[1] = surr[direction];
			relativeSurr[2] = surr[0];
		}
		
		
		
		return relativeSurr; 
		
	}
	
	public void reGenerateBoard(){
		grid = new int[10][10]; 
		for(int i = 0; i < 33; i++){
			grid[food[i][0]][food[i][1]] = 1;   
			if(i < 22){ 
				grid[poison[i][0]][poison[i][1]] = -1; 
			}
		}
		
	}


	public double tryAgentSimulation(EANN agent, int timeSteps) {
		double score = 0;
		int eatenPoison = 0; 
	
		int[] relativeError = new int[2]; 
		
		int[] agentPos = {agentStartPos[0], agentStartPos[1]}; 
		int agentDirection = agentStartDirection; 
		
	   	JFrame frame = new JFrame();
	   	FlatlandGUI flGUI = new FlatlandGUI(); 
	   	flGUI.updateInfo(grid, agentPos, agentDirection); 
	    frame.add(flGUI);
	    frame.setSize(250, 350);
	    frame.setVisible(true);
	    frame.repaint(); 
	    
	    JSlider timeSlider = new JSlider(50,250);
	    timeSlider.setValue(150); 
	    timeSlider.setPaintLabels(true);
	    timeSlider.setMajorTickSpacing(50);
	    JPanel sliderPanel = new JPanel(); 
	    sliderPanel.add(timeSlider);
	    JFrame sliderFrame = new JFrame(); 
	    sliderFrame.add(sliderPanel); 
	    sliderFrame.setSize(350, 150);
	    sliderFrame.setLocation(0, 400);
	    sliderFrame.setVisible(true);
		
	    double[] stats = new double[2]; 
	    
		for(int i = 0; i < timeSteps; i++){
			int[][] surrCordinates = getSurroundings(agentPos, agentDirection); 
			int[] sense = senseSurroundings(surrCordinates); 
			int motordelta = agent.getMotorAction(sense); 
			int[] newPos = surrCordinates[motordelta+1];
			
			if(grid[newPos[0]][newPos[1]] == 1){ // Food found!
				score++;
				stats[0]++; 
				grid[newPos[0]][newPos[1]] = 0; 
			}
			
			else if(grid[newPos[0]][newPos[1]] == -1){ // Eats poison 
				grid[newPos[0]][newPos[1]] = 0;
				stats[1]++; 
				if(!(sense[0] == -1 && sense[1] == -1 && sense[2] == -1)){ // eats poison without needing to
					relativeError[0]++;
					eatenPoison++;
					score -= (((eatenPoison+1) * (eatenPoison+1)) ); 
					if(eatenPoison == 4){
						return 0;
					}	
				}	
			}
			else{
				if(sense[0] == 1 || sense[1] == 1 || sense[2] == 1){ // does not eat food when food in range
					System.out.println("does not eat food when food in range"); 
					relativeError[1]++; 
				}	
			}

			agentPos = newPos; 
			agentDirection = (agentDirection + motordelta) % 4; 
			if(agentDirection == -1){
				agentDirection = 3;  
			}
			
			flGUI.updateInfo(grid, agentPos, agentDirection);
			frame.repaint(); 
			waitForTime(timeSlider.getValue()); 	
		}
		
		System.out.println("Stats: food:" + stats[0] + " , poison" + stats[1]); 
		System.out.println("posionError:" + relativeError[0] + " , foodError:" + relativeError[1]); 
		
		
		return score; 
	}
	
	
	public void waitForTime(long mmsTime){
		long timeNow = System.currentTimeMillis(); 
		while(System.currentTimeMillis() < timeNow + mmsTime){
		}
	}
	
	
	

	
	
	
	

	

}
