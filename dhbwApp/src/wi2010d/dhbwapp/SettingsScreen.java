package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.DatabaseManager;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Tag;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingsScreen extends Activity implements OnClickListener{
	
	Button resetDB, testData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_screen);
		
		resetDB = (Button) findViewById(R.id.btn_reset_database);
		resetDB.setOnClickListener(this);
		
		testData = (Button) findViewById(R.id.btn_write_test_data);
		testData.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_screen, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_reset_database:
			DatabaseManager.getInstance().deleteDB();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Database and all Data deleted!",
					Toast.LENGTH_SHORT);
			toast.show();
			break;
		case R.id.btn_write_test_data:
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(Create.getInstance().newTag("Penis"));

		Create.getInstance().newStack("Stack 1",
		Create.getInstance().newCard("Hallo", "Welt", tags, "", ""));
		Create.getInstance().newStack("Stack 2",
		Create.getInstance().newCard("Front 2", "Back 2", tags, "", ""));
		Create.getInstance().newStack("Stack 3",
		Create.getInstance().newCard("Front 3", "Back dsakjdsalkjdsa 3,5", tags, "", ""));
		Create.getInstance().newStack("Stack 4",
		Create.getInstance().newCard("Front as d4", "lkjdsadsakdsal 4,5", tags, "", ""));
		
		toast = Toast.makeText(getApplicationContext(),
				"Test data written!",
				Toast.LENGTH_SHORT);
		toast.show();
			break;
		
		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			break;
		}
	}
	

}
