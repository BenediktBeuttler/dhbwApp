package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wi2010d.dhbwapp.control.Delete;
import wi2010d.dhbwapp.control.Statistics;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Stack;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Activity for Statistics. Screen contains three tabs: Overview Tab: Shows
 * overall learning time and Status of selected Stacks or all Stacks Progress:
 * Shows progress (% in drawer) of the last runthroughs of selected Stack Last
 * Runthrough: Shows details to last runthrough (stack, date, duration, status
 * before & after)
 * 
 * @author Schuetzler
 * 
 */
public class StatisticsScreen extends OnResumeFragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	boolean backPressed = false;
	public static Context context;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();

		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.statistics_screen);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.statistics);
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

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mViewPager.setCurrentItem(extras.getInt("Tab"));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics_screen, menu);
		return true;
	}

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
		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			return false;
		}
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
				fragment = new Overview();
				break;
			case 1:
				fragment = new Progress();
				break;
			case 2:
				fragment = new LastReview();
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
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.statistics_screen_tab_overview)
						.toUpperCase();
			case 1:
				return getString(R.string.statistics_screen_tab_progress)
						.toUpperCase();
			case 2:
				return getString(R.string.statistics_screen_tab_lastreview)
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

	/**
	 * Overview Class shows, updates, etc. the Overview Screen in the Statistic
	 * Part
	 * 
	 * @author Schuetzler
	 * 
	 */
	public static class Overview extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		TextView totalDuration;
		TextView totalNumberOfCards;
		TextView dontKnow;
		TextView notSure;
		TextView sure;

		Spinner spinner;
		boolean stacksAvailable;

		public Overview() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater
					.inflate(R.layout.statistics_screen_overview, null);

			// Initialize the spinner that should contain the names of all
			// existing stacks later
			spinner = (Spinner) v
					.findViewById(R.id.lbl_statistics_overview_stackSpinner);

			// Instantiate List that contains the names of existing stacks later
			List<String> items = new ArrayList<String>();

			// collect stack names in list
			for (Stack stack : Stack.allStacks) {

				// add Stack to list
				items.add(stack.getStackName());
			}

			// If the size of the list 0, there is no Stack available, if the
			// size
			// is 1 or more, the List will be sorted and a new entry
			// "All Stacks" is added
			if (items.size() == 0) {
				stacksAvailable = false;
				items.add("No stacks available");
			} else {
				Collections.sort(items);
				stacksAvailable = true;
				items.add(0, "All Stacks");
			}

			// Instantiate and initialize Adapter
			ArrayAdapter<String> adapter;
			adapter = new ArrayAdapter<String>(v.getContext(),
					android.R.layout.simple_spinner_item, items);

			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);

			// Set onItemSelectedListener
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					// Get selected Item and update Content of Overall Screen
					if (stacksAvailable) {
						String name = (String) parent
								.getItemAtPosition(position);
						setContent(name, (View) view.getParent().getParent());
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			// Set content of Overall Screen (for on create)
			if (stacksAvailable) {
				setContent("All Stacks", v);
			}

			return v;
		}

		/**
		 * Method that updates the Content (--> TextViews) of Overall Screen
		 * Parameter name is the name of selected Stack
		 * 
		 * @param name
		 *            selected Stack name or "All Stacks" for all Stacks
		 * @param v
		 *            view
		 */
		private void setContent(String name, View v) {

			// Get the relevant TextViews
			totalDuration = (TextView) v
					.findViewById(R.id.lbl_statistics_overview_totalDuration);
			totalNumberOfCards = (TextView) v
					.findViewById(R.id.lbl_statistics_overview_totalNumberOfCards);
			dontKnow = (TextView) v
					.findViewById(R.id.lbl_statistics_overview_dontKnow);
			notSure = (TextView) v
					.findViewById(R.id.lbl_statistics_overview_notSure);
			sure = (TextView) v.findViewById(R.id.lbl_statistics_overview_sure);

			// If "All Stacks" is selected
			if (name.equals("All Stacks")) {

				// Get and set total duration and total Number of Cards for all
				// Stacks
				totalDuration.setText(Statistics.getInstance()
						.getOverallDuration());
				totalNumberOfCards.setText(Statistics.getInstance()
						.getTotalNumberOfCards());

				// Get the overall status of drawers
				String[] actualStatus = Statistics.getInstance()
						.getOverallActualDrawerStatus();

				// Put Overall Status of drawers in the TextViews
				dontKnow.setText("" + actualStatus[0]);
				notSure.setText("" + actualStatus[1]);
				sure.setText("" + actualStatus[2]);

				// If there is a specific Stack selected
			} else {

				// Get and set total duration and total Number of Cards for
				// selected Stack
				totalDuration.setText(Statistics.getInstance()
						.getStackOverallDuration(name));
				totalNumberOfCards.setText(Statistics.getInstance()
						.getTotalNumberOfCards(name));

				// Get the overall status of drawers
				String[] actualStatus = Statistics.getInstance()
						.getActualDrawerStatus(name);

				// Put Overall Status of drawers in the TextViews
				dontKnow.setText("" + actualStatus[0]);
				notSure.setText("" + actualStatus[1]);
				sure.setText("" + actualStatus[2]);
			}

		}

	}

	/**
	 * Progress Class, updating and setting the Progress Part of the Statistic
	 * Screen
	 * 
	 */
	public static class Progress extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		// Instantiating paramter
		private Spinner spinner;
		private Button showDiagram;
		boolean stacksAvailable = true;

		public Progress() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater
					.inflate(R.layout.statistics_screen_progress, null);

			// Initializing Spinner that should contain all Stack Names later
			spinner = (Spinner) v
					.findViewById(R.id.lbl_statistics_progress_stackSpinner);

			// Initializing Button to show diagram and set OnClickListener
			showDiagram = (Button) v
					.findViewById(R.id.btn_statistics_progress_diagram);
			showDiagram.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// start a new activity which calls the graphical diagram to
					// show
					Intent intent = new Intent(context,
							StatisticsProgressDiagramActivity.class);

					// ini Y-value of sure answers
					int[] sureY = new int[10];
					// ini Y-value of notSure answers
					int[] notSureY = new int[10];
					// ini Y-value of dontKnow answers
					int[] dontKnowY = new int[10];

					// TODO: Niklas get StackName (bei null), stimmt
					// allData.length so in der schleife?
					int[][] allData = Statistics.getInstance().getLastProgress(
							null);
					for (int i = 0; i < allData.length; i++) {
						// fill the variable dontKnow with the Y-values
						dontKnowY[i] = allData[i][0];
						// fill the variable with notSureY the Y-values
						notSureY[i] = allData[i][1];
						// fill the variable sureY with Y-values
						sureY[i] = allData[i][2];
					}

					// add the values via putExtra to the intent
					intent.putExtra("notSureY", notSureY);
					intent.putExtra("sureY", sureY);
					intent.putExtra("dontKnowY", dontKnowY);

					// start the Activity with the added extras
					startActivity(intent);
				}
			});

			// Instantiating list that contains all Stack Names later
			List<String> items = new ArrayList<String>();

			// collect stack names in list
			for (Stack stack : Stack.allStacks) {
				// Add actual Stack Name to List
				items.add(stack.getStackName());
			}
			// If the List is empty
			if (items.size() == 0) {
				stacksAvailable = false;
				items.add("No stacks available");
			} else {
				// Sort List items
				Collections.sort(items);
			}

			// Instantiating and Initializing adapter
			ArrayAdapter<String> adapter;
			adapter = new ArrayAdapter<String>(v.getContext(),
					android.R.layout.simple_spinner_item, items);

			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);

			// Set OnItemSelectedListener of Spinner
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					// If there are available Stacks, get the selected Stack and
					// set Content of
					// Page, based on the Stack Name
					if (stacksAvailable) {
						String name = (String) parent
								.getItemAtPosition(position);
						setContent(name, (View) view.getParent().getParent());
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			return v;
		}

		/**
		 * Method that sets the Content of Progress Screen, based on the
		 * selected Stack
		 * 
		 * @param name
		 *            selected stack name or "All Stacks" if selected
		 * @param v
		 */
		private void setContent(String name, View v) {

			// Instantiate Array that shall contain different TextViews (=Cells)
			// of the table
			TextView[][] Cells;
			Cells = new TextView[10][4];

			// Fill Array ([COLUMN][ROW])
			Cells[0][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell01);
			Cells[0][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell11);
			Cells[0][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell21);
			Cells[0][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell31);

			Cells[1][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell02);
			Cells[1][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell12);
			Cells[1][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell22);
			Cells[1][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell32);

			Cells[2][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell03);
			Cells[2][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell13);
			Cells[2][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell23);
			Cells[2][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell33);

			Cells[3][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell04);
			Cells[3][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell14);
			Cells[3][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell24);
			Cells[3][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell34);

			Cells[4][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell05);
			Cells[4][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell15);
			Cells[4][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell25);
			Cells[4][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell35);

			Cells[5][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell06);
			Cells[5][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell16);
			Cells[5][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell26);
			Cells[5][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell36);

			Cells[6][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell07);
			Cells[6][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell17);
			Cells[6][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell27);
			Cells[6][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell37);

			Cells[7][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell08);
			Cells[7][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell18);
			Cells[7][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell28);
			Cells[7][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell38);

			Cells[8][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell09);
			Cells[8][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell19);
			Cells[8][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell29);
			Cells[8][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell39);

			Cells[9][0] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell010);
			Cells[9][1] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell110);
			Cells[9][2] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell210);
			Cells[9][3] = (TextView) v
					.findViewById(R.id.lbl_statistics_progress_cell310);

			// deleting content of table before filling with new data
			for (int k = 0; k < 10; k++) {
				for (int l = 0; l < 4; l++) {
					Cells[k][l].setText("-");
					Cells[k][l].setTextColor(Color.WHITE);
				}
			}

			// Get List with the last Runthrough dates
			List<String> lastDates = Statistics.getInstance()
					.getLastRunthroughDates(name);

			// Create Array with the last Progress
			int[][] progress = Statistics.getInstance().getLastProgress(name);

			// Get Number of Runthroughs and subract 1 --> to use it in list
			int runthroughCount = (Statistics.getInstance()
					.getNumberOfRunthroughs(name)) - 1;

			// For number of available Runthroughs
			for (int i = 0; i < Statistics.getInstance()
					.getNumberOfRunthroughs(name); i++) {

				// Set the Date-TextViews
				Cells[i][0].setText(lastDates.get(runthroughCount));

				// Set progress and color of Text of Dont't Know
				Cells[i][1].setText(progress[runthroughCount][0] + "%");
				if (progress[runthroughCount][0] > 40) {
					Cells[i][1].setTextColor(Color.RED);
				} else {
					Cells[i][1].setTextColor(Color.GREEN);
				}

				// Set progress and color of Text of Not Sure
				Cells[i][2].setText(progress[runthroughCount][1] + "%");
				if (progress[runthroughCount][1] > 40) {
					Cells[i][2].setTextColor(Color.RED);
				} else {
					Cells[i][2].setTextColor(Color.GREEN);
				}

				// Set progress and color of Text of Sure
				Cells[i][3].setText(progress[runthroughCount][2] + "%");
				if (progress[runthroughCount][2] > 90) {
					Cells[i][3].setTextColor(Color.GREEN);
				} else {
					Cells[i][3].setTextColor(Color.RED);
				}

				runthroughCount--;
			}
		}
	}

	/**
	 * LastReview Class, setting and updating the Last Review part of the
	 * Statistic Screen
	 * 
	 * @author Schuetzler
	 * 
	 */
	public static class LastReview extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		TextView stackName;
		TextView lastRunthrough;
		TextView duration;

		TextView dontKnowBefore;
		TextView notSureBefore;
		TextView sureBefore;

		TextView dontKnowAfter;
		TextView notSureAfter;
		TextView sureAfter;

		String[] statusBefore = Statistics.getInstance().getStatusBefore();
		String[] statusAfter = Statistics.getInstance().getStatusAfter();

		boolean stacksAvailable;

		public LastReview() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.statistics_screen_lastreview,
					null);

			// If there are any existing Stacks
			if (Stack.allStacks.isEmpty()) {
				// if not, do nothing
				;
			} else {

				// if there are any existing Stacks

				// Update TextViews
				stackName = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_stackName);
				stackName.setText(Statistics.getInstance()
						.getLastRunthroughName());

				lastRunthrough = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_lastRunthrough);
				lastRunthrough.setText(Statistics.getInstance()
						.getLastRunthroughDate());

				duration = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_duration);
				duration.setText(Statistics.getInstance()
						.getDurationOfLastRunthrough());

				dontKnowBefore = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_dontKnowBefore);
				dontKnowBefore.setText(statusBefore[0]);

				notSureBefore = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_notSureBefore);
				notSureBefore.setText(statusBefore[1]);

				sureBefore = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_sureBefore);
				sureBefore.setText(statusBefore[2]);

				dontKnowAfter = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_dontKnowAfter);
				dontKnowAfter.setText(statusAfter[0]);

				notSureAfter = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_notSureAfter);
				notSureAfter.setText(statusAfter[1]);

				sureAfter = (TextView) v
						.findViewById(R.id.lbl_statistics_lastReview_sureAfter);
				sureAfter.setText(statusAfter[2]);
			}

			return v;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!backPressed) {
				backPressed = true;
				if (getIntent() != null && getIntent().getExtras() != null) {
					if (getIntent().getExtras().getBoolean("isRandomStack",
							false)) {
						for (Stack stack : Stack.allStacks) {
							if (stack.getStackName().equals(
									Statistics.getInstance()
											.getLastRunthroughName())) {
								Delete.getInstance().deleteStack(stack);
							}
						}
					}
				}
			}
			finish();
			return true;
		}
		finish();
		return false;
	}

}
