package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Init;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;

public class Progress extends Activity {
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Init init = Init.getInstance(getApplicationContext(), this);
		// if the data hasn't been loaded, do it now
		if (!Init.runComplete) {
			init.execute();
		}

		// if the data has been loaded, proceed to the main screen
		else {
			new ErrorHandler(getApplicationContext());
			Intent i = new Intent(getApplicationContext(), StartScreen.class);
			startActivity(i);
			finish();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.progress_screen, menu);
		return true;
	}

}
