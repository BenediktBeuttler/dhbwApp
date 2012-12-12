package wi2010d.dhbwapp;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class LearningScreen extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learning_screen, menu);
		
		ErrorHandler.getInstance().handleError(1);
		
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(this, LearningStackScreen.class);
		
		switch (v.getId()) {
		case R.id.btn_learning_stack1:
			i.putExtra("a", "b");
			startActivityForResult(i, 0);
			break;
		case R.id.btn_learning_stack2:
			i.putExtra("c", "d");
			startActivityForResult(i, 0);
			break;

		default:
			break;
		}
		
	}

}
