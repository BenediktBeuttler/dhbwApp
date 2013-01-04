package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Stack;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
				Intent i = new Intent(getApplicationContext(), AdminCreateDynamicStack.class);
				i.putExtra("fromLearning", fromLearning);
				startActivityForResult(i, 1);
			}
		});

		items = updateStack();

		lv = (ListView) findViewById(R.id.learn_stack_list);
		lvAdapter = new ArrayAdapter<String>(this, R.layout.layout_listitem,
				items);
		lv.setAdapter(lvAdapter);

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
			//ErrorHandler started, if Result is canceled
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(getFragmentManager(), "dialog");
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