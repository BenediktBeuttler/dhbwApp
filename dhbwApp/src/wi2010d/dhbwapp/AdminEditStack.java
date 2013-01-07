package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Delete;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.control.Learn;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminEditStack extends FragmentActivity {
	String stackName;
	EditText txt_stack_name;

	public AdminEditStack() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_edit_stack_screen);
		txt_stack_name = (EditText) findViewById(R.id.txt_admin_edit_stack);

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_edit_stack_screen, menu);
		return true;
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
		case R.id.btn_admin_edit_stack_save:
			String newStackName = (txt_stack_name.getText().toString());
			for (Stack stack : Stack.allStacks) {
				if (stack.getStackName().equals(newStackName)) {
					// ErrorHandling, if stack name is already taken.
					ErrorHandlerFragment newFragment = ErrorHandlerFragment
							.newInstance(R.string.error_handler_name_taken,
									ErrorHandlerFragment.NAME_TAKEN);
					newFragment.show(getFragmentManager(), "dialog");
					//
					break;
				} else if (stack.getStackName().equals(stackName)) {
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
			return true;
		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			return false;
		}
	}

}
