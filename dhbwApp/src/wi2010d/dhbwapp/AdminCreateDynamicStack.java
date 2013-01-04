package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.model.Tag;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminCreateDynamicStack extends FragmentActivity {

	Button createDynStack;
	Fragment tagList;
	ArrayList<Tag> dynStackTagList = new ArrayList<Tag>();
	String name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.admin_create_dynamic_stack);
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// set all tags unchecked, then create the list
			
			for (Tag tag : Tag.allTags) {
				tag.setChecked(false);
			}

			createDynStack = (Button) findViewById(R.id.btn_admin_create_dynamic_stack);			
			createDynStack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					AlertDialog.Builder alert = new AlertDialog.Builder(v
							.getContext());

					alert.setTitle("New Tag");
					alert.setMessage("Insert Tag Name");

					// Set an EditText view to get user input
					final EditText input = new EditText(v.getContext());
					int i = 0;
					for (Tag tag : Tag.allTags) {
						if (tag.isChecked()) {
							dynStackTagList.add(tag);
							if (input.getText().toString().equals("")) {
								if (i <= 3) {
									name = name + " - " + tag.getTagName();
								}
								i++;
							} else {
								name = input.getText().toString();
							}
						}
					}
					input.setText(name);
					alert.setView(input);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (dynStackTagList.size() > 0) {
										setResult(RESULT_CANCELED);
										if (Create.getInstance().newDynStack(
												input.getText().toString(), dynStackTagList)) {
											Toast toast = Toast.makeText(
													getApplicationContext(),
													"Dynamic Stack " + name
															+ " created!",
													Toast.LENGTH_LONG);
											toast.show();
											setResult(RESULT_OK);
										}
										finish();
									}
								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							});
					alert.show();
				}

			});
			tagList = new AdminTagListFragment();
			tagList.setArguments(getIntent().getExtras());
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