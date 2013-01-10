package wi2010d.dhbwapp;


import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class HelpAboutScreen extends OnResumeActivity implements OnClickListener{

	private TextView appName, version, feedback;
	private ImageView knowitall;
	private ImageButton email;
	private RatingBar rating;
	private float ratingNumer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_about_screen);

		appName = (TextView) findViewById(R.id.lbl_help_about_app_name);
		version = (TextView) findViewById(R.id.lbl_help_about_vers);
		knowitall = (ImageView) findViewById(R.id.img_help_about_knowitall);
		feedback = (TextView) findViewById(R.id.lbl_help_about_feedback);
		email = (ImageButton) findViewById(R.id.btn_help_about_email);
		rating = (RatingBar)findViewById(R.id.ratingBar);

		email.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_help_about_email:
			ratingNumer = Float.valueOf(rating.getRating());
			if(ratingNumer == 0){
				Toast toast = Toast.makeText(
						getApplicationContext(),
						"Please give a rating first.",
						Toast.LENGTH_SHORT);
				toast.show();
			}else{
				//TODO: Thomas do it! Die Variable mit dem Rating is ratingNumer, aber als float
				// kannst meine Mailadresse angeben: benedikt.beuttler@gmail.com
			}
			break;
		default:
			break;
		}
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
		default:
			// if anything goes wrong in the switchcase show dialog
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(getFragmentManager(), "dialog");
			return false;
		}
	}
}