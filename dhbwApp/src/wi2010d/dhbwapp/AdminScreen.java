package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AdminScreen extends OnResumeActivity implements OnClickListener{
	Button new_card_new_stack;
	Button edit_card; 
	Button edit_stack;
	Button import_export;
	Button new_dyn_stack;
	Button update_dyn_stacks;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_screen);
		
		new_card_new_stack = (Button)findViewById(R.id.btn_admin_new_card_new_stack);
		edit_stack = (Button)findViewById(R.id.btn_admin_edit_stack);
		import_export = (Button)findViewById(R.id.btn_admin_import_export);
		new_dyn_stack = (Button)findViewById(R.id.btn_admin_new_dyn_stack);
		update_dyn_stacks = (Button)findViewById(R.id.btn_admin_update_dyn_stack);
		
		
		new_card_new_stack.setOnClickListener(this);
		edit_stack.setOnClickListener(this);
		import_export.setOnClickListener(this);
		new_dyn_stack.setOnClickListener(this);
		update_dyn_stacks.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_screen, menu);
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
		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			return false;
		}
	}
	
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_admin_new_card_new_stack:
			startActivity(new Intent (this, AdminNewCard.class));
			break;
		case R.id.btn_admin_edit_stack:
			startActivity(new Intent (this, AdminChooseStackScreen.class));
			break;
		case R.id.btn_admin_import_export:
			startActivity(new Intent (this, AdminImportExport.class));
			break;
		case R.id.btn_admin_new_dyn_stack:
			Intent i = new Intent (this, AdminCreateDynamicStack.class);
			startActivity(i);
			break;
		case R.id.btn_admin_update_dyn_stack:
			Create.getInstance().updateDynStacks();
			Toast toast = Toast.makeText(getApplicationContext(),
					"All dynamic Stacks have been updated successfully",
					Toast.LENGTH_SHORT);
			toast.show();
			break;
		default:
			break;
		}

}
}
