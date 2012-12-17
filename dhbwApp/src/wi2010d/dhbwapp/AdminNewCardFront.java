package wi2010d.dhbwapp;

import wi2010d.dhbwapp.model.Card;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AdminNewCardFront extends FragmentActivity {
	Card card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card_front);
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

}
