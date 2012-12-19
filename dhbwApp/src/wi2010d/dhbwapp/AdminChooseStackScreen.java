package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.model.Stack;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

		items = updateStacks();

		lv = (ListView) findViewById(R.id.admin_stack_list);
		lvAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		lv.setAdapter(lvAdapter);

		lv.setClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String stackName = ((TextView) v).getText().toString();

				if (!stackName.equals("No stacks available")) {
					Intent i = new Intent(getApplicationContext(),
							AdminEditStack.class);
					i.putExtra("stackName", stackName);
					startActivityForResult(i, 1);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			items = updateStacks();
			lvAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, items);
			lv.setAdapter(lvAdapter);
		}
	}

	public ArrayList<String> updateStacks() {
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

}