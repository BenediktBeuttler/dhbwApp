package wi2010d.dhbwapp;

import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R.layout;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

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
	public Intent getDiagram(Context context, int[]sureY, int[]notSureY, int[]dontKnowY, long[] x){
		
		
		//set a series which displays the 3 graphs
		TimeSeries dontKnowLine = new TimeSeries("Don't know");
		for (int i = 0; i < x.length; i++) {
			//LineGraph for the drawer to be displayed
			dontKnowLine.add(i, dontKnowY[i]);
		}
		TimeSeries notSureLine = new TimeSeries("Not Sure");
		for (int i = 0; i < x.length; i++) {
			//LineGraph for the drawer to be displayed
			notSureLine.add(i, notSureY[i]);
		}
		TimeSeries sureLine = new TimeSeries("Sure");
		for (int i = 0; i < x.length; i++) {
			//LineGraph for the drawer to be displayed
			sureLine.add(i, sureY[i]);
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		//add the lines to the series
		dataset.addSeries(dontKnowLine);
		dataset.addSeries(notSureLine);
		dataset.addSeries(sureLine);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); 
		XYSeriesRenderer renderer1 = new XYSeriesRenderer();
		renderer1.setColor(Color.RED);
		mRenderer.addSeriesRenderer(renderer1);
		XYSeriesRenderer renderer2 = new XYSeriesRenderer();
		renderer2.setColor(Color.WHITE);
		mRenderer.addSeriesRenderer(renderer2);
		XYSeriesRenderer renderer3 = new XYSeriesRenderer();
		renderer3.setColor(Color.BLUE);
		mRenderer.addSeriesRenderer(renderer3);
		mRenderer.setMargins(new int[] {40, 40, 40, 40});
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setPanLimits(new double[] { 10, 10, 10, 100 });
		//mRenderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		
		//call the graphActivity to draw everything
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, mRenderer, "Progress Chart");
		return intent;

	}
	
	
}
