package wi2010d.dhbwapp;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpScreen extends Activity implements OnClickListener{
Button introduction, learn_statistics, edit_cards_stacks, settings, import_export;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_screen);
		
		introduction = (Button)findViewById(R.id.btn_help_introduction);
		learn_statistics = (Button)findViewById(R.id.btn_help_learnstatistics);
		edit_cards_stacks = (Button)findViewById(R.id.btn_help_editcardsstacks);
		settings = (Button)findViewById(R.id.btn_help_settings);
		import_export = (Button)findViewById(R.id.btn_help_importexport);
		
		introduction.setOnClickListener(this);
		learn_statistics.setOnClickListener(this);
		edit_cards_stacks.setOnClickListener(this);
		settings.setOnClickListener(this);
		import_export.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help_screen, menu);
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
		case R.id.btn_help_introduction:
			startActivity(new Intent (this, HelpIntroductionScreen.class));
			break;
		case R.id.btn_help_learnstatistics:
			startActivity(new Intent (this, HelpLearnStatisticsScreen.class));
			break;
		case R.id.btn_help_editcardsstacks:
			startActivity(new Intent (this, HelpEditCardsStackScreen.class));
			break;
		case R.id.btn_help_settings:
			startActivity(new Intent (this, HelpSettingsScreen.class));
			break; 
		case R.id.btn_help_importexport:
			startActivity(new Intent (this, HelpImportExportScreen.class));
			break;

		default:
			break;
		}

}
}