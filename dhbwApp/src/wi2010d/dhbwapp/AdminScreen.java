package wi2010d.dhbwapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AdminScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_screen, menu);
		return true;
	}

}
