package wi2010d.dhbwapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartScreen extends Activity implements OnClickListener{
	Button learning, edit, admin, statistic;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);
		
		learning = (Button)findViewById(R.id.btn_start_learning);
		edit = (Button)findViewById(R.id.btn_start_edit);
		admin = (Button)findViewById(R.id.btn_start_admin);
		statistic = (Button)findViewById(R.id.btn_start_statistic);

		learning.setOnClickListener(this);
		edit.setOnClickListener(this);
		admin.setOnClickListener(this);
		statistic.setOnClickListener(this);
		
		//INIT METHODE MUSS AUSGEFÜHRT WERDEN!!!
		//ERRORHANDLER MUSS MIT APP CONTEXT INITIALISIERT WERDEN !!!
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_screen, menu);
		return true;
	}
	

	@Override
	public void onClick(View v) {
				
		switch (v.getId()) {
		case R.id.btn_start_learning:
			startActivity(new Intent (this, LearningScreen.class));
			break;
		case R.id.btn_start_edit:
			startActivity(new Intent (this, EditScreen.class));
			break;
		case R.id.btn_start_admin:
			startActivity(new Intent (this, AdminScreen.class));
			break;
		case R.id.btn_start_statistic:
			//startActivity(new Intent (this, StatisticScreen.class));
			break;

		default:
			break;
		}
		
	}

	//Thomas B commit 
}
