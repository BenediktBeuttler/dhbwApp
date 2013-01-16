package wi2010d.dhbwapp;

import java.text.SimpleDateFormat;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

/**
 * Draws the values into a graphical diagram
 */
public class StatisticsProgressDiagram {
	/**
	 * sets the X and Y Values to call an intent which draws the graphical
	 * diagram
	 * 
	 * @param context
	 *            The Context of the Application
	 * @param sureY
	 *            The Y-Value of the cards which are in the drawer sure
	 * @param notSureY
	 *            The Y-Value of the cards which are in the drawer not sure
	 * @param dontKnowY
	 *            The Y-Value of the cards which are in the drawer dont know
	 * @param x
	 *            The X-Value of the runthrough at position x
	 * @return Intent, to start Activity
	 */
	public Intent getDiagram(Context context, int[] sureY, int[] notSureY,
			int[] dontKnowY, long[] x) {

		int maxX = 0;
		boolean add;
		for (int i = 0; i < x.length; i++) {
			// get max X Value
			if (x[i] > 0) {
				maxX = maxX + 1;
			}
		}
		Log.e("maxX", "" + maxX);
		if (maxX < 10) {
			add = true;
		} else {
			add = false;
		}

		// set a series which displays the 3 graphs
		TimeSeries dontKnowLine = new TimeSeries("Don't know");
		for (int i = 0; i < x.length; i++) {
			// LineGraph for the drawer to be displayed
			dontKnowLine.add(i, 100);
		}
		TimeSeries notSureLine = new TimeSeries("Not Sure");
		for (int i = 0; i < x.length; i++) {
			if (i == 0 && add) {
				notSureLine.add(0, 0);
			} else {
				if (add) {
					// LineGraph for the drawer to be displayed
					notSureLine.add(i, notSureY[i - 1] + sureY[i - 1]);
				} else {
					notSureLine.add(i, notSureY[i] + sureY[i]);
				}

			}
		}
		TimeSeries sureLine = new TimeSeries("Sure");
		for (int i = 0; i < x.length; i++) {
			if (i == 0 && add) {
				sureLine.add(0, 0);
			} else {
				if (add) {
					// LineGraph for the drawer to be displayed
					sureLine.add(i, sureY[i - 1]);
				} else {
					sureLine.add(i, sureY[i]);
				}
			}
		}

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// add the lines to the series
		dataset.addSeries(dontKnowLine);
		dataset.addSeries(notSureLine);
		dataset.addSeries(sureLine);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		XYSeriesRenderer renderer1 = new XYSeriesRenderer();
		renderer1.setColor(Color.RED);
		renderer1.setLineWidth(1);
		renderer1.setFillBelowLine(true);
		renderer1.setFillBelowLineColor(Color.RED);
		renderer1.setChartValuesTextSize(20);
		mRenderer.addSeriesRenderer(renderer1);

		XYSeriesRenderer renderer2 = new XYSeriesRenderer();
		renderer2.setColor(Color.YELLOW);
		renderer2.setLineWidth(1);
		renderer2.setFillBelowLine(true);
		renderer2.setFillBelowLineColor(Color.YELLOW);
		mRenderer.addSeriesRenderer(renderer2);

		XYSeriesRenderer renderer3 = new XYSeriesRenderer();
		renderer3.setColor(Color.GREEN);
		renderer3.setLineWidth(1);
		renderer3.setFillBelowLine(true);
		renderer3.setFillBelowLineColor(Color.GREEN);
		mRenderer.addSeriesRenderer(renderer3);

		mRenderer.setMargins(new int[] { 40, 40, 40, 40 });
		if (maxX == 10) {
			mRenderer.setPanLimits(new double[] { 0, 9, 0, 100 });
		} else {
			mRenderer.setPanLimits(new double[] { 0, maxX, 0, 100 });
		}
		// mRenderer.setZoomLimits(new double[] { -10, 20, -10, 40 });

		// set the Background Color
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setMarginsColor(Color.BLACK);

		// set value for x axis
		if (maxX == 10) {
			mRenderer.setXLabels(maxX - 1);
			mRenderer.setXAxisMax(maxX - 1, 0);
		} else {
			mRenderer.setXLabels(maxX);
			mRenderer.setXAxisMax(maxX, 0);
		}
		mRenderer.setXLabelsAlign(Align.CENTER);

		// set value for y axis
		mRenderer.setYLabels(10);
		mRenderer.setYTitle("in %", 0);
		mRenderer.setAxisTitleTextSize(20);
		mRenderer.setYAxisMin(0, 0);
		mRenderer.setYAxisMax(100, 0);
		mRenderer.setYAxisAlign(Align.LEFT, 0);
		mRenderer.setYLabelsAlign(Align.LEFT, 0);
		mRenderer.setLegendTextSize(20);
		mRenderer.setLabelsTextSize(20);

		// call the graphActivity to draw everything
		Intent intent = ChartFactory.getLineChartIntent(context, dataset,
				mRenderer, "Progress Chart");
		return intent;

	}

}
