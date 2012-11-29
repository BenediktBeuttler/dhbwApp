package wi2010d.dhbwapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class StartScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_screen, menu);
		return true;
	}
	
	public void start_onButtonClick(View view){
		if(view.getId()== R.id.btn_start_learning){
			startActivity(new Intent(this, LearningScreen.class));
		}
		if(view.getId() == R.id.btn_start_edit){
			startActivity(new Intent(this, EditScreen.class));
		}
		if(view.getId() == R.id.btn_start_admin){
			startActivity(new Intent(this, AdminScreen.class));
		}
		if(view.getId() == R.id.btn_start_statistic){
			System.out.println("FUCK YOU!!!!!");
			//startActivity(new Intent(this, statisticScreen.class));
		}
	}

}
