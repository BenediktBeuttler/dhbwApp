package wi2010d.dhbwapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Admin_edit_stack extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_edit_stack_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_edit_stack_screen, menu);
		return true;
	}

}
