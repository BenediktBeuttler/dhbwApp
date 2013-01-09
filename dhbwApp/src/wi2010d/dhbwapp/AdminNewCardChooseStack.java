package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.model.Stack;
import android.os.Bundle;
import android.view.Menu;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card_choose_stack);

		// get the name of every Stack for the listView
		for (Stack stack : Stack.allStacks) {
			if (stack.isDynamicGenerated()) {
				items.add("<Dyn> " + stack.getStackName());
			} else {
				items.add(stack.getStackName());
			}
		}

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

}
