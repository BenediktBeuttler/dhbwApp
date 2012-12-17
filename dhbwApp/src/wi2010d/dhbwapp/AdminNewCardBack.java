package wi2010d.dhbwapp;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdminNewCardBack extends FragmentActivity {

	private Card card;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card_back);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	

	public void setCard(Card card){
		this.card = card;
	}

}
