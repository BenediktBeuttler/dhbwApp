package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Tag;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * The Administration Screen. This Screen offers buttons to Create a New Card
 * and/or Stack, Edit a Card, Edit a Stack, Import / Export, create a new
 * dynamic Stack and update all dynamic stacks.
 */
public class AdminScreen extends OnResumeActivity implements OnClickListener {
	// Create the Buttons
	Button new_card_new_stack;
	Button edit_card;
	Button edit_stack;
	Button import_export;
	Button new_dyn_stack;
	Button update_dyn_stacks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_screen);

		// Initialize the buttons
		new_card_new_stack = (Button) findViewById(R.id.btn_admin_new_card_new_stack);
		edit_stack = (Button) findViewById(R.id.btn_admin_edit_stack);
		import_export = (Button) findViewById(R.id.btn_admin_import_export);
		new_dyn_stack = (Button) findViewById(R.id.btn_admin_new_dyn_stack);
		update_dyn_stacks = (Button) findViewById(R.id.btn_admin_update_dyn_stack);

		// Set the onClickListeners
		new_card_new_stack.setOnClickListener(this);
		edit_stack.setOnClickListener(this);
		import_export.setOnClickListener(this);
		new_dyn_stack.setOnClickListener(this);
		update_dyn_stacks.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_screen, menu);
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
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
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
		case R.id.btn_admin_new_card_new_stack:
			startActivity(new Intent(this, AdminNewCard.class)); // Start
																	// Activity
			break;
		case R.id.btn_admin_edit_stack:
			startActivity(new Intent(this, AdminChooseStackScreen.class)); // Start
																			// Activity
			break;
		case R.id.btn_admin_import_export:
			startActivity(new Intent(this, AdminImportExport.class)); // Start
																		// Activity
			break;
		case R.id.btn_admin_new_dyn_stack:
			if (Tag.allTags.size() != 0 && Card.allCards.size() != 0) { // Start
																		// Activity
																		// when
																		// there
																		// is a
																		// tag
																		// and
																		// card
				Intent i = new Intent(getApplicationContext(),
						AdminCreateDynamicStack.class);
				startActivityForResult(i, 1);
			} else {
				Toast.makeText(getApplicationContext(),
						"No Tags for dynamic stacks available!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btn_admin_update_dyn_stack:
			Create.getInstance().updateDynStacks(); // Update the dynamic stacks
			Toast toast = Toast.makeText(getApplicationContext(),
					"All dynamic Stacks have been updated successfully",
					Toast.LENGTH_SHORT);
			toast.show();
			break;
		default:
			break;
		}

	}
}
