package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.model.Stack;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class LearningChooseStackScreen extends Activity implements OnClickListener{
	
	EditText card_front;
	Button createDynStack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_choose_stack_screen);
		createDynStack = (Button)findViewById(R.id.btn_learning_create_dyn_stack);
		createDynStack.setOnClickListener(this);
		
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, LearningCard.class));
		
	}


}
