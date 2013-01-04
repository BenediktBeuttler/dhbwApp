package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Stack;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminEditStack extends Activity {
	String stackName;
	EditText txt_stack_name;
	Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_edit_stack_screen);
		txt_stack_name = (EditText) findViewById(R.id.txt_admin_edit_stack);
		save = (Button) findViewById(R.id.btn_admin_edit_stack_save);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				stackName = null;
			} else {
				stackName = extras.getString("stackName");
			}
		} else {
			stackName = (String) savedInstanceState
					.getSerializable("stackName");
		}

		txt_stack_name.setText(stackName);

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String newStackName = (txt_stack_name.getText().toString());
				for (Stack stack : Stack.allStacks) {
					if(stack.getStackName().equals(newStackName))
					{
						//ErrorHandling if StackName is already taken
						ErrorHandlerFragment newFragment = ErrorHandlerFragment
								.newInstance(R.string.error_handler_name_taken, ErrorHandlerFragment.NAME_TAKEN );
						newFragment.show(getFragmentManager(), "dialog");	
						//
						break;
					}
					else if (stack.getStackName().equals(stackName)) {
						Edit.getInstance().changeStackName(newStackName, stack);
						Toast toast = Toast.makeText(getApplicationContext(),
								"Stack name changed to "
										+ txt_stack_name.getText().toString(),
								Toast.LENGTH_SHORT);
						toast.show();
						setResult(AdminChooseStackScreen.RESULT_OK);
						finish();
					}
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_edit_stack_screen, menu);
		return true;
	}

}
