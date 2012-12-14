package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AdminChooseStackScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_choose_stack_screen);
		
		//String []s = new String[]{"Lorem","Ipsum","dolor","sit","amet"};
				
		//List<Tag> tags = new ArrayList<Tag>();
		//tags.add(Create.getInstance().newTag("Penis"));
		
		//Create.getInstance().newStack("Döner", Create.getInstance().newCard("Hallo", "Welt", tags, "", ""));
		//Create.getInstance().newStack("Reppe", Create.getInstance().newCard("2", "2,5", tags, "", ""));
		//Create.getInstance().newStack("Bene", Create.getInstance().newCard("3", "3,5", tags, "", ""));
		//Create.getInstance().newStack("Tim", Create.getInstance().newCard("4", "4,5", tags, "", ""));
		
		//ArrayList<String> items = new ArrayList<String>();
		//for (Stack stack : Stack.allStacks)
		{
			//items.add(stack.getStackName());
		}
        //ListView lv = (ListView)findViewById(R.id.admin_stack_list);
        //lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_choose_stack_screen, menu);
		return true;
	}
	

}
