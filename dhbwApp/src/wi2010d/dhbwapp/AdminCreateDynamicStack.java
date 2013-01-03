package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.model.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminCreateDynamicStack extends FragmentActivity {
	Button createDynStack;
	Fragment tagList;
	EditText dynStackName;
	ArrayList<Tag> dynStackTagList = new ArrayList<Tag>();
	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.admin_create_dynamic_stack);
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// set all tags unchecked, then create the list
			for (Tag tag : Tag.allTags) {
				tag.setChecked(false);
			}
			dynStackName = (EditText) findViewById(R.id.txt_admin_create_dyn_stack_name);
			
			createDynStack = (Button) findViewById(R.id.btn_admin_create_dynamic_stack);
			createDynStack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (Tag tag : Tag.allTags) {
						if (tag.isChecked()) {
							dynStackTagList.add(tag);
							if(dynStackName.getText().toString().equals("")){
								name = name + tag.getTagName();
							}
						}
					}
					if (dynStackTagList.size() > 0) {
						Create.getInstance().newDynStack(name, dynStackTagList);
						Toast toast = Toast.makeText(v.getContext(),
								"Dynamic Stack " + name + " created!",
								Toast.LENGTH_LONG);
						toast.show();
						finish();
					}
				}

			});

			tagList = new AdminTagListFragment();

			// Create the TagListFragment and bind it to the layout
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();

			fragmentTransaction.add(R.id.layout_admin_create_dyn_4_taglist,
					tagList).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_create_dynamic_stack, menu);
		return true;
	}

}
