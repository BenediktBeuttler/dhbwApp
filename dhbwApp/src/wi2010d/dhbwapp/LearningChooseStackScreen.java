package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.model.Stack;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class LearningChooseStackScreen extends Activity {
	
	EditText card_front;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_choose_stack_screen);
		
		ArrayList<String> items = new ArrayList<String>();
		for (Stack stack : Stack.allStacks)
		{
			items.add(stack.getStackName());
		}
        ListView lv = (ListView)findViewById(R.id.learn_stack_list);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stapel_screen, menu);
		return true;
	}


}
