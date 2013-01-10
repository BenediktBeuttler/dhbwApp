package wi2010d.dhbwapp;

import android.os.Bundle;

/**
 * Simple Activity, just showing the help text about usage of this app.
 */
public class HelpIntroductionScreen extends OnResumeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_introduction_screen);
	}

}
