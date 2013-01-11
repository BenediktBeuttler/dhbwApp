package wi2010d.dhbwapp;

import android.os.Bundle;

/**
 * Simple Activity, just showing the help text about the usage of learning and statistics.
 */
public class HelpLearnStatisticsScreen extends OnResumeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_learn_statistics_screen);
	}
}
