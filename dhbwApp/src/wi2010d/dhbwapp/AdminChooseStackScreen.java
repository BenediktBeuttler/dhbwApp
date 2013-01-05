package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Stack;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_choose_stack_screen);

		items = updateStackList();

		lv = (ListView) findViewById(R.id.admin_stack_list);
		lvAdapter = new ArrayAdapter<String>(this,
				R.layout.layout_listitem, items);
		lv.setAdapter(lvAdapter);

		lv.setClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String stackName = ((TextView) v).getText().toString();

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
			}
		});
		
		lv.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Bene, hier Dialog mit 3 Buttons: Edit / Delete / Archive	
				//kein LongClick: siehe http://developer.android.com/guide/topics/ui/menus.html#context-menu
				//bsp: http://www.stealthcopter.com/blog/2010/04/android-context-menu-example-on-long-press-gridview/
				//bsp 2 :http://saigeethamn.blogspot.de/2011/05/context-menu-android-developer-tutorial.html
				
										//TODO: OnClick: Edit --> AdminEditStackScreen, vorher stackName übergeben
									
										//TODO: OnClick: Delete --> Delete.getInstance().deleteStack(stack), vorher stack raussuchen, dann onActivityResult(0,RESULT_OK,null) ausführen
									
										//TODO: OnClick: Archive --> Erst Exchange.exportStack, dann delete
									
				
				return false;
			}
		});
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