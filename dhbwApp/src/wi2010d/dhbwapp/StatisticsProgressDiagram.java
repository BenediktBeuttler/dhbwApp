package wi2010d.dhbwapp;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;

/**
 * Draws the values into a graphical diagram
 */
public class StatisticsProgressDiagram {
/**
 * sets the X and Y Values to call an intent which draws the graphical diagram
 * 
 * @param context	The Context of the Application
 * @param sureY	The Y-Value of the cards which are in the drawer sure
 * @param notSureY	The Y-Value of the cards which are in the drawer not sure
 * @param dontKnowY	The Y-Value of the cards which are in the drawer dont know
 * @param x	The X-Value of the runthrough at position x
 * @return	Intent, to start Activity
 */
	public Intent getDiagram(Context context, int[]sureY, int[]notSureY, int[]dontKnowY, int[]x){
		
		//set a series which displays the 3 graphs
		TimeSeries dontKnowLine = new TimeSeries("Don't know");
		for (int i = 0; i < x.length; i++) {
			//LineGraph for the drawer to be displayed
			dontKnowLine.add(x[i], dontKnowY[i]);
		}
		TimeSeries notSureLine = new TimeSeries("Not Sure");
		for (int i = 0; i < x.length; i++) {
			//LineGraph for the drawer to be displayed
			notSureLine.add(x[i], notSureY[i]);
		}
		TimeSeries sureLine = new TimeSeries("Sure");
		for (int i = 0; i < x.length; i++) {
			//LineGraph for the drawer to be displayed
			sureLine.add(x[i], sureY[i]);
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		//add the lines to the series
		dataset.addSeries(dontKnowLine);
		dataset.addSeries(notSureLine);
		dataset.addSeries(sureLine);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); 
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		
		//call the graphActivity to draw everything
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, mRenderer, "Progress Diagram");
		return intent;

	}
	
	
}
