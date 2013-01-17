package wi2010d.dhbwapp;

import java.util.Date;

import org.achartengine.GraphicalView;

import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Runthrough;
import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * Stores the graphic values (X,Y) and starts / initialize the graphical diagram
 */

public class StatisticsProgressDiagramActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.statistics_screen_graph);

		StatisticsProgressDiagram line = new StatisticsProgressDiagram();
		// get the X and Y values for the diagram
		int[] notSureY = getIntent().getExtras().getIntArray("notSureY");
		int[] sureY = getIntent().getExtras().getIntArray("sureY");
		int[] dontKnowY = getIntent().getExtras().getIntArray("dontKnowY");
		// get the X-value
		long[] x = getIntent().getExtras().getLongArray("lastDates");

		GraphicalView gv = line.getDiagram(this, sureY, notSureY, dontKnowY, x);
		// start the actual graphical diagram activity
		LayoutParams layoutP = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		LinearLayout ll = new LinearLayout(getApplicationContext());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);
		ll.addView(gv);
		this.addContentView(ll, layoutP);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_start_screen:
			startActivity(new Intent(this, StartScreen.class));
			finish();
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
			finish();
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpLearnStatisticsScreen.class));
			finish();
			return true;
		default:
			// if anything goes wrong in the switchcase show dialog
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(getFragmentManager(), "dialog");
			return false;
		}
	}
}
