package wi2010d.dhbwapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * Stores the graphic values (X,Y) and starts / initialize the graphical diagram
 */

public class StatisticsProgressDiagramActivity extends Activity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		StatisticsProgressDiagram line = new StatisticsProgressDiagram();
		//get the X and Y values for the diagram
		int[]notSureY = getIntent().getExtras().getIntArray("notSureY");
		int[]sureY = getIntent().getExtras().getIntArray("sureY");
		int[]dontKnowY = getIntent().getExtras().getIntArray("dontKnowY");
		//TODO: X value is missing
		int[]x = null;
		
		Intent lineIntent = line.getDiagram(this, notSureY, sureY, dontKnowY, x);
		
		//start the actual graphical diagram activity
		startActivity(lineIntent);
		finish();
	}
}
