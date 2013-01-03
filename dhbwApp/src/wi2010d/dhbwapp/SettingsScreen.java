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
			showDialog();
			break;
			
		case R.id.btn_write_test_data:
			List<Tag> tags = new ArrayList<Tag>();
			List<Card> cards = new ArrayList<Card>();
			List<Card> cards1 = new ArrayList<Card>();

			Tag mundl = Create.getInstance().newTag("Mündlich");
			Tag presentation = Create.getInstance()
					.newTag("2. PA Präsentation");

			tags.add(mundl);
			Card card1 = Create.getInstance().newCard("Was ist Java?",
					"Java ist eine objektorientierte Programmiersprache.",
					tags, "", "");
			Card card2 = Create.getInstance().newCard(
					"Für was steht die Abkürzung JVM?",
					"Java Virtual Machine.", tags, "", "");
			Card card3 = Create
					.getInstance()
					.newCard(
							"Welche Variabel Typen gibt es?",
							"Boolean, Gleitkomma-Zahl, Ganzzahl-Typen, Char, String, Array.",
							tags, "", "");
			Card card4 = Create.getInstance().newCard(
					"Für was steht die Abkürzung SDK?",
					"Software Development Kit.", tags, "", "");
			Card card5 = Create.getInstance().newCard("Für was steht JRE?",
					"Java Runtime Environment.", tags, "", "");
			Card card6 = Create.getInstance().newCard(
					"Welche Methode kann Objekte erstellen?",
					"Der Konstrukt0r.", tags, "", "");
			cards.add(card1);
			cards.add(card2);
			cards.add(card3);
			cards.add(card4);
			cards.add(card5);
			cards.add(card6);
			Stack stack = new Stack(false, "Java", cards);
			Database.getInstance().addNewStack(stack);

			tags.add(presentation);
			Card card7 = Create.getInstance().newCard("Was ist Java1?",
					"Java ist eine objektorientierte Programmiersprache1.",
					tags, "", "");
			Card card8 = Create.getInstance().newCard("Was ist Java2?",
					"Java ist eine objektorientierte Programmiersprache2.",
					tags, "", "");
			Card card9 = Create.getInstance().newCard("Was ist Java3?",
					"Java ist eine objektorientierte Programmiersprache3.",
					tags, "", "");
			Card card10 = Create.getInstance().newCard("Was ist Java4?",
					"Java ist eine objektorientierte Programmiersprache4.",
					tags, "", "");
			Card card11 = Create.getInstance().newCard("Was ist Java5?",
					"Java ist eine objektorientierte Programmiersprache5.",
					tags, "", "");
			cards1.add(card7);
			cards1.add(card8);
			cards1.add(card9);
			cards1.add(card10);
			cards1.add(card11);
			Stack stack1 = new Stack(false, "EDV-Recht", cards1);
			Database.getInstance().addNewStack(stack1);

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

	void showDialog() {
		ErrorHandlerFragment newFragment = ErrorHandlerFragment
				.newInstance(R.string.error_handler_delete_db, ErrorHandlerFragment.RESET_DB);
		newFragment.show(getFragmentManager(), "dialog");		
	}

}
