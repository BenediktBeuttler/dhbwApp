package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Database;
import wi2010d.dhbwapp.control.DatabaseManager;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LearningScreen extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_screen);
		
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
		getMenuInflater().inflate(R.menu.learning_screen, menu);
			
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(this, LearningChooseStackScreen.class);
		
		DatabaseManager.getInstance().deleteDB();
		
		
		/*switch (v.getId()) {
		case R.id.btn_learning_stack1:
			i.putExtra("a", "b");
			startActivityForResult(i, 0);
			break;

		default:
			break;
		}*/
		
	}

}
