package wi2010d.dhbwapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LearningCardFront extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_card_front);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_card_front_screen, menu);
		return true;
	}

}
