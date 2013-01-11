package wi2010d.dhbwapp;

import android.os.Bundle;

public class HelpSettingsScreen extends OnResumeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_settings_screen);
	}

}
