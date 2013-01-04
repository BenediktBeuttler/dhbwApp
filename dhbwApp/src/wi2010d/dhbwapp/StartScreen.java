package wi2010d.dhbwapp;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartScreen extends Activity implements OnClickListener {
	Button learning, admin, settings, statistic, help;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);

		learning = (Button) findViewById(R.id.btn_start_learning);
		admin = (Button) findViewById(R.id.btn_start_admin);
		settings = (Button) findViewById(R.id.btn_start_settings);
		statistic = (Button) findViewById(R.id.btn_start_statistic);
		help = (Button) findViewById(R.id.btn_start_help);
		
		learning.setOnClickListener(this);
		admin.setOnClickListener(this);
		settings.setOnClickListener(this);
		statistic.setOnClickListener(this);
		help.setOnClickListener(this);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
	}

	@Override
	public void onClick(View v) {

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
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			break;
		}

	}

}
