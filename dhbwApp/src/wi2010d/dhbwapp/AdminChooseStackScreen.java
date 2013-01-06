package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Stack;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AdminChooseStackScreen extends Activity {

	public final static int RESULT_OK = 1;
	public final static int RESULT_FAIL = 2;
	private ArrayList<String> items = new ArrayList<String>();
	private ListView lv;
	private ArrayAdapter<String> lvAdapter;

	// tell android that we want this view to create a menu when it is long
	// pressed. Method onCreateContextMenu is further relevant
	// registerForContextMenu(lv);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_choose_stack_screen);

		items = updateStackList();

		lv = (ListView) findViewById(R.id.admin_stack_list);
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
				parent.showContextMenuForChild(v);
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
			menu.add(0, v.getId(), 0, "Change Name and Tags");
			menu.add(0, v.getId(), 1, "Reset Drawer");
			menu.add(0, v.getId(), 2, "Delete");
			menu.add(0, v.getId(), 3, "Archive");
		} else {
			menu.add(0, v.getId(), 0, "Change Name");
			menu.add(0, v.getId(), 1, "Reset Drawer");
			menu.add(0, v.getId(), 2, "Delete");
			menu.add(0, v.getId(), 3, "Archive");
		}
	}

	// Define what happends when the item in list is long pressed
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Change Name") {

			// pass the stack name to the edit activity and wheater it is an
			// dynamic generated stack or not, edit it
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			String stackName = ((TextView) info.targetView).getText()
					.toString();

			if (!stackName.equals("No stacks available")) {
				for (Stack stack : Stack.allStacks) {
					if (stack.getStackName().equals(stackName)) {
						if (stack.isDynamicGenerated()) {
							Intent i = new Intent(getApplicationContext(),
									AdminEditDynamicStack.class);
							i.putExtra("stackName", stackName);
							i.putExtra("buttonInvisible", true);
							startActivityForResult(i, 1);
							break;

						} else {
							Intent i = new Intent(getApplicationContext(),
									AdminEditStack.class);
							i.putExtra("stackName", stackName);
							startActivityForResult(i, 1);
							break;
						}
					}
				}

			}
		} else if (item.getTitle() == "Reset Drawer") {
			// all cards of this stack get resetted and set back to the drawer:
			// don't know
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			String stackName = ((TextView) info.targetView).getText()
					.toString();

			for (Stack stack : Stack.allStacks) {
				if (stack.getStackName().equals(stackName)) {
					Edit.getInstance().resetDrawer(stack);
					Toast toast = Toast.makeText(getApplicationContext(),
							"Stack has been resetted successfully",
							Toast.LENGTH_SHORT);
					toast.show();
					setResult(AdminChooseStackScreen.RESULT_OK);
					break;
				}
			}
		} else if (item.getTitle() == "Delete") {
			// TODO: OnClick: Delete -->
			// Delete.getInstance().deleteStack(stack), vorher stack raussuchen,
			// dann onActivityResult(0,RESULT_OK,null) ausf�hren
			// Delete the selected stack, after the user is asked to delete the
			// stack

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			// set title
			alertDialogBuilder.setTitle("Delete Card");
			// set dialog message
			alertDialogBuilder
					.setMessage("Are you sure you want to delete this stack?")
					.setIcon(R.drawable.question)
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// delete here
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
			// TODO: OnClick: Archive --> Erst Exchange.exportStack, dann delete
			// first the stack gets exported to the knowitowl-directory, then
			// the stack gets deleted
		} else {
			return false;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			items = updateStackList();
			lvAdapter = new ArrayAdapter<String>(this,
					R.layout.layout_listitem, items);
			lv.setAdapter(lvAdapter);
		}
	}

	public ArrayList<String> updateStackList() {
		ArrayList<String> items = new ArrayList<String>();
		for (Stack stack : Stack.allStacks) {
			items.add(stack.getStackName());
		}
		if (items.size() == 0) {
			items.add("No stacks available");
		}
		return items;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_choose_stack_screen, menu);
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

}