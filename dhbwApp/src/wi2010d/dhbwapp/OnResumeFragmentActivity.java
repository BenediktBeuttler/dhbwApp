package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Init;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

public class OnResumeFragmentActivity extends FragmentActivity {

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
