package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Tag;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The Administration Screen. This Screen offers buttons to Create a New Card
 * and/or Stack, Edit a Card, Edit a Stack, Import / Export, create a new
 * dynamic Stack and update all dynamic stacks.
 */
public class AdminScreen extends OnResumeActivity implements OnClickListener {
	// Create the Buttons
	private Button new_card_new_stack, edit_stack, import_export,
			new_dyn_stack, update_dyn_stacks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();

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
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpScreen.class));
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
			return true;
		default:
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(this.getFragmentManager(), "dialog");
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
			// only start activity, if there are Tags available
			boolean tagsAvailable = false;
			for (Tag tag : Tag.allTags) {
				if (tag.getTotalCards() > 0) {
					tagsAvailable = true;
					break;
				}
			}
			if (tagsAvailable) {
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
			// create dialog to insert name of new stack
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Update dynamic Stacks");
			alert.setMessage("This function checks if there are any new Cards containing the " +
					"Tags of the selected dynamic Stack and adds them to this one. " +
					"Do you really want to update your dynamic stacks?");
			alert.setIcon(R.drawable.question);
			// Set the new Stack name
			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// This function checks if all dynamic stacks with a
							// certain tag
							// have all cards with this certain tag and updates
							// them
							Create.getInstance().updateDynStacks(); // Update
																	// the
																	// dynamic
																	// stacks
							Toast toast = Toast
									.makeText(
											getApplicationContext(),
											"All dynamic Stacks have been updated successfully",
											Toast.LENGTH_SHORT);
							toast.show();
						}
					});

			alert.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
			alert.show();

			break;
		default:
			break;
		}

	}
}
