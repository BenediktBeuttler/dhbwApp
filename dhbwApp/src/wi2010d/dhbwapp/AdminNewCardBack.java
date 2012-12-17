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

public class AdminNewCardBack extends FragmentActivity implements OnClickListener {

	private Card card;
	Button newCardNewStack, existingStack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card_back);
		
		newCardNewStack = (Button) findViewById(R.id.btn_new_card_new_stack);
		existingStack = (Button) findViewById(R.id.btn_new_card_existing_stack);
		newCardNewStack.setOnClickListener(this);
		existingStack.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_new_card_new_stack:
			startActivity(new Intent(this, AdminEditNewStack.class));
			break;
		case R.id.btn_new_card_existing_stack:
			startActivity(new Intent(this, AdminChooseStackScreen.class));
			break;

		default:
			ErrorHandler.getInstance().handleError(
					ErrorHandler.getInstance().GENERAL_ERROR);
			break;
		}

	}

	public void setCard(Card card){
		this.card = card;
	}

}
