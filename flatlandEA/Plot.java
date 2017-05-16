package flatlandv1;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Plot extends ApplicationFrame{

	
	
	
	
	
	
	public Plot(final String title, ArrayList<double[]> best, ArrayList<double[]> avg, ArrayList<double[]> sd) {

	    super(title);
	    
	
	    	
	    	
	    	
		   
	    XYDataset data = createDataset(best, avg, sd); 
	    
	    JFreeChart chart = ChartFactory.createXYLineChart(
	        "Evolutionary run ",
	        "Generation", 
	        "fitness", 
	        data,
	        PlotOrientation.VERTICAL,
	        true,
	        true,
	        false
	    );

	    ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    setContentPane(chartPanel);
	    
	    

	}
	
	
	 private XYDataset createDataset( ArrayList<double[]> best, ArrayList<double[]> avg, ArrayList<double[]> sd){
	   
	    final XYSeries s1 = new XYSeries("Best");          
	  	for(int i = 0; i < best.size(); i++ ){
    		s1.add(best.get(i)[0], best.get(i)[1]);
    	}
    
		final XYSeries s2 = new XYSeries( "Avg" ); 	
	    for(int i = 0; i < avg.size(); i++ ){
	    	s2.add(avg.get(i)[0], avg.get(i)[1]);
	    }
	    final XYSeries s3 = new XYSeries( "S-deviation" ); 
	    for(int i = 0; i < sd.size(); i++ ){
	    	s3.add(sd.get(i)[0], sd.get(i)[1]);
	    }        
	                
	            
	               
	            
	      final XYSeriesCollection dataset = new XYSeriesCollection( );          
	      dataset.addSeries( s1 );          
	      dataset.addSeries( s2 );          
	      dataset.addSeries( s3 );
	      return dataset;
	   }

	

	
	
	/*
	 * final XYSeriesDemo demo = new XYSeriesDemo("XY Series Demo");
    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);
	 * 
	 * 
	 * 
	 */
	
	
}
