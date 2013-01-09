package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Init;
import android.app.Activity;
import android.content.Intent;

/**
 * This Activity should be extended by every activity, so the onResume method is
 * added to every activity
 */
public class OnResumeActivity extends Activity {

	/**
	 * Checks if the Garbage Collector deleted one or more Variables.
	 * If this happened, reload the app.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (Init.isSthGarabageCollected()) {
			Intent newStart = new Intent(getApplicationContext(),
					Progress.class);

			if (this.getParent() != null) {
				this.getParent().finish();
			}

			startActivity(newStart);
			this.finish();
		}
	}

}
