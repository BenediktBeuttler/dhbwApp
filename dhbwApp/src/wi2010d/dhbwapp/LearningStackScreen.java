package wi2010d.dhbwapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LearningStackScreen extends Activity {
	
	EditText card_front;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_stack_screen);
		
		card_front = (EditText)findViewById(R.id.learning_card_front);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stapel_screen, menu);
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data.getExtras().containsKey("a")){
			card_front.setText("Von Button 1");
		}
		if (data.getExtras().containsKey("c")){
			card_front.setText("Von Button 2");
		}
	}

}
