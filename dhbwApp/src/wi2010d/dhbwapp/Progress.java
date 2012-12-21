package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Init;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;

public class Progress extends Activity {
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load everything in a new thread
		Init.resetInstance(); // Async Tasks can only be executed once, so we
								// delete it before starting
		Init init = Init.getInstance(getApplicationContext(), this);
		init.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.progress_screen, menu);
		return true;
	}

}
