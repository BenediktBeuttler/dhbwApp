package wi2010d.dhbwapp;

import java.io.File;
import java.util.ArrayList;

import wi2010d.dhbwapp.control.Delete;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.control.Exchange;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Stack;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class LearningChooseStackScreen extends Activity implements
		OnClickListener {

	EditText card_front;
	Button createDynStack;
	Boolean fromLearning = true;
	ArrayList<String> items = new ArrayList<String>();
	ListView lv;
	ArrayAdapter<String> lvAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_choose_stack_screen);
		createDynStack = (Button) findViewById(R.id.btn_learning_create_dyn_stack);
		createDynStack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						AdminCreateDynamicStack.class);
				startActivityForResult(i, 1);
			}
		});

		items = updateStack();

		lv = (ListView) findViewById(R.id.learn_stack_list);
		lvAdapter = new ArrayAdapter<String>(this, R.layout.layout_listitem,
				items);
		lv.setAdapter(lvAdapter);
		// tell android that we want this view to create a menu when it is long
		// pressed. Method onCreateContextMenu is further relevant
		registerForContextMenu(lv);
		lv.setClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String stackName = ((TextView) v).getText().toString();

				if (!stackName.equals("No stacks available")) {
					Intent i = new Intent(getApplicationContext(),
							LearningCard.class);
					i.putExtra("stackName", stackName);
					startActivity(i);
				}

			}
		});
	}

	// When the registered view receives a long-click event, the system calls
	// the onCreateContextMenu() method. This is where the menu items are
	// defined.
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		String sName = ((TextView) info.targetView).getText().toString();
		boolean isDynamic = false;
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(sName)) {
				if (stack.isDynamicGenerated()) {
					isDynamic = true;
				}
			}
		}
		if (isDynamic) {
			menu.add(0, v.getId(), 0, "Start Learning");
			menu.add(0, v.getId(), 1, "Change Name and Tags");
			menu.add(0, v.getId(), 2, "Reset Anwsers");
			menu.add(0, v.getId(), 3, "Delete");
			menu.add(0, v.getId(), 4, "Archive");
		} else 
			menu.add(0, v.getId(), 0, "Start Learning");
			menu.add(0, v.getId(), 1, "Change Name");
			menu.add(0, v.getId(), 2, "Reset Anwsers");
			menu.add(0, v.getId(), 3, "Delete");
			menu.add(0, v.getId(), 4, "Archive");
		}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info;
		info = (AdapterContextMenuInfo) item.getMenuInfo();
		final String stackName = ((TextView) info.targetView).getText()
				.toString();

		if (!stackName.equals("No stacks available")) {
			if (item.getTitle() == "Change Name") {

				// pass the stack name to the edit activity and whether it is an
				// dynamic generated stack or not, edit it


				//TODO: BENE, hier PopUp einfügen
			} else if (item.getTitle() == "Start Learning") {
			
				if (!stackName.equals("No stacks available")) {
					Intent i = new Intent(getApplicationContext(),
							LearningCard.class);
					i.putExtra("stackName", stackName);
					startActivity(i);
				}

			}
			else if (item.getTitle() == "Change Name and Tags") {
				// pass the stack name to the edit activity and whether it is an
				// dynamic generated stack or not, edit it

				Intent i = new Intent(getApplicationContext(),
						AdminEditDynamicStack.class);
				i.putExtra("stackName", stackName);
				i.putExtra("buttonInvisible", true);
				startActivityForResult(i, 1);

			} else if (item.getTitle() == "Reset Drawer") {
				// all cards of this stack get reseted and set back to the
				// drawer:
				// don't know
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				// set title
				alertDialogBuilder.setTitle("Delete Card");
				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to reset all cards in this stack to 'don't know'?")
						.setIcon(R.drawable.question)
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Stack clickedStack = null;
										for (Stack stack : Stack.allStacks) {
											if (stack.getStackName().equals(
													stackName)) {
												clickedStack = stack;
												break;
											}
										}

										// reset all cards to don't know
										Edit.getInstance().resetDrawer(
												clickedStack);
										Toast toast = Toast
												.makeText(
														getApplicationContext(),
														stackName
																+ " has been resetted successfully",
														Toast.LENGTH_SHORT);
										toast.show();
										setResult(AdminChooseStackScreen.RESULT_OK);

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			} else if (item.getTitle() == "Delete") {
				// Delete the selected stack, after asking the user

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				// set title
				alertDialogBuilder.setTitle("Delete Card");
				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to delete this stack?")
						.setIcon(R.drawable.question)
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Stack clickedStack = null;
										for (Stack stack : Stack.allStacks) {
											if (stack.getStackName().equals(
													stackName)) {
												clickedStack = stack;
												break;
											}
										}

										// Delete the stack
										Delete.getInstance().deleteStack(
												clickedStack);

										// Update the stackList
										updateStack();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			} else if (item.getTitle() == "Archive") {
				Stack clickedStack = null;
				for (Stack stack : Stack.allStacks) {
					if (stack.getStackName().equals(stackName)) {
						clickedStack = stack;
						break;
					}
				}
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_UNMOUNTED)
						|| !Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED_READ_ONLY)
						|| new File(Environment.getExternalStorageDirectory()
								.getPath() + "/knowItOwl/").canWrite()) {
					try {
						Exchange.getInstance().exportStack(
								clickedStack,
								Environment.getExternalStorageDirectory()
										.getPath() + "/knowItOwl/", stackName);

						Delete.getInstance().deleteStack(clickedStack);

						updateStack();

						Toast toast = Toast.makeText(getApplicationContext(),
								stackName + " archived successfully!",
								Toast.LENGTH_SHORT);
						toast.show();
					} catch (Exception e) {
						// TODO Bisl ErrorBeuttlern ExportError
						ErrorHandlerFragment newFragment = ErrorHandlerFragment
								.newInstance(R.string.error_handler_general, ErrorHandlerFragment.GENERAL_ERROR );
						newFragment.show(this.getFragmentManager(), "dialog");	
					}
				} else {
					//SD KArte nicht gefunden!
					ErrorHandlerFragment newFragment = ErrorHandlerFragment
							.newInstance(R.string.error_handler_no_sd, ErrorHandlerFragment.NO_SD );
					newFragment.show(this.getFragmentManager(), "dialog");	
				}
			} else {
				return false;
			}
		}

		return true;
	}

	private ArrayList<String> updateStack() {
		items.clear();
		for (Stack stack : Stack.allStacks) {
			items.add(stack.getStackName());
		}
		if (items.size() == 0) {
			items.add("No stacks available");
		}
		return items;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			items = updateStack();
			lvAdapter = new ArrayAdapter<String>(this,
					R.layout.layout_listitem, items);
			lv.setAdapter(lvAdapter);
		}
		if (resultCode == RESULT_CANCELED) {
			// nothing happens if result is canceled
		}
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learning_screen, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, LearningCard.class));
	}

}