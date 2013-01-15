package wi2010d.dhbwapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Activity for creating a new card. Tabbed Activity with 3 Screens: First
 * Screen offers the possibility to add the cards' front text and take a picture
 * for the front. The Second screen offers the same cases for the cards' back
 * side. The third screen let's you add the cards' tags.
 */
public class AdminNewCard extends OnResumeFragmentActivity implements
		ActionBar.TabListener {

	public String cardFrontPic = "";
	public String cardBackPic = "";

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

	public static final int TAKE_PICTURE_FRONT = 1;
	private ImageButton takePictureFront;
	private ImageButton deletePictureFront;
	private ImageButton showPictureFront;
	public Uri imageUriFront;

	public static final int TAKE_PICTURE_BACK = 2;
	private ImageButton takePictureBack;
	private ImageButton deletePictureBack;
	private ImageButton showPictureBack;
	public Uri imageUriBack;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	private Card card;
	private ArrayList<Tag> cardTagList = new ArrayList<Tag>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
				// set all tags unchecked
				for (Tag tag : Tag.allTags) {
					tag.setChecked(false);
				}
				fragment = new AdminTagListFragment();

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

	/**
	 * Fragment for the cards' front, offering text view + ImageButtons to take
	 * and view picture.
	 */
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

			cardFront = (EditText) v.findViewById(R.id.txt_new_card_front);

			// Set up ImageButton to take new picture
			takePictureFront = (ImageButton) v
					.findViewById(R.id.btn_admin_new_card_picture_front);
			takePictureFront.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// New intent to take picture
					Intent takePicture = new Intent(
							"android.media.action.IMAGE_CAPTURE");

					// Create unique name for picture with help of the actual
					// date
					Date date = new Date();
					SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
					String picName = sd.format(date);

					// Check if there is a path /knowItOwl/pictures available
					// if not --> create it
					if (!new File(Environment.getExternalStorageDirectory()
							.getPath() + "/knowItOwl/pictures").exists()) {
						new File(Environment.getExternalStorageDirectory()
								.getPath() + "/knowItOwl/pictures").mkdir();
					}

					// New file for the picture
					File photo = new File(Environment
							.getExternalStorageDirectory()
							+ "/knowItOwl/pictures", picName + ".jpg");
					takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photo));
					imageUriFront = Uri.fromFile(photo);

					// Start activity
					startActivityForResult(takePicture, TAKE_PICTURE_FRONT);

				}
			});

			// Set up image Butto
			showPictureFront = (ImageButton) v
					.findViewById(R.id.btn_admin_new_card_picture_front_show);

			showPictureFront.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// Check if there is an existing picture
					if (!cardFrontPic.equals("")
							&& checkPictureAvailability(true)) {

						// Show picture in Gallery
						Intent show = new Intent();
						show.setAction(Intent.ACTION_VIEW);
						show.setDataAndType(
								Uri.fromFile(new File(cardFrontPic)), "image/*");
						startActivity(show);
					}
				}
			});

			// Set up ImageButton for deleting the picture
			deletePictureFront = (ImageButton) v
					.findViewById(R.id.btn_admin_new_card_picture_front_delete);

			deletePictureFront.setVisibility(ImageButton.GONE);

			deletePictureFront.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// Set front pic = "" and update Image Button and delete
					// file from SD-Card
					File fileToDelete = new File(cardFrontPic);
					if (fileToDelete.delete()) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"Picture has been deleted successfully",
								Toast.LENGTH_LONG);
						toast.show();
					}

					// set cardFrontPic "" and set visibility of ImageButton =
					// GONE as there is
					// no picture anymore, update thumbnail
					cardFrontPic = "";
					deletePictureFront.setVisibility(ImageButton.GONE);
					updateImageButtonNewCard(true, showPictureFront);

				}
			});

			// set imageButton GONE as there is no picture onCreate
			showPictureFront.setVisibility(ImageButton.GONE);

			return v;
		}
	}

	/**
	 * Fragment for the cards' back, offering text view + buttons to take and
	 * view picture.
	 */
	public class NewCardBack extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public NewCardBack() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.admin_new_card_back, null);

			cardBack = (EditText) v.findViewById(R.id.txt_new_card_back);

			takePictureBack = (ImageButton) v
					.findViewById(R.id.btn_admin_new_card_picture_back);
			takePictureBack.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// New intent to take picture
					Intent takePicture = new Intent(
							"android.media.action.IMAGE_CAPTURE");

					// Create unique picture name with help of the actual date
					Date date = new Date();
					SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
					String picName = sd.format(date);

					// Check if file /knowItOwl/picture exists --> if not,
					// create it
					if (!new File(Environment.getExternalStorageDirectory()
							.getPath() + "/knowItOwl/pictures").exists()) {
						new File(Environment.getExternalStorageDirectory()
								.getPath() + "/knowItOwl/pictures").mkdir();
					}

					// Create new file
					File photo = new File(Environment
							.getExternalStorageDirectory()
							+ "/knowItOwl/pictures", picName + ".jpg");
					takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photo));
					imageUriBack = Uri.fromFile(photo);

					// Start activity and take picture
					startActivityForResult(takePicture, TAKE_PICTURE_BACK);

				}
			});

			// Set up Image Button (for thumbnail)
			showPictureBack = (ImageButton) v
					.findViewById(R.id.btn_admin_new_card_picture_back_show);

			showPictureBack.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// Check if there is any existing Card
					if (!cardBackPic.equals("")
							&& checkPictureAvailability(false)) {

						// New intent to show picture
						Intent show = new Intent();
						show.setAction(Intent.ACTION_VIEW);
						show.setDataAndType(
								Uri.fromFile(new File(cardBackPic)), "image/*");
						startActivity(show);
					}
				}
			});

			// Set up Button for deleting the picture
			deletePictureBack = (ImageButton) v
					.findViewById(R.id.btn_admin_new_card_picture_back_delete);

			deletePictureBack.setVisibility(ImageButton.GONE);

			deletePictureBack.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// Set back pic = "" and update Image Button and delete file
					// from SD-Card
					File fileToDelete = new File(cardBackPic);

					if (fileToDelete.delete()) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"Picture has been deleted successfully",
								Toast.LENGTH_LONG);
						toast.show();
					}

					cardBackPic = "";
					deletePictureBack.setVisibility(ImageButton.GONE);
					updateImageButtonNewCard(false, showPictureBack);

				}
			});

			// Set picture visibility GONE as there is no picture to show
			// anymore
			showPictureBack.setVisibility(ImageButton.GONE);

			return v;
		}
	}

	/**
	 * Checks if the cards' front or back text is empty or not
	 * 
	 * @return true, if front+back is filled with text
	 */
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

		switch (requestCode) {
		case STACK_CHOSEN:
			stackName = data.getExtras().getString("stackName");
			for (Stack stack : Stack.allStacks) {
				if (stack.getStackName().equals(stackName)) {

					Edit.getInstance().addCardToStack(stack, card);

					toast = Toast.makeText(this, "Card added to stack "
							+ stackName, Toast.LENGTH_LONG);
					toast.show();
					finish();
					break;
				}
			}
			break;
		case 65537:

			// If picture is taken (front)
			if (resultCode == RESULT_OK) {

				if (!cardFrontPic.equals("") && checkPictureAvailability(true)) {

					// Delete former file from SD-Card
					File fileToDelete = new File(cardFrontPic);
					fileToDelete.delete();
				}

				// Save path in cardFrontPic
				cardFrontPic = imageUriFront.getPath();

				// Update Buttons
				updateImageButtonNewCard(true, showPictureFront);
				deletePictureFront.setVisibility(ImageButton.VISIBLE);

				Toast.makeText(getApplicationContext(),
						"Picture saved under: " + imageUriFront.getPath(),
						Toast.LENGTH_LONG).show();
				break;
			}
			break;
		case 131074:
			// If picture is taken (back)
			if (resultCode == RESULT_OK) {

				if (!cardBackPic.equals("") && checkPictureAvailability(false)) {

					// Delete former file from SD-Card
					File fileToDelete = new File(cardBackPic);
					fileToDelete.delete();
				}

				// Save path in cardBackPic
				cardBackPic = imageUriBack.getPath();

				// Update ImageButtons
				updateImageButtonNewCard(false, showPictureBack);
				deletePictureBack.setVisibility(ImageButton.VISIBLE);

				Toast.makeText(getApplicationContext(),
						"Picture saved under: " + imageUriBack.getPath(),
						Toast.LENGTH_LONG).show();
				break;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Method to update an ImageButton --> set the actual thumbnail or set
	 * Button GONE if there is no picture available
	 * 
	 * @param front
	 *            : TRUE = set Image Button on front, FALSE = set Image Button
	 *            on back
	 * @param pictureBtn
	 *            : ImageButton that is to be updated
	 * @return true if it has worked
	 */
	public boolean updateImageButtonNewCard(boolean front,
			ImageButton pictureBtn) {

		// height and width of thumbnail
		final int THUMBNAIL_SIZE = 128;

		// if the ImageButton on front is to be updated
		if (front) {

			// check if there is any existing front picture
			if (!cardFrontPic.equals("") && checkPictureAvailability(true)) {

				FileInputStream fis = null;
				try {
					fis = new FileInputStream(new File(cardFrontPic));
				} catch (FileNotFoundException e) {
					ErrorHandlerFragment newFragment = ErrorHandlerFragment
							.newInstance(R.string.error_handler_file_not_found,
									ErrorHandlerFragment.FILE_NOT_FOUND);
					newFragment.show(this.getFragmentManager(), "dialog");
				}

				// new bitmap (thumbnail)
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

				imageBitmap = Bitmap.createScaledBitmap(imageBitmap,
						THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] byteArray = baos.toByteArray();

				// Update ImageButton
				pictureBtn.setVisibility(ImageButton.VISIBLE);
				pictureBtn.setImageBitmap(imageBitmap);

				// if there is no front picture available
			} else {
				pictureBtn.setVisibility(ImageButton.GONE);
			}
			// If the imageButton on back is to be updated
		} else {

			// If there is an existing picture
			if (!cardBackPic.equals("") && checkPictureAvailability(false)) {

				FileInputStream fis = null;
				try {
					fis = new FileInputStream(new File(cardBackPic));
				} catch (FileNotFoundException e) {
					ErrorHandlerFragment newFragment = ErrorHandlerFragment
							.newInstance(R.string.error_handler_file_not_found,
									ErrorHandlerFragment.FILE_NOT_FOUND);
					newFragment.show(this.getFragmentManager(), "dialog");
				}

				// Create Bitmap (Thumbnail)
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

				imageBitmap = Bitmap.createScaledBitmap(imageBitmap,
						THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] byteArray = baos.toByteArray();

				// Update ImageButton
				pictureBtn.setVisibility(ImageButton.VISIBLE);
				pictureBtn.setImageBitmap(imageBitmap);

				// If there is no back picture available
			} else {
				pictureBtn.setVisibility(ImageButton.GONE);
			}
		}

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
		case R.id.btn_admin_new_card_new_stack:

			if (isCardNotEmpty()) {
				// find the checked Tags from the list
				cardTagList.clear();
				for (Tag tag : Tag.allTags) {
					if (tag.isChecked()) {
						cardTagList.add(tag);
					}
				}

				card = Create.getInstance().newCard(
						cardFront.getText().toString(),
						cardBack.getText().toString(), cardTagList,
						cardFrontPic, cardBackPic);
				// create dialog to insert name of new stack
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("New Stack");
				alert.setMessage("Name of the new stack");

				// Set an EditText view to get user input
				final EditText input = new EditText(this);
				alert.setView(input);

				alert.setPositiveButton("Create",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Toast toast;

								if (input.getText().toString().equals("")) {
									toast = Toast.makeText(
											getApplicationContext(),
											"Please insert a stack name!",
											Toast.LENGTH_SHORT);
									toast.show();
								} else if (input.getText().toString()
										.equals("All Stacks")) {
									toast = Toast
											.makeText(
													getApplicationContext(),
													"Stack name cannot be 'All Stacks', please select another one!",
													Toast.LENGTH_LONG);
									toast.show();

								} else {
									String stackName = input.getText()
											.toString();
									Create.getInstance().newStack(stackName,
											card);

									toast = Toast
											.makeText(
													getApplicationContext(),
													"Stack "
															+ stackName
															+ " created and Card added",
													Toast.LENGTH_LONG);
									toast.show();
									finish();
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
			return true;
		case R.id.btn_admin_new_card_existing_stack:
			if (Stack.allStacks.size() == 0) {
				ErrorHandler.getInstance().handleError(
						ErrorHandler.getInstance().NO_STACK_AVAILABLE);
			} else if (isCardNotEmpty()) {
				// find the checked Tags from the list
				cardTagList.clear();
				for (Tag tag : Tag.allTags) {
					if (tag.isChecked()) {
						cardTagList.add(tag);
					}
				}
				card = Create.getInstance().newCard(
						cardFront.getText().toString(),
						cardBack.getText().toString(), cardTagList,
						cardFrontPic, cardBackPic);

				Intent i = new Intent(getApplicationContext(),
						AdminNewCardChooseStack.class);
				startActivityForResult(i, STACK_CHOSEN);
			}
		default:
			return false;
		}
	}

	/**
	 * Check if picture exists
	 * 
	 * @param front
	 *            : boolean if to check front picture (true) or back picture
	 *            (false)
	 * @return true, if picture exists
	 */
	private boolean checkPictureAvailability(boolean front) {

		File picture;

		// Create file with the path where the picture is supposed to be stored
		if (front) {
			picture = new File(cardFrontPic);
		} else {
			picture = new File(cardBackPic);
		}

		// Check if picture exists and return result
		if (picture.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
