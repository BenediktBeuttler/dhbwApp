package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wi2010d.dhbwapp.control.Statistics;
import wi2010d.dhbwapp.model.Stack;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class StatisticsScreen extends FragmentActivity implements
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

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics_screen, menu);
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
				return getString(R.string.statistics_screen_tab_overview).toUpperCase();
			case 1:
				return getString(R.string.statistics_screen_tab_progress).toUpperCase();
			case 2:
				return getString(R.string.statistics_screen_tab_lastreview).toUpperCase();
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
	
	public static class Overview extends Fragment implements OnItemSelectedListener {
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

		public Overview() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.statistics_screen_overview, null);
			
			spinner = (Spinner) v.findViewById(R.id.lbl_statistics_overview_stackSpinner);
			//spinner.setOnItemSelectedListener(this);

			List<String> items = new ArrayList<String>();
			//collect stack names in list
			for (Stack stack : Stack.allStacks) {
				items.add(stack.getStackName());
			}
			if (items.size() == 0) 
			{
				items.add("No stacks available");
			}
			else
			{			
				Collections.sort(items);
				items.add(0, "All Stacks");
			}
			
			ArrayAdapter<String> adapter;

			adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, items);
			
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);
			
			setContent("All Stacks", v);
			
			return v;
		}
		
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
		{
			String name = (String) parent.getItemAtPosition(position);
			setContent(name, parent);
		} 

		
		public void onNothingSelected(AdapterView<?> parent ) 
		{ 
			;
		}
		
		
		private void setContent(String name, View v)
		{
			
			totalDuration = (TextView) v.findViewById(R.id.lbl_statistics_overview_totalDuration);
			totalNumberOfCards = (TextView) v.findViewById(R.id.lbl_statistics_overview_totalNumberOfCards);
			dontKnow = (TextView) v.findViewById(R.id.lbl_statistics_overview_dontKnow);
			notSure = (TextView) v.findViewById(R.id.lbl_statistics_overview_notSure);
			sure = (TextView) v.findViewById(R.id.lbl_statistics_overview_sure);
			
			if (name.equals("All Stacks"))
			{
				totalDuration.setText(Statistics.getInstance().getOverallDuration());
				totalNumberOfCards.setText(Statistics.getInstance().getTotalNumberOfCards());
				
				String[] actualStatus = Statistics.getInstance().getOverallActualDrawerStatus();
				dontKnow.setText("" + actualStatus[0]);
				notSure.setText("" + actualStatus[1]);
				sure.setText("" + actualStatus[2]);
				
			}
			else
			{
				totalDuration.setText(Statistics.getInstance().getStackOverallDuration(name));
				totalNumberOfCards.setText(Statistics.getInstance().getTotalNumberOfCards(name));
				
				String[] actualStatus = Statistics.getInstance().getActualDrawerStatus(name);
				dontKnow.setText("" + actualStatus[0]);
				notSure.setText("" + actualStatus[1]);
				sure.setText("" + actualStatus[2]);
			}
			
		}
		
	}
	
	public static class Progress extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public Progress() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.statistics_screen_progress, null);
			return v;
		}
	}
	
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
		
		public LastReview() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.statistics_screen_lastreview, null);
			stackName = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_stackName);
			stackName.setText(Statistics.getInstance().getLastRunthroughName());
			
			lastRunthrough = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_lastRunthrough);
			lastRunthrough.setText(Statistics.getInstance().getLastRunthroughDate());
			
			duration = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_duration);
			duration.setText(Statistics.getInstance().getDurationOfLastRunthrough());
			
			dontKnowBefore = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_dontKnowBefore);
			dontKnowBefore.setText(statusBefore[0]);
			
			notSureBefore = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_notSureBefore);
			notSureBefore.setText(statusBefore[1]);
			
			sureBefore = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_sureBefore);
			sureBefore.setText(statusBefore[2]);
			
			
			dontKnowAfter = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_dontKnowAfter);
			dontKnowAfter.setText(statusAfter[0]);
			
			notSureAfter = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_notSureAfter);
			notSureAfter.setText(statusAfter[1]);
			
			sureAfter = (TextView) v.findViewById(R.id.lbl_statistics_lastReview_sureAfter);
			sureAfter.setText(statusAfter[2]);
			
			return v;
		}
	}

}
