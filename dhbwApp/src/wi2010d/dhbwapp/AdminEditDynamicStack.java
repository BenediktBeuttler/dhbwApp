package wi2010d.dhbwapp;

import java.util.ArrayList;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Tabbed Activity, the first Tab offers the possibility to change the name of
 * the dynamic stack, the second tab offers the possibility to change the tags.
 */
public class AdminEditDynamicStack extends OnResumeFragmentActivity implements
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
	private ArrayList<Tag> stackTagList = new ArrayList<Tag>();
	private Stack stack;
	private String stackName;
	private EditText txt_stack_name;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();
		context = this;
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_new_card);

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
		for (Stack searchStack : Stack.allStacks) {
			if (searchStack.getStackName().equals(stackName)) {
				stack = searchStack;
				break;
			}
		}

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
		getMenuInflater().inflate(R.menu.admin_edit_dynamic_stack, menu);
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
				fragment = new EditDynStack();
				break;
			case 1:
				// set all tags unchecked
				for (Tag tag : Tag.allTags) {
					tag.setChecked(false);
				}
				// set the stacks' tags checked
				for (Tag tag : stack.getDynamicStackTags()) {
					tag.setChecked(true);
				}
				fragment = new AdminTagListFragment();
			default:
				break;
			}

			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putBoolean("buttonInvisible", true);
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
				return "Edit Name";
			case 1:
				return "Edit Tags";
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
	 * This Fragment is used to change the name of the dynamic stack.
	 */
	public class EditDynStack extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public EditDynStack() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.admin_edit_stack_screen, null);

			txt_stack_name = (EditText) v
					.findViewById(R.id.txt_admin_edit_stack);
			txt_stack_name.setText(stackName);
			txt_stack_name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// create dialog to insert name of new stack
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle("Edit Name");

					// Set an EditText view to get user input
					final EditText input = new EditText(getApplicationContext());
					input.setText(txt_stack_name.getText());
					alert.setView(input);

					// Set the new Stack name
					alert.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Toast toast;
									if (input.getText().toString().equals("")) {
										// handle no text
										toast = Toast.makeText(
												getApplicationContext(),
												"Please insert a name!",
												Toast.LENGTH_SHORT);
										toast.show();
									} else {
										String dyn_stack_name = input.getText()
												.toString();
										txt_stack_name.setText(dyn_stack_name);
									}
								}
							});

					alert.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							});
					alert.show();

				}
			});

			return v;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_start_screen:
			startActivity(new Intent(this, StartScreen.class));
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpEditCardsStackScreen.class));
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
			return true;
		case R.id.btn_admin_edit_stack_save:
			Stack foundStack;
			boolean nameAlreadyTaken = false;
			String newName = txt_stack_name.getText().toString();
			for (Stack stack : Stack.allStacks) {
				if (stack.getStackName().equals(newName)) {
					nameAlreadyTaken = true;
					break;
				}
			}

			if (nameAlreadyTaken) {
				Toast.makeText(
						getApplicationContext(),
						"The stack with the name "
								+ newName
								+ " is already existing, please select another name!",
						Toast.LENGTH_LONG).show();
			} else {
				for (Stack stack : Stack.allStacks) {
					if (stack.getStackName().equals(stackName)) {
						foundStack = stack;
						if (newName.equals("")) {
							// ErrorHandler started, if name is empty
							ErrorHandlerFragment newFragment = ErrorHandlerFragment
									.newInstance(
											R.string.error_handler_no_input,
											ErrorHandlerFragment.NO_INPUT);
							newFragment.show(getFragmentManager(), "dialog");
						}
						for (Tag tag : Tag.allTags) {
							if (tag.isChecked()) {
								stackTagList.add(tag);
							}
						}
						if (stackTagList.size() <= 0) {
							// ErrorHandler started, if no tags are selected
							ErrorHandlerFragment newFragment = ErrorHandlerFragment
									.newInstance(R.string.error_handler_no_tag,
											ErrorHandlerFragment.NO_TAG);
							newFragment.show(getFragmentManager(), "dialog");
						} else {
							Edit.getInstance().changeStackName(newName,
									foundStack);
							foundStack.setDynamicStackTags(stackTagList);
							Create.getInstance().updateDynStack(foundStack);
							Toast toast = Toast.makeText(
									getApplicationContext(), "Dynamic Stack "
											+ txt_stack_name.getText()
													.toString()
											+ " updated succesfully",
									Toast.LENGTH_SHORT);
							toast.show();
							setResult(AdminChooseStackScreen.RESULT_OK);
							finish();
						}

					}
				}
			}
			return true;
		default:
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(this.getFragmentManager(), "dialog");
			return false;
		}
	}

}
