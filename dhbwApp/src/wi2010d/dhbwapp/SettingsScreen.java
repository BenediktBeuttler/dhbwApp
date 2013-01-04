package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Database;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingsScreen extends Activity implements OnClickListener {

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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_start_screen:
			startActivity(new Intent(this, StartScreen.class));
			finish();
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpScreen.class));
			finish();
			return true;
		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_reset_database:
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
			.newInstance(R.string.error_handler_delete_db, ErrorHandlerFragment.RESET_DB);
			newFragment.show(getFragmentManager(), "dialog");	
			break;
			
		case R.id.btn_write_test_data:
			
			List<Tag> tags0 = new ArrayList<Tag>();
			List<Tag> tags1 = new ArrayList<Tag>();
			List<Tag> tags2 = new ArrayList<Tag>();
			List<Tag> tags3 = new ArrayList<Tag>();
			List<Tag> tags4 = null;
			
			List<Card> allCards = new ArrayList<Card>();
			
			Tag mundl = Create.getInstance().newTag("Mündlich");
			Tag presentation = Create.getInstance().newTag("2. PA Präsentation");
			Tag nachpruefung = Create.getInstance().newTag("Nachprüfung Recht");
			Tag projektmgmt = Create.getInstance().newTag("Projektmgmt. Zertifizierung");

			tags0.add(mundl);
			tags0.add(presentation);
			tags1.add(nachpruefung);
			tags1.add(projektmgmt);
			tags2.add(nachpruefung);
			tags3.add(projektmgmt);
			tags3.add(mundl);
			tags3.add(presentation);

			for (int i = 0; i < 100; i++){
				
				if (i < 25){
					allCards.add(Create.getInstance().newCard("Card " + i + " front", 
							"Card " + i + " back", tags0, "", ""));
				}
				
				if ((24 < i) && (i < 45)){
					allCards.add(Create.getInstance().newCard("Card " + i + " front", 
							"Card " + i + " back", tags1, "", ""));
				}
				
				if ((44 < i) && (i < 60)){
					allCards.add(Create.getInstance().newCard("Card " + i + " front", 
							"Card " + i + " back", tags2, "", ""));
				}
				
				if ((59 < i) && (i < 80)){
					allCards.add(Create.getInstance().newCard("Card " + i + " front", 
							"Card " + i + " back", tags3, "", ""));
				}
				
				if (i > 79){
					allCards.add(Create.getInstance().newCard("Card " + i + " front", 
							"Card " + i + " back", tags4, "", ""));
				}
			}
			
			int cardNumber = 100;
			int randomNumber;
			List<Card> cards = new ArrayList<Card>();
			
			for (int i = 0; i < 25; i++){
				
				cards.clear();
				
				for (int j = 0; j < 4; j++){
					
					randomNumber = (int)Math.floor((Math.random() * cardNumber));
					cardNumber = cardNumber - 1;
					cards.add(allCards.get(randomNumber));
					allCards.remove(randomNumber);
				}
				
				Database.getInstance().addNewStack(new Stack(false, "Stack " + i, cards));
			}

			Toast toast;
			toast = Toast.makeText(getApplicationContext(),
					"Test data written!", Toast.LENGTH_SHORT);
			toast.show();
			break;

		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			break;
		}
	}
}
