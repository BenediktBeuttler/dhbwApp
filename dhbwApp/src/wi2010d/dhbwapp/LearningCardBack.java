package wi2010d.dhbwapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class LearningCardBack extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_card_back);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_card_back_screen, menu);
		return true;
	}

}
