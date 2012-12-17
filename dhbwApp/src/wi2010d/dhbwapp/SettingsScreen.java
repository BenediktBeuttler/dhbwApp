package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.DatabaseManager;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsScreen extends Activity implements OnClickListener{
	
	Button resetDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_screen);
		
		resetDB = (Button) findViewById(R.id.btn_reset_database);
		
		resetDB.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_screen, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_reset_database:
			DatabaseManager.getInstance().deleteDB();
			break;
		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			break;
		}
	}
	

}
