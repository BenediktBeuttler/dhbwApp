package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.DatabaseManager;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EditScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_screen);
		
		DatabaseManager.getInstance().deleteDB();
	/*	Stack.allStacks.clear();
		Card.allCards.clear();
		Tag.allTags.clear();
		Runthrough.allRunthroughs.clear();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_screen, menu);
		return true;
	}

}
