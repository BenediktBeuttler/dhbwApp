package wi2010d.dhbwapp;

import org.achartengine.GraphicalView;

import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * Stores the graphic values (X,Y) and starts / initialize the graphical diagram
 */

public class StatisticsProgressDiagramActivity extends OnResumeActivity {
	private LinearLayout ll;

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
		ll = new LinearLayout(getApplicationContext());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);
		ll.addView(gv);
		this.addContentView(ll, layoutP);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// if the view is null, we need to finish the activity, because we
		// cannot get the Extras-Data anymore
		if(ll==null){
			finish();
		}
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
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpLearnStatisticsScreen.class));
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
