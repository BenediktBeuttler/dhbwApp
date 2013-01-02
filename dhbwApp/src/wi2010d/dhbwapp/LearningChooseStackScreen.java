package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Stack;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class LearningChooseStackScreen extends Activity implements
		OnClickListener {

	EditText card_front;
	Button createDynStack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_choose_stack_screen);
		createDynStack = (Button) findViewById(R.id.btn_learning_create_dyn_stack);
		createDynStack.setOnClickListener(this);

		ArrayList<String> items = new ArrayList<String>();

		for (Stack stack : Stack.allStacks) {
			items.add(stack.getStackName());
		}
		if (items.size() == 0) {
			items.add("No stacks available");
		}

		ListView lv = (ListView) findViewById(R.id.learn_stack_list);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items));

		lv.setClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String stackName = ((TextView) v).getText().toString();

				if (!stackName.equals("No stacks available")) {
					Intent i = new Intent(getApplicationContext(), LearningCard.class);
					i.putExtra("stackName", stackName);
					startActivity(i);
					finish();
				}

			}
		});
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
		// TODO Auto-generated method stub
		startActivity(new Intent(this, LearningCard.class));

	}

}
