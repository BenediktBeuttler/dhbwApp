package wi2010d.dhbwapp;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class AdminNewCardBack extends FragmentActivity implements OnClickListener {

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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_admin_new_card_new_stack:
			startActivity(new Intent(this, Admin_edit_stack.class));
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
