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
		setContentView(R.layout.progress_screen);

		// Initiate DB Manager, Database and load everything
		Init init = Init.getInstance(getApplicationContext());
		init.loadFromDB();
		
		new ErrorHandler(getApplicationContext());
		
		pb = (ProgressBar) findViewById(R.id.progress_bar_progress_screen);

		Intent i = new Intent(getApplicationContext(), StartScreen.class);
		startActivity(i);
		finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.progress_screen, menu);
		return true;
	}

}
