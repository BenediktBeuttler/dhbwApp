package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Init;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

/**
 * The Loading Screen when starting the App. It starts the Init Async Task,
 * which adds the Loading Circle to this Activity.
 */
public class Progress extends Activity {
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load everything in a new thread
		Init.resetInstance(); // Async Tasks can only be executed once, so we
								// delete it before starting
		Init.getInstance(getApplicationContext(), this).execute();
	}

}
