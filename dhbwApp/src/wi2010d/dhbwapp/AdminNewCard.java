package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Database;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
			case 2:
				fragment = new NewCardTags();

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
			return 3;
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
			case 2:
				return getString(R.string.admin_screen_tab_card_tags)
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

	public class NewCardTags extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		Button newTag;
		private ListView mainListView;
		private Planet[] planets;
		private ArrayAdapter<Planet> listAdapter;
		private LinearLayout ll;
		
		public NewCardTags() {
		}
	
			/** Called when the activity is first created. */
			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				View v = inflater.inflate(R.layout.admin_new_card_tags, null);
				newTag = (Button) v.findViewById(R.id.btn_admin_new_card_new_tag);
				
				newTag.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

						alert.setTitle("New Tag");
						alert.setMessage("Insert Tag Name");

						// Set an EditText view to get user input 
						final EditText input = new EditText(v.getContext());
						alert.setView(input);

						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						  String value = input.getText().toString();
						  // Do something with value!
						  Toast toast; // = new Toast(getApplicationContext());
						  toast = Toast.makeText(getApplicationContext(), "New Tag '" + value + "' has been added.", Toast.LENGTH_LONG);
						  toast.show();
						  }
						});

						alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int whichButton) {
						    // Canceled.
						  }
						});

						alert.show();	
					}
				});
				
				// Find the ListView resource.
				mainListView = (ListView) v.findViewById(R.id.tagsListView);
				//IMPLEMENT A BUTTON JUST FOR TEST-CASES!!!!!***************!"§%§"§$%$§
				//btn = (Button) findViewById(R.id.button1);

				// When item is tapped, toggle checked properties of CheckBox and
				// Planet.
				mainListView
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View item,
									int position, long id) {
								Planet planet = listAdapter.getItem(position);
								planet.toggleChecked();
								PlanetViewHolder viewHolder = (PlanetViewHolder) item
										.getTag();
								viewHolder.getCheckBox().setChecked(planet.isChecked());
							}
						});

				// Create and populate planets.
				planets = (Planet[]) getLastNonConfigurationInstance();
				if (planets == null) {
					planets = new Planet[] { new Planet("Mercury"),
							new Planet("Venus"), new Planet("Earth"),
							new Planet("Mars"), new Planet("Jupiter"),
							new Planet("Saturn"), new Planet("Uranus"),
							new Planet("Neptune"), new Planet("Ceres"),
							new Planet("Pluto"), new Planet("Haumea"),
							new Planet("Makemake"), new Planet("Eris") };
				}
				ArrayList<Planet> planetList = new ArrayList<Planet>();
				planetList.addAll(Arrays.asList(planets));

				// Set our custom array adapter as the ListView's adapter.
				listAdapter = new PlanetArrayAdapter(getApplicationContext(), planetList);
				mainListView.setAdapter(listAdapter);
				return v;
			}

			/** Holds planet data. */
			private class Planet {
				private String name = "";
				private boolean checked = false;

				public Planet() {
				}

				public Planet(String name) {
					this.name = name;
				}

				public Planet(String name, boolean checked) {
					this.name = name;
					this.checked = checked;
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public boolean isChecked() {
					return checked;
				}

				public void setChecked(boolean checked) {
					this.checked = checked;
				}

				public String toString() {
					return name;
				}

				public void toggleChecked() {
					checked = !checked;
				}
			}

			/** Holds child views for one row. */
			private class PlanetViewHolder {
				private CheckBox checkBox;
				private TextView textView;

				public PlanetViewHolder() {
				}

				public PlanetViewHolder(TextView textView, CheckBox checkBox) {
					this.checkBox = checkBox;
					this.textView = textView;
				}

				public CheckBox getCheckBox() {
					return checkBox;
				}

				public void setCheckBox(CheckBox checkBox) {
					this.checkBox = checkBox;
				}

				public TextView getTextView() {
					return textView;
				}

				public void setTextView(TextView textView) {
					this.textView = textView;
				}
			}

			/** Custom adapter for displaying an array of Planet objects. */
			private class PlanetArrayAdapter extends ArrayAdapter<Planet> {

				private LayoutInflater inflater;

				public PlanetArrayAdapter(Context context, List<Planet> planetList) {
					super(context, R.layout.admin_new_card_tags_simplerow, R.id.rowTextView, planetList);
					// Cache the LayoutInflate to avoid asking for a new one each time.
					inflater = LayoutInflater.from(context);
				}

				public View getView(int position, View convertView, ViewGroup parent) {
					// Planet to display
					Planet planet = (Planet) this.getItem(position);

					// The child views in each row.
					CheckBox checkBox;
					TextView textView;

					// Create a new row view
					if (convertView == null) {
						convertView = inflater.inflate(R.layout.admin_new_card_tags_simplerow, null);

						// Find the child views.
						textView = (TextView) convertView
								.findViewById(R.id.rowTextView);
						checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);

						// Optimization: Tag the row with it's child views, so we don't
						// have to
						// call findViewById() later when we reuse the row.
						convertView.setTag(new PlanetViewHolder(textView, checkBox));

						// If CheckBox is toggled, update the planet it is tagged with.
						checkBox.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								CheckBox cb = (CheckBox) v;
								Planet planet = (Planet) cb.getTag();
								planet.setChecked(cb.isChecked());
							}
						});
					}
					// Reuse existing row view
					else {
						// Because we use a ViewHolder, we avoid having to call
						// findViewById().
						PlanetViewHolder viewHolder = (PlanetViewHolder) convertView
								.getTag();
						checkBox = viewHolder.getCheckBox();
						textView = viewHolder.getTextView();
					}

					// Tag the CheckBox with the Planet it is displaying, so that we can
					// access the planet in onClick() when the CheckBox is toggled.
					checkBox.setTag(planet);

					// Display planet data
					checkBox.setChecked(planet.isChecked());
					textView.setText(planet.getName());

					return convertView;
				}

			}

			public Object onRetainNonConfigurationInstance() {
				return planets;
			}
			//return v;
		}
	}

