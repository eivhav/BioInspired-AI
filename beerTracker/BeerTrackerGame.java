package beerTracker;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import flatlandv1.FlatlandGUI;
import flatlandv1.PhenoType;

public class BeerTrackerGame{
	
	int[][] world = new int[30][15]; 
	int[] objectPos = new int[2]; 
	int objectSize = 3; 
	int objectCount = 0;
	int[][] objectSeqeunce; 
	int agentPos = 15;
	boolean wrapAround = true; 
	boolean pullAllowed = false; 
	int timeSteps = 600; 
	double score; 
	
	boolean recentlyPulled = false; 
	
	boolean threadCompleted = false; 
	public PhenoType pt = null; 
	public boolean running = true; 
	
	double[] stats = new double[4]; // Captures, avoidance, miss, crash 
	
	double[] runscore1Weights = {1.4, 1.8, -0.7, -0.8};   
	double[] runscore2Weights = {1.3, 1.8, -0.7, -0.8}; 
	double[] runscore3Weights = {2, 1, -0.3, -0.8}; 
	
	double[] rWeigths; 
	 
	CTRNN agent; 
	
	public BeerTrackerGame(int noOfinputNodes, int noOfOutputNodes, int[] hiddenTopologi, int weightRange, int task, int[][] objectSeqeunce){
		agent = new CTRNN(hiddenTopologi, noOfinputNodes, noOfOutputNodes, weightRange); 
		rWeigths = runscore1Weights; 
		if(task > 1){
			pullAllowed = true; 
			rWeigths = runscore2Weights; 
		}
		if(task > 2){
			wrapAround = false; 
			pullAllowed = false;
			rWeigths = runscore3Weights; 
		}
		this.objectSeqeunce = objectSeqeunce; 
		
		
	}
	
	
	public double runAgent(boolean sim, PhenoType pt){ 
		agent.updateWeights(pt.getSymbols());
		agentPos = 15; 
		score = 0; 
		stats = new double[5]; 
		createNewObject(sim, false); 
		JFrame frame = new JFrame();
		BeerTrackerGUI brGUI = new BeerTrackerGUI();
		JSlider timeSlider = new JSlider(50,250);
		if(sim){
		   	brGUI.updateInfo(world, agentPos, objectSize, recentlyPulled); 
		    frame.add(brGUI);
		    frame.setSize(750, 450);
		    frame.setVisible(true);
		    frame.repaint(); 
		    timeSlider.setValue(50); 
		    timeSlider.setPaintLabels(true);
		    timeSlider.setMajorTickSpacing(50);
		    JPanel sliderPanel = new JPanel(); 
		    sliderPanel.add(timeSlider);
		    JFrame sliderFrame = new JFrame(); 
		    sliderFrame.add(sliderPanel); 
		    sliderFrame.setSize(400, 150);
		    sliderFrame.setLocation(0, 475);
		    sliderFrame.setVisible(true);
	    
		}
		
		for(int t = 0; t < timeSteps; t++){
			
			double[] sense; 
			if(wrapAround){
				sense = new double[5]; 
			}
			else{
				sense = new double[7]; 
				Random rnd = new Random(); 
				double oscValue = 1 ; 
				if(agentPos == 0){
					sense[6] = oscValue; 
				}
				else if(agentPos == 25){
					sense[5] = oscValue; 
				}
			}
			
			for(int i = 0; i < objectSize; i++){
				world[(objectPos[0]+i+30) % 30][objectPos[1]] += 1; 
			}
			
			for(int i = 0; i < 5; i++){	
				sense[i] =  world[(agentPos + i+30) % 30][objectPos[1]]; 
			}
			
			int[] agentAction  = agent.getMotorAction(sense, false); 
			if(wrapAround){
				agentPos += agentAction[0];
				agentPos= (agentPos+30) % 30; 
			}
			else{
				agentPos += agentAction[0];
				if(agentPos < 0){
					agentPos = 0; 
				}
				else if(agentPos > 25){
					agentPos = 25; 
				}
			}
			
			for(int i = 0; i < 5; i++){	
				world[(agentPos+i+30) % 30][0] += 2; 
			}
			
		
		
			
			if(sim){
				brGUI.updateInfo(world, agentPos, objectSize, recentlyPulled);
				frame.repaint(); 
				waitForTime(timeSlider.getValue()); 
			}
		
		
			if(objectPos[1] == 0){
				updateScore(sim);
				createNewObject(sim, false); 	
			}
			
			if(pullAllowed && agentAction[1] == 1){
					recentlyPulled = true; 
					objectPos[1] = 0; 
					if(sim){
						System.out.println("Pull");
					}
				
			}
			else{
				objectPos[1]--;
				recentlyPulled = false; 
			}
			
			
			world = new int[30][15];
			
		}
		if(sim){
			System.out.println("Stats: Captures, avoidance, miss, crash  ");
			System.out.println(stats[0] + ", " + stats[1] + ", " + stats[2] + ", " + stats[3]);
		}
		
		return score; 
		//return (Math.pow(stats[0], 1.65)) + (Math.pow(stats[1], 1.5)) - (Math.pow(stats[2], 1.6)) - (Math.pow(stats[3], 1.7));
		// Good run problem 1 : 
		//return Math.pow(stats[0], 1.8) + Math.pow(stats[1], 1.7) - (Math.pow(stats[2], 2.5) / stats[0]) - (Math.pow(stats[3], 2.5) / stats[1]) ;
		//return Math.pow(stats[0], 2.5) + Math.pow(stats[1], 2) - (Math.pow(stats[2], 3) / (stats[0] + stats[1])) - (Math.pow(stats[3], 2) * (stats[0] + stats[1])) ;
	}
	
	
	public void updateScore(boolean sim){
		
		if(objectSize > 4){
			boolean crash = false; 
			for(int i = 0; i < 5; i++){ 
				if((world[(agentPos +i +30) % 30][0] == 3)){
					crash = true; 
					stats[3]++; 
					score += rWeigths[3]; //0.8				3:0.8
					if(recentlyPulled){
						stats[4]++; 
					}
					break; 
				}
			}
			if(!crash){
				stats[1]++; 
				score += rWeigths[1]; //1.8					3: 1
				if(recentlyPulled){
					//score += 0.5; 
				}
			}
		}
		
		else{
			boolean capture = true; 
			for(int i = 0; i < objectSize; i++){ 
				if(!(world[(objectPos[0] +i) % 30][0] == 3)){
					capture = false; 
					stats[2]++;
					score += rWeigths[2];   //0.7 		3: 0.3
					if(recentlyPulled){
						score -= 2; 
					}
					break; 
				}
			}
			if(capture){
				score += rWeigths[0]; //1.3 			3: 2
				stats[0]++;
				if(recentlyPulled){
					score += 3; 
				}
			}
		}
			
	}
	
	
	
	public void createNewObject(boolean sim, boolean staticSeq){ 
		if(!sim && staticSeq){
			objectPos[0] = objectSeqeunce[objectCount][0]; 
			objectSize = objectSeqeunce[objectCount][1]; 
		}
		else{
			Random rnd2 = new Random();
			objectSize = rnd2.nextInt(6) +1;
			Random rnd = new Random();
			if(wrapAround){
				objectPos[0] = rnd.nextInt(30); 
			}
			else{
				objectPos[0] = rnd.nextInt(30- objectSize); 
			}
		}
		objectPos[1] = 14;	
	}
	

	public void waitForTime(long mmsTime){
		long timeNow = System.currentTimeMillis(); 
		while(System.currentTimeMillis() < timeNow + mmsTime){
		}
	}
	
	public boolean objectIsOver(){
		for(int i = 0; i < 5; i++){ 
			if(world[(agentPos +i +30) % 30][objectPos[1]] == 1 && objectPos[1] != 0){
				return true; 
			}
		}
		return false; 
		
	}
	
	

}
