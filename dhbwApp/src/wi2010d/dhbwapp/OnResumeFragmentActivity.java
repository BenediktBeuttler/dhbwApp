package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Init;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * This FragmentActivity should be extended by every FragmentActivity, so the
 * onResume method is added to every FragmentActivity
 */
public class OnResumeFragmentActivity extends FragmentActivity {

	@Override
	protected void onResume() {
		super.onResume();
		this.reloadOnGarbageCollected();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		this.reloadOnGarbageCollected();
	}

	/**
	 * Checks if the Garbage Collector deleted one or more Variables. If this
	 * happened, reload the app.
	 */
	protected void reloadOnGarbageCollected() {
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
