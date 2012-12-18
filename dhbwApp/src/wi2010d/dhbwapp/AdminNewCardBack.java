package wi2010d.dhbwapp;

import wi2010d.dhbwapp.model.Card;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AdminNewCardBack extends FragmentActivity {

	private Card card;
	Button newCardNewStack, existingStack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card_back);

		newCardNewStack = (Button) findViewById(R.id.btn_new_card_new_stack);
		existingStack = (Button) findViewById(R.id.btn_new_card_existing_stack);

		newCardNewStack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

			}

		});

		existingStack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public void setCard(Card card) {
		this.card = card;
	}

}
