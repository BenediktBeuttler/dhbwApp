package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.model.Tag;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * In this activity the AdminTagListFragment is used. A new dynamic Stack can be
 * created with the chosen Tags. For the name of the stack a generated name is
 * offered.
 */
public class AdminCreateDynamicStack extends OnResumeFragmentActivity {

	String name = "";
	ArrayList<Tag> dynStackTagList = new ArrayList<Tag>();
	Fragment tagList;
	Context createDynamicStackContext;
	Button createDynStack;
	boolean buttonInvisible = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();

		setContentView(R.layout.admin_create_dynamic_stack);
		super.onCreate(savedInstanceState);
		createDynamicStackContext = this;

		if (savedInstanceState == null) {

			// set all tags unchecked, then create the list
			for (Tag tag : Tag.allTags) {
				tag.setChecked(false);
			}

			tagList = new AdminTagListFragment();
			getIntent().putExtra("buttonInvisible", buttonInvisible);
			tagList.setArguments(getIntent().getExtras());
			// Create the TagListFragment and bind it to the layout
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();

			fragmentTransaction.add(R.id.layout_admin_create_dyn_4_taglist,
					tagList).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_create_dynamic_stack, menu);
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
		case R.id.btn_admin_create_dynamic_stack:
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("New Stack");
			alert.setMessage("Insert Stack Name");

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			int i = 0;
			for (Tag tag : Tag.allTags) {
				if (tag.isChecked()) {
					dynStackTagList.add(tag);
					if (input.getText().toString().equals("")) {
						if (i == 0) {
							name = tag.getTagName();
						}
						if (i > 0 && i <= 3) {
							name = name + " - " + tag.getTagName();
						}
						i++;
					} else {
						name = input.getText().toString();
					}
				}
			}
			input.setText(name);
			alert.setView(input);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if (dynStackTagList.size() > 0) {
								setResult(RESULT_CANCELED);

								// Start the Dynamic Stack Creation in a new
								// Runnable, so the GUI won't freeze
								Runnable r = new Runnable() {

									@Override
									public void run() {
										if (Create.getInstance().newDynStack(
												input.getText().toString(),
												dynStackTagList)) {
											Toast toast = Toast.makeText(
													getApplicationContext(),
													"Dynamic Stack "
															+ input.getText()
																	.toString()
															+ " created!",
													Toast.LENGTH_LONG);
											toast.show();
											setResult(RESULT_OK);
										}
									}
								};
								r.run();
								finish();
							}
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});
			alert.show();

			return true;
		default:
			return false;
		}
	}
}