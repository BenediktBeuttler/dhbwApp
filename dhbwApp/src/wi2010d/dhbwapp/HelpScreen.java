package wi2010d.dhbwapp;

import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Offers links to the different HelpScreens
 */
public class HelpScreen extends OnResumeActivity implements OnClickListener {
	Button introduction, learn_statistics, edit_cards_stacks, settings,
			import_export, about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_screen);

		// Init Buttons
		introduction = (Button) findViewById(R.id.btn_help_introduction);
		learn_statistics = (Button) findViewById(R.id.btn_help_learnstatistics);
		edit_cards_stacks = (Button) findViewById(R.id.btn_help_editcardsstacks);
		settings = (Button) findViewById(R.id.btn_help_settings);
		import_export = (Button) findViewById(R.id.btn_help_importexport);
		about = (Button) findViewById(R.id.btn_help_about);

		// Set onClickListeners
		introduction.setOnClickListener(this);
		learn_statistics.setOnClickListener(this);
		edit_cards_stacks.setOnClickListener(this);
		settings.setOnClickListener(this);
		import_export.setOnClickListener(this);
		about.setOnClickListener(this);
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
			// if anything goes wrong (case not found), display general error
			// dialog
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(getFragmentManager(), "dialog");
			return false;
		}
	}

	public void onClick(View v) {

		// Start Activities when clicking the button
		switch (v.getId()) {
		case R.id.btn_help_introduction:
			startActivity(new Intent(this, HelpIntroductionScreen.class));
			break;
		case R.id.btn_help_learnstatistics:
			startActivity(new Intent(this, HelpLearnStatisticsScreen.class));
			break;
		case R.id.btn_help_editcardsstacks:
			startActivity(new Intent(this, HelpEditCardsStackScreen.class));
			break;
		case R.id.btn_help_settings:
			startActivity(new Intent(this, HelpSettingsScreen.class));
			break;
		case R.id.btn_help_importexport:
			startActivity(new Intent(this, HelpImportExportScreen.class));
			break;
		case R.id.btn_help_about:
			startActivity(new Intent(this, HelpAboutScreen.class));
			break;

		default:
			// if anything goes wrong (case not found), display general error
			// dialog
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(getFragmentManager(), "dialog");
			break;
		}

	}
}