package wi2010d.dhbwapp;

import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class HelpAboutScreen extends OnResumeActivity implements
		OnClickListener {

	private TextView appName, version, feedback;
	private ImageView knowitall;
	private ImageButton email;
	private RatingBar rating;
	private float ratingNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();
		setContentView(R.layout.help_about_screen);
		
		// set the animated logo
		ImageView img = (ImageView) findViewById(R.id.img_help_about_knowitall);
		img.setBackgroundResource(R.drawable.gif_big);

		// Get the background, which has been compiled to an AnimationDrawable
		// object.
		AnimationDrawable frameAnimation = (AnimationDrawable) img
				.getBackground();

		// Start the animation (looped playback by default).
		frameAnimation.start();

		appName = (TextView) findViewById(R.id.lbl_help_about_app_name);
		version = (TextView) findViewById(R.id.lbl_help_about_vers);
		feedback = (TextView) findViewById(R.id.lbl_help_about_feedback);
		email = (ImageButton) findViewById(R.id.btn_help_about_email);
		rating = (RatingBar) findViewById(R.id.ratingBar);

		email.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_help_about_email:
			ratingNumber = Float.valueOf(rating.getRating());
			if (ratingNumber == 0) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Please give a rating first.", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				// kannst meine Mailadresse angeben: benedikt.beuttler@gmail.com
				// Start Sending Intent
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"Feedback - Know it Owl");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, " + '\n'
						+ "I rate your app with " + ratingNumber + " stars.");
				emailIntent.setData(Uri
						.parse("mailto:benedikt.beuttler@gmail.com"));
				startActivity(Intent
						.createChooser(emailIntent, "Send Feedback"));
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
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
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