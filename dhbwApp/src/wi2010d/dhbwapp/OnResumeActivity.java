package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Init;
import android.app.Activity;
import android.content.Intent;

public class OnResumeActivity extends Activity {

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
