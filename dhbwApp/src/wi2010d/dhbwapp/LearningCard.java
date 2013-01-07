package wi2010d.dhbwapp;

import java.io.File;

import wi2010d.dhbwapp.control.Delete;
import wi2010d.dhbwapp.control.Learn;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LearningCard extends FragmentActivity implements
		ActionBar.TabListener {

	public static final int RESULT_CHANGED = 66;

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
	Card card;
	Stack stack;
	String stackName;

	TextView txt_counter_front;
	TextView txt_counter_back;
	TextView txt_front;
	TextView txt_back;
	Button sure, dontKnow, notSure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_card);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				stackName = null;
			} else {
				stackName = extras.getString("stackName");
			}
		} else {
			stackName = (String) savedInstanceState
					.getSerializable("stackName");
		}
		setTitle("Learning - "+ stackName);
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(stackName)) {
				this.stack = stack;
				break;
			}
		}

		card = Learn.getInstance().startLearning(stack);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
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
		getMenuInflater().inflate(R.menu.learning_card, menu);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.btn_learning_card_front_sure:
			card = Learn.getInstance().learnCard(2);
			if (card == null) {
				Intent intent = (new Intent(this, StatisticsScreen.class));
				intent.putExtra("Tab", 3);
				startActivity(intent);
				finish();
			} else {
				mViewPager.setCurrentItem(0);
				Log.e("TB", Learn.getInstance().getActualProgressAsString());
				txt_counter_front.setText(Learn.getInstance()
						.getActualProgressAsString());
				txt_counter_back.setText(Learn.getInstance()
						.getActualProgressAsString());
				txt_front.setText(card.getCardFront());
				txt_back.setText(card.getCardBack());
			}
			return true;
		case R.id.btn_learning_card_front_dontKnow:
			card = Learn.getInstance().learnCard(0);
			if (card == null) {
				Intent intent = (new Intent(this, StatisticsScreen.class));
				intent.putExtra("Tab", 3);
				startActivity(intent);
				finish();
			} else {
				mViewPager.setCurrentItem(0);
				txt_counter_front.setText(Learn.getInstance()
						.getActualProgressAsString());
				txt_counter_back.setText(Learn.getInstance()
						.getActualProgressAsString());
				txt_front.setText(card.getCardFront());
				txt_back.setText(card.getCardBack());
			}
			return true;
		case R.id.btn_learning_card_front_notSure:
			card = Learn.getInstance().learnCard(1);
			if (card == null) {
				Intent intent = (new Intent(this, StatisticsScreen.class));
				intent.putExtra("Tab", 3);
				startActivity(intent);
				finish();
			} else {
				mViewPager.setCurrentItem(0);
				txt_counter_front.setText(Learn.getInstance()
						.getActualProgressAsString());
				txt_counter_back.setText(Learn.getInstance()
						.getActualProgressAsString());
				txt_front.setText(card.getCardFront());
				txt_back.setText(card.getCardBack());
			}
			return true;
		case R.id.btn_admin_edit_card:
			Intent intent = new Intent(this, AdminEditCard.class);
			intent.putExtra("cardID", card.getCardID());
			startActivityForResult(intent, RESULT_CHANGED);
			return true;
		case R.id.btn_admin_delete_card:

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			// set title
			alertDialogBuilder.setTitle("Delete Card");
			// set dialog message
			alertDialogBuilder
					.setMessage("Are you sure you want to delete this card?")
					.setIcon(R.drawable.question)
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Delete.getInstance().deleteCard(card);
									card = Learn.getInstance().learnCard(3);
									if (card == null) {
										Intent deleteCard = (new Intent(
												getApplicationContext(),
												StatisticsScreen.class));
										deleteCard.putExtra("Tab", 3);
										startActivity(deleteCard);
										finish();
									} else {
										mViewPager.setCurrentItem(0);
										txt_counter_front.setText(Learn
												.getInstance()
												.getActualProgressAsString());
										txt_counter_back.setText(Learn
												.getInstance()
												.getActualProgressAsString());
										txt_front.setText(card.getCardFront());
										txt_back.setText(card.getCardBack());
									}
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
			
			
			return true;

		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			return false;
		}

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_CHANGED:
			for (Card searchCard : Card.allCards) {
				if (searchCard.getCardID() == this.card.getCardID()) {
					this.card = searchCard;
					break;
				}
			}
			txt_front.setText(card.getCardFront());
			txt_back.setText(card.getCardBack());
			txt_counter_front.setText(Learn.getInstance()
					.getActualProgressAsString());
			txt_counter_back.setText(Learn.getInstance()
					.getActualProgressAsString());
			break;
		default:
			break;
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
	
	public void abortLearning(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		// set title
		alertDialogBuilder.setTitle("Abort Learning");
		// set dialog message
		alertDialogBuilder
				.setMessage("Are you sure you want to abort this learning session?")
				.setIcon(R.drawable.question)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								card = Learn.getInstance().learnCard(4);
								Intent intent = (new Intent(getApplicationContext(), StatisticsScreen.class));
								intent.putExtra("Tab", 3);
								startActivity(intent);
								finish();
							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
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
				fragment = new CardFront();
				break;
			case 1:
				fragment = new CardBack();
				break;
			default:
				ErrorHandler error = new ErrorHandler(getApplicationContext());
				error.handleError(1);
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
				return getString(R.string.learning_screen_tab_card_front)
						.toUpperCase();
			case 1:
				return getString(R.string.learning_screen_tab_card_back)
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

	public class CardFront extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public final String ARG_SECTION_NUMBER = "section_number";

		public CardFront() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.learning_card_front, null);
			txt_front = (TextView) v.findViewById(R.id.txt_card_front);
			txt_front.setMovementMethod(new ScrollingMovementMethod());
			txt_front.setText(card.getCardFront());
			txt_counter_front = (TextView) v
					.findViewById(R.id.txt_learning_counter);
			txt_counter_front.setText(Learn.getInstance()
					.getActualProgressAsString());
			return v;
		}
		
		
	}

	public class CardBack extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public final String ARG_SECTION_NUMBER = "section_number";
		
		Button showPicture;

		public CardBack() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.learning_card_back, null);
			txt_back = (TextView) v.findViewById(R.id.txt_card_back);
			txt_back.setMovementMethod(new ScrollingMovementMethod());
			txt_back.setText(card.getCardBack());
			txt_counter_back = (TextView) v
					.findViewById(R.id.txt_learning_counter);
			txt_counter_back.setText(Learn.getInstance()
					.getActualProgressAsString());
			
			showPicture = (Button) v.findViewById(R.id.btn_learning_card_back_picture);
			showPicture.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				Toast toast;
				toast = Toast.makeText(getApplicationContext(),
						"Path: " +  card.getCardBackPicture(), Toast.LENGTH_LONG);
				toast.show();
					
				
				if (card.getCardBackPicture() != ""){						
					Intent show = new Intent();
					show.setAction(Intent.ACTION_VIEW);
					show.setDataAndType(Uri.fromFile(new File(card.getCardBackPicture())), "image/*");
					startActivity(show);
				}
				
					
				}
			});
			
			return v;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
        {
			abortLearning();
			return true;
        }
		return false;
	}
}
