package boidProject;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class SliderPanel {
	
	Parameters params; 
	JTextField allValue; 
	JTextField cohValue; 
	JTextField sepValue; 
	JSlider allSlider; 
	JSlider cohSlider; 
	JSlider sepSlider; 
	

	
	public SliderPanel(Parameters params){
		this.params = params; 
		
	    JPanel sliderPanel = new JPanel(); 
	    
	    GridLayout sliderLayout = new GridLayout(3,0);
	    sliderPanel.setLayout(sliderLayout);
	 
	    allSlider = new JSlider(-10,30);
	    allSlider.setValue(13); 
	    allSlider.setPaintLabels(true);
	    allSlider.setMajorTickSpacing(5);
	    allSlider.setMinorTickSpacing(1);
	    sliderPanel.add(allSlider);
	    
	    cohSlider = new JSlider(-10, 30);
	    cohSlider.setValue(-1);
	    cohSlider.setPaintLabels(true);
	    cohSlider.setPaintTicks(true);
	    cohSlider.setMajorTickSpacing(5);
	    cohSlider.setMinorTickSpacing(1);
	    sliderPanel.add(cohSlider); 
	    
	    sepSlider = new JSlider(-10,30);
	    sepSlider.setValue(19); 
	    sepSlider.setPaintLabels(true);
	    sepSlider.setPaintTicks(true);
	    sepSlider.setMajorTickSpacing(5);
	    sepSlider.setMinorTickSpacing(1);
	    sliderPanel.add(sepSlider, BorderLayout.SOUTH); 
	    
	    JFrame sliderFrame = new JFrame(); 
	    sliderFrame.add(sliderPanel); 
	    sliderFrame.setSize(250, 250);
	    sliderFrame.setVisible(true);
	    
	    JPanel valuePanel = new JPanel(); 
	    GridLayout valueLayout = new GridLayout(3,2);
	    valuePanel.setLayout(valueLayout);
	    
	    JLabel allTitle = new JLabel("          Allignement:");  
	    JLabel cohTitle = new JLabel("          Cohesion:");
	    JLabel sepTitle = new JLabel("          Seperation:");
	    allValue = new JTextField(Double.toString(params.allignementParam)); 
	    cohValue = new JTextField(Double.toString(params.cohesiontParam)); 
	    sepValue = new JTextField(Double.toString(params.seperationParam));
	    valuePanel.add(allTitle); 
	    valuePanel.add(allValue); 
	    valuePanel.add(cohTitle);
	    valuePanel.add(cohValue);
	    valuePanel.add(sepTitle);
	    valuePanel.add(sepValue);
	    
	    JFrame valueFrame = new JFrame();
	    valueFrame.add(valuePanel); 
	    valueFrame.setSize(250, 250);
	    valueFrame.setVisible(true);
	    		
	}
	
	public void updateValues(){
		params.allignementParam = getConversioValue(allSlider.getValue()); 
		params.cohesiontParam = getConversioValue(cohSlider.getValue()); 
		params.seperationParam = getConversioValue(sepSlider.getValue()); 
	
	    allValue.setText(Double.toString(params.allignementParam)); 
	    cohValue.setText(Double.toString(params.cohesiontParam)); 
	    sepValue.setText(Double.toString(params.seperationParam));
		
		
	}
	
	public double getConversioValue(int v){
		double n = 0; 
		double val = (double) v; 
		if(val > 10)
			n = (val-10) * 10; 
		else if(val > 0){
			n= val; 
		}
		else if(val == 0){
			n = 1; 
		}
		else if(val < 0){
			n = (10+val)/10; 
			
		}
		return n; 	
	}
}
