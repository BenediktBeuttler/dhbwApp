package wi2010d.dhbwapp;

import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Start Activity with main navigation
 */
public class StartScreen extends OnResumeActivity implements OnClickListener {
	private Button learning, admin, settings, statistic, help;
	private TextView appName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);

		// init the views
		appName = (TextView) findViewById(R.id.lbl_app_name_start_screen);
		learning = (Button) findViewById(R.id.btn_start_learning);
		admin = (Button) findViewById(R.id.btn_start_admin);
		settings = (Button) findViewById(R.id.btn_start_settings);
		statistic = (Button) findViewById(R.id.btn_start_statistic);
		help = (Button) findViewById(R.id.btn_start_help);
		// set OnClickListener
		learning.setOnClickListener(this);
		admin.setOnClickListener(this);
		settings.setOnClickListener(this);
		statistic.setOnClickListener(this);
		help.setOnClickListener(this);
		// hide the ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		if (getIntent().getExtras() != null) {
			int picturesNotFound = getIntent().getExtras().getInt(
					"picturesNotFound", 0);
			if (picturesNotFound > 0) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);

				alert.setTitle("Import");
				if (picturesNotFound == 1) {
					alert.setMessage("Sorry, " + picturesNotFound
							+ " picture could not be found and imported.");
				}
				else{
					alert.setMessage("Sorry, " + picturesNotFound
							+ " pictures could not be found and imported.");	
				}
				alert.setIcon(R.drawable.alert);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});

				alert.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// check which button is pressed and start specific activity
		switch (v.getId()) {
		case R.id.btn_start_learning:
			startActivity(new Intent(this, LearningChooseStackScreen.class));
			break;
		case R.id.btn_start_admin:
			startActivity(new Intent(this, AdminScreen.class));
			break;
		case R.id.btn_start_settings:
			startActivity(new Intent(this, SettingsScreen.class));
			break;
		case R.id.btn_start_statistic:
			startActivity(new Intent(this, StatisticsScreen.class));
			break;
		case R.id.btn_start_help:
			startActivity(new Intent(this, HelpScreen.class));
			break;
		default:
			// if anything goes wrong, display general error dialog
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(getFragmentManager(), "dialog");
			break;
		}

	}

}
