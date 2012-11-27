package wi2010d.dhbwapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class LearningScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learning_screen, menu);
		return true;
	}
	
	public void onButtonClick(View view){
		
		if(view.getId()== R.id.btn_learning_stapel){
			startActivity(new Intent(this, StapelScreen.class));
		}
	}

}
