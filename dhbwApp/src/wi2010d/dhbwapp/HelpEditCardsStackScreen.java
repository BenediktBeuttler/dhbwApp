package wi2010d.dhbwapp;

import android.os.Bundle;

/**
 * Simple Activity, just showing the help text about editing Cards and Stacks.
 */
public class HelpEditCardsStackScreen extends OnResumeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_edit_cards_stack_screen);
	}

}
