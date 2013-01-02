package wi2010d.dhbwapp;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Database;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminNewCard extends FragmentActivity implements
		ActionBar.TabListener {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_start_screen:
			startActivity(new Intent(this, StartScreen.class));
			finish();
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpScreen.class));
			finish();
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
			finish();
			return true;
		case R.id.btn_admin_new_card_new_stack:
			if (isCardNotEmpty()) {
				card = Create.getInstance().newCard(
						cardFront.getText().toString(),
						cardBack.getText().toString(), null, "", "");
				
				Intent i = new Intent(getApplicationContext(),
						AdminNewStack.class);
				startActivityForResult(i, NEW_STACK);
				
			}
			return true;
		case R.id.btn_admin_new_card_existing_stack:
			if (Stack.allStacks.size() == 0) {
				ErrorHandler.getInstance().handleError(
						ErrorHandler.getInstance().NO_STACK_AVAILABLE);
			} else if (isCardNotEmpty()) {
				card = Create.getInstance().newCard(
						cardFront.getText().toString(),
						cardBack.getText().toString(), null, "", "");
				
				Intent i = new Intent(getApplicationContext(),
						AdminNewCardChooseStack.class);
				startActivityForResult(i, STACK_CHOSEN);
			}
		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			return false;
		}
	}

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	EditText cardFront;
	EditText cardBack;
	public static final int STACK_CHOSEN = 10;
	public static final int NEW_STACK = 11;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	Card card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.newCard);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_new_card, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;

			switch (position) {
			case 0:
				fragment = new NewCardFront();
				break;
			case 1:
				fragment = new NewCardBack();
				break;

			default:
				break;
			}

			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.admin_screen_tab_card_front)
						.toUpperCase();
			case 1:
				return getString(R.string.admin_screen_tab_card_back)
						.toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}

	public class NewCardFront extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public NewCardFront() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.admin_new_card_front, null);

			cardFront = (EditText) v.findViewById(R.id.txt_edit_card_front);

			return v;
		}
	}

	public class NewCardBack extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		Button newCardNewStack, existingStack;

		public NewCardBack() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.admin_new_card_back, null);

			cardBack = (EditText) v.findViewById(R.id.txt_edit_card_back);
			/*
			newCardNewStack = (Button) v
					.findViewById(R.id.btn_admin_new_card_new_stack);
			existingStack = (Button) v
					.findViewById(R.id.btn_admin_new_card_existing_stack);
			

			newCardNewStack.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					if (isCardNotEmpty()) {
						card = Create.getInstance().newCard(
								cardFront.getText().toString(),
								cardBack.getText().toString(), null, "", "");
						
						Intent i = new Intent(getApplicationContext(),
								AdminNewStack.class);
						startActivityForResult(i, NEW_STACK);
						
					}
				}

			});
			

			existingStack.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					if (Stack.allStacks.size() == 0) {
						ErrorHandler.getInstance().handleError(
								ErrorHandler.getInstance().NO_STACK_AVAILABLE);
					} else if (isCardNotEmpty()) {
						card = Create.getInstance().newCard(
								cardFront.getText().toString(),
								cardBack.getText().toString(), null, "", "");
						
						Intent i = new Intent(getApplicationContext(),
								AdminNewCardChooseStack.class);
						startActivityForResult(i, STACK_CHOSEN);
						
					}
				}

			});
			*/
			return v;
		}
	}

	protected boolean isCardNotEmpty() {
		if (cardFront.getText() == null
				|| cardFront.getText().toString().equals("")) {
			ErrorHandler.getInstance().handleError(
					ErrorHandler.getInstance().TEXT_FIELD_FRONT_EMPTY);
			return false;
		} else if (cardBack.getText() == null
				|| cardBack.getText().toString().equals("")) {
			ErrorHandler.getInstance().handleError(
					ErrorHandler.getInstance().TEXT_FIELD_BACK_EMPTY);
			return false;
		} else
			return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String stackName;
		Toast toast;

		switch (resultCode) {
		case STACK_CHOSEN:
			stackName = data.getExtras().getString("stackName");
			for (Stack stack : Stack.allStacks) {
				if (stack.getStackName().equals(stackName)) {
					stack.getCards().add(card);
					Database.getInstance().addStackCardCorrelation(
							stack.getStackID(), card.getCardID());
					toast = Toast.makeText(this, "Card added to stack "
							+ stackName, Toast.LENGTH_SHORT);
					toast.show();
					finish();
					break;
				}
			}
			break;

		case NEW_STACK:
			stackName = data.getExtras().getString("stackName");
			Create.getInstance().newStack(stackName, card);
			toast = Toast.makeText(this, "Stack " + stackName
					+ " created and Card added", Toast.LENGTH_SHORT);
			toast.show();
			finish();
			break;
		default:
			break;
		}
	}

}
