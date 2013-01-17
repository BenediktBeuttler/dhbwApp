package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.Collections;

import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Stack;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Gets started for result by AdminNewCard, when a stack needs to be chosen.
 * This activity provides a list with all stacks and gives it back to
 * AdminNewCard, when a card is chosen.
 */
public class AdminNewCardChooseStack extends OnResumeActivity {

	private ArrayList<String> items = new ArrayList<String>();
	private ListView lv;
	private ArrayAdapter<String> lvAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card_choose_stack);
		setResult(RESULT_CANCELED);

		// get the name of every Stack for the listView
		for (Stack stack : Stack.allStacks) {
			if (stack.isDynamicGenerated()) {
				if (stack.getStackName().startsWith("<Dyn>")) {
					items.add(stack.getStackName());
				} else {
					items.add("<Dyn> " + stack.getStackName());
				}
			} else {
				items.add(stack.getStackName());
			}
		}
		Collections.sort(items);

		// get the ListView and create the adapter
		lv = (ListView) findViewById(R.id.admin_edit_card_stack_list);
		lvAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		lv.setAdapter(lvAdapter);

		lv.setClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// Get the clicked StackName, set the result and finish the
				// activity
				String stackName = ((TextView) v).getText().toString();
				if (stackName.startsWith("<Dyn>")) {
					stackName = stackName.substring(6);
				}
				setResult(AdminNewCard.STACK_CHOSEN, getIntent());
				getIntent().putExtra("stackName", stackName);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create the options menu
		getMenuInflater().inflate(R.menu.admin_choose_stack_screen, menu);
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
			startActivity(new Intent(this, HelpEditCardsStackScreen.class));
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

}
