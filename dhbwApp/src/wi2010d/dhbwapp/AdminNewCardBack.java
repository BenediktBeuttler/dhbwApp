package wi2010d.dhbwapp;

import wi2010d.dhbwapp.model.Card;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdminNewCardBack extends FragmentActivity {

	private Card card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card_back);
		
		Log.d("Card Back", "On Create");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public void setCard(Card card) {
		this.card = card;
	}

}
