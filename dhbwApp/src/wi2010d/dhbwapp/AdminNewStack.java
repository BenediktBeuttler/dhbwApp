package wi2010d.dhbwapp;
 
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminNewStack extends Activity {
	String stackName;
	EditText txt_stack_name;
	Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_stack);
		txt_stack_name = (EditText) findViewById(R.id.txt_admin_new_stack);
		//save = (Button) findViewById(R.id.btn_admin_new_stack_save);

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toast;
				if (txt_stack_name.getText().toString().equals("")) {
					toast = Toast.makeText(getApplicationContext(),
							"Please insert a stack name!", Toast.LENGTH_SHORT);
					toast.show();
				} else if (txt_stack_name.getText().toString()
						.equals("All Stacks")) {
					toast = Toast
							.makeText(
									getApplicationContext(),
									"Stack name cannot be 'All Stacks', please select another one!",
									Toast.LENGTH_SHORT);
					toast.show();

				} else {
					stackName = txt_stack_name.getText().toString();
					setResult(AdminNewCard.NEW_STACK, getIntent());
					getIntent().putExtra("stackName", stackName);
					finish();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.admin_choose_stack_screen, menu);
		return true;
	}

}
