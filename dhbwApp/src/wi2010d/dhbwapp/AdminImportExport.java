package wi2010d.dhbwapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wi2010d.dhbwapp.control.Exchange;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Stack;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminImportExport extends FragmentActivity implements
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
	static ListView importList;
	static ArrayAdapter<String> importListAdapter;
	static View importView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_import_export_screen);

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
		getMenuInflater().inflate(R.menu.admin_import_export, menu);
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
				fragment = new Import();
				break;
			case 1:
				fragment = new Export();
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
				return getString(R.string.admin_import).toUpperCase();
			case 1:
				return getString(R.string.admin_export).toUpperCase();
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

	public static class Import extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public final String ARG_SECTION_NUMBER = "section_number";

		ListView importList;
		Button importButton;
		ArrayList<String> items = new ArrayList<String>();
		ArrayAdapter<String> importListAdapter;
		Toast toast;
		View v;

		public Import() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			v = inflater.inflate(R.layout.admin_import, null);
			AdminImportExport.importView = v;

			if (!new File(Environment.getExternalStorageDirectory().getPath()
					+ "/knowItOwl/").exists()) {
				new File(Environment.getExternalStorageDirectory().getPath()
						+ "/knowItOwl/").mkdir();
			}

			importList = (ListView) v
					.findViewById(R.id.list_admin_import_stacks);
			AdminImportExport.importList = importList;

			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_UNMOUNTED)
					|| new File(Environment.getExternalStorageDirectory()
							.getPath() + "/knowItOwl/").canRead()) {

				File knowItOwlDir = new File(Environment
						.getExternalStorageDirectory().getPath()
						+ "/knowItOwl/");
				File[] fileList = knowItOwlDir.listFiles();
				if (fileList != null) {
					for (File stackName : fileList) {
						items.add(stackName.getName());
					}
				}
				if (items.size() == 0) {
					toast = Toast
							.makeText(
									getActivity(),
									"No stacks for import found, please ensure that the XML-Files are in the folder /sdcard/knowItOwl/",
									Toast.LENGTH_SHORT);
					toast.show();
					items.add("No stacks available");
				} else {
					importListAdapter = new ArrayAdapter<String>(
							v.getContext(),
							android.R.layout.simple_list_item_1, items);
					AdminImportExport.importListAdapter = importListAdapter;

					importList.setAdapter(importListAdapter);
					importList.setClickable(true);
					importList
							.setOnItemClickListener(new AdapterView.OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View v, int position, long id) {
									String stackName = ((TextView) v).getText()
											.toString();
									if (stackName.equals("No stacks available")) {
										toast = Toast
												.makeText(
														getActivity(),
														"No stacks for import found, please ensure that the XML-Files are in the folder /sdcard/knowItOwl/",
														Toast.LENGTH_LONG);
										toast.show();
										
									} else {
										try {
											Exchange.getInstance()
													.importStack(
															Environment
																	.getExternalStorageDirectory()
																	.getPath()
																	+ "/knowItOwl/"
																	+ stackName);
											toast = Toast
													.makeText(
															getActivity(),
															"Stack "
																	+ stackName
																	+ " got imported succesfully!",
															Toast.LENGTH_SHORT);
											toast.show();
										} catch (Exception e) {
											ErrorHandler.getInstance().handleError(ErrorHandler.getInstance().IMPORT_ERROR);
										}
									}
								}
							});

				}

			} else {
				/*toast = Toast.makeText(getActivity(), "No sdcard available!",
						Toast.LENGTH_SHORT);
				toast.show();*/
				ErrorHandlerFragment newFragment = ErrorHandlerFragment
						.newInstance(R.string.error_handler_no_sd, ErrorHandlerFragment.NO_SD );
				newFragment.show(getActivity().getFragmentManager(), "dialog");	
			}

			return v;
		}
	}

	public static class Export extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		String stackName;
		boolean stackChosen = false;
		Toast toast;

		public Export() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.admin_export, null);

			Spinner exportStacks = (Spinner) v
					.findViewById(R.id.spin_admin_export);
			Button exportButton = (Button) v
					.findViewById(R.id.btn_admin_export);

			List<String> items = new ArrayList<String>();

			for (Stack stack : Stack.allStacks) {
				items.add(stack.getStackName());
			}
			if (items.size() == 0) {
				items.add("No stacks available");
			} else {
				Collections.sort(items);
				items.add(0, "All Stacks");
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					v.getContext(), android.R.layout.simple_spinner_item, items);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			exportStacks.setAdapter(adapter);
			exportStacks
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

							stackName = (String) parent
									.getItemAtPosition(position);
							stackChosen = true;

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							stackChosen = false;
						}
					});

			exportButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_UNMOUNTED)
							|| !Environment.getExternalStorageState().equals(
									Environment.MEDIA_MOUNTED_READ_ONLY)
							|| new File(Environment
									.getExternalStorageDirectory().getPath()
									+ "/knowItOwl/").canWrite()) {
						if (stackChosen) {

							if (stackName.equals("All Stacks")) {

								final Runnable r = new Runnable() {
									public void run() {
										for (Stack stack : Stack.allStacks) {
											try {
												Exchange.getInstance()
														.exportStack(
																stack,
																Environment
																		.getExternalStorageDirectory()
																		.getPath()
																		+ "/knowItOwl/",
																stack.getStackName());
											} catch (Exception e) {
												ErrorHandler
														.getInstance()
														.handleError(
																ErrorHandler
																		.getInstance().EXPORT_ERROR);
											}
										}
										toast = Toast
												.makeText(
														getActivity(),
														"All stacks exported succesfully!"
																+ "to /sdcard/knowItOwl",
														Toast.LENGTH_SHORT);
										toast.show();
										AdminImportExport.importList.setAdapter(AdminImportExport
												.updateImportListAdapter());
									}
								};
								r.run();

							} else {
								final Runnable r = new Runnable() {
									public void run() {

										for (Stack stack : Stack.allStacks) {
											if (stack.getStackName().equals(
													stackName)) {
												try {
													Exchange.getInstance()
															.exportStack(
																	stack,
																	Environment
																			.getExternalStorageDirectory()
																			.getPath()
																			+ "/knowItOwl/",
																	stackName);
												} catch (Exception e) {
													ErrorHandler
															.getInstance()
															.handleError(
																	ErrorHandler
																			.getInstance().EXPORT_ERROR);
												}

												toast = Toast
														.makeText(
																getActivity(),
																"Stack "
																		+ stackName
																		+ " exported to /sdcard/knowItOwl/",
																Toast.LENGTH_SHORT);
												toast.show();
												AdminImportExport.importList
														.setAdapter(AdminImportExport
																.updateImportListAdapter());
											}
										}
									}
								};
								r.run();
							}

						} else {
							toast = Toast.makeText(getActivity(),
									"No stack chosen!", Toast.LENGTH_SHORT);
							toast.show();
						}

					} else {
						/*toast = Toast.makeText(getActivity(),
								"No sdcard available!", Toast.LENGTH_SHORT);
						toast.show();*/
						ErrorHandlerFragment newFragment = ErrorHandlerFragment
								.newInstance(R.string.error_handler_no_sd, ErrorHandlerFragment.NO_SD );
						newFragment.show(getActivity().getFragmentManager(), "dialog");	
					}
				}
			});

			return v;
		}
	}

	protected static ArrayAdapter<String> updateImportListAdapter() {
		ArrayList<String> items = new ArrayList<String>();
		File knowItOwlDir = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/knowItOwl/");
		File[] fileList = knowItOwlDir.listFiles();

		for (File stackName : fileList) {
			items.add(stackName.getName());
			Log.d("file", stackName.toString());
		}
		if (items.size() == 0) {
			items.add("No stacks available");
		}
		importListAdapter = new ArrayAdapter<String>(
				AdminImportExport.importView.getContext(),
				android.R.layout.simple_list_item_1, items);
		return importListAdapter;
	}
}
