package wi2010d.dhbwapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Tag;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Tabbed Activity with 3 Screens: First Screen offers the possibility to edit
 * the cards' front text and take a picture for the front. The Second screen
 * offers the same cases for the cards' back side. The third screen let's you
 * edit the cards' tags.
 */
public class AdminEditCard extends OnResumeFragmentActivity implements
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
	EditText cardFront;
	EditText cardBack;
	Card card;
	int cardID;

	// The array list holding the cards tags, used to change the tags when
	// saving.
	private ArrayList<Tag> cardTagList = new ArrayList<Tag>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_edit_card);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				cardID = -1;
			} else {
				cardID = extras.getInt("cardID");
			}
		} else {
			cardID = (Integer) savedInstanceState.getSerializable("cardID");
		}
		for (Card card : Card.allCards) {
			if (card.getCardID() == cardID) {
				this.card = card;
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
				fragment = new EditCardFront();
				break;
			case 1:
				fragment = new EditCardBack();
				break;
			case 2:
				// Uncheck all tags, then check the ones from the card
				for (Tag tag : Tag.allTags) {
					tag.setChecked(false);
				}
				for (Tag tag : card.getTags()) {
					tag.setChecked(true);
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
	 * Fragment for the cards' front, offering a TextView and a buttons to take
	 * a picture or view it.
	 */
	public class EditCardFront extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private Button editPicture;
		private ImageButton showPictureButton;
		public Uri imageUriFront;
		public static final int TAKE_PICTURE = 1;

		public EditCardFront() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.admin_edit_card_front, null);

			cardFront = (EditText) v.findViewById(R.id.txt_edit_card_front);
			cardFront.setText(card.getCardFront());

			// Set up button for taking new picture
			editPicture = (Button) v.findViewById(R.id.btn_edit_picture_front);
			editPicture.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent takePicture = new Intent(
							"android.media.action.IMAGE_CAPTURE");
					Date date = new Date();
					SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
					String picName = sd.format(date);

					if (!new File(Environment.getExternalStorageDirectory()
							.getPath() + "/knowItOwl/pictures").exists()) {
						new File(Environment.getExternalStorageDirectory()
								.getPath() + "/knowItOwl/pictures").mkdir();
					}

					File photo = new File(Environment
							.getExternalStorageDirectory()
							+ "/knowItOwl/pictures", picName + ".jpg");
					takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photo));
					imageUriFront = Uri.fromFile(photo);
					Log.e("AdminEditCard", "test: " + imageUriFront.getPath());
					startActivityForResult(takePicture, TAKE_PICTURE);
				}
			});

			// Set up Button for showing the actual picture
			showPictureButton = (ImageButton) v
					.findViewById(R.id.btn_edit_picture_front_show);

			updateImageButtonAdminEdit(true, showPictureButton);

			showPictureButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!card.getCardFrontPicture().equals("")) {
						Intent show = new Intent();
						show.setAction(Intent.ACTION_VIEW);
						show.setDataAndType(Uri.fromFile(new File(card
								.getCardFrontPicture())), "image/*");
						startActivity(show);
					}
				}
			});

			return v;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// Gets called when a picture is taken
			if (resultCode == Activity.RESULT_OK) {
				Edit.getInstance().addNewPicToCard(true,
						imageUriFront.getPath(), card);

				updateImageButtonAdminEdit(true, showPictureButton);

				Toast.makeText(getApplicationContext(),
						"Picture saved under: " + imageUriFront.getPath(),
						Toast.LENGTH_LONG).show();
			}
		}

	}

	/**
	 * Fragment for the cards' back, offering text view + buttons to take and
	 * view picture.
	 */
	public class EditCardBack extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		// Button save;

		private Button editPicture;
		private ImageButton showPictureButton;
		public Uri imageUri;
		public static final int TAKE_PICTURE = 1;

		public EditCardBack() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.admin_edit_card_back, null);

			cardBack = (EditText) v.findViewById(R.id.txt_edit_card_back);
			cardBack.setText(card.getCardBack());

			// Set up edit picture button
			editPicture = (Button) v.findViewById(R.id.btn_edit_picture_back);
			editPicture.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent takePicture = new Intent(
							"android.media.action.IMAGE_CAPTURE");
					Date date = new Date();
					SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
					String picName = sd.format(date);

					if (!new File(Environment.getExternalStorageDirectory()
							.getPath() + "/knowItOwl/pictures").exists()) {
						new File(Environment.getExternalStorageDirectory()
								.getPath() + "/knowItOwl/pictures").mkdir();
					}

					File photo = new File(Environment
							.getExternalStorageDirectory()
							+ "/knowItOwl/pictures", picName + ".jpg");
					takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photo));
					imageUri = Uri.fromFile(photo);
					Log.e("AdminEditCard", "test: " + imageUri.getPath());
					startActivityForResult(takePicture, TAKE_PICTURE);
				}
			});

			// Set up show picture button
			showPictureButton = (ImageButton) v
					.findViewById(R.id.btn_edit_picture_back_show);

			updateImageButtonAdminEdit(false, showPictureButton);

			showPictureButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!card.getCardBackPicture().equals("")) {
						Intent show = new Intent();
						show.setAction(Intent.ACTION_VIEW);
						show.setDataAndType(Uri.fromFile(new File(card
								.getCardBackPicture())), "image/*");
						startActivity(show);
					}
				}
			});

			return v;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {

			// Gets called, when a picture is taken
			if (resultCode == Activity.RESULT_OK) {
				Edit.getInstance().addNewPicToCard(false, imageUri.getPath(),
						card);

				updateImageButtonAdminEdit(false, showPictureButton);

				Toast.makeText(getApplicationContext(),
						"Picture saved under: " + imageUri.getPath(),
						Toast.LENGTH_LONG).show();
			}
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

	// TODO: Gucken, ob hier auch dieselbe Methode aus der Learner-Klasse
	// aufgerufen werden kann
	public boolean updateImageButtonAdminEdit(boolean front,
			ImageButton pictureBtn) {
		final int THUMBNAIL_SIZE = 128;

		Log.e("AdminEditCard",
				"update Image Button erreicht: " + card.getCardBackPicture());

		if (front) {
			if (!card.getCardFrontPicture().equals("")
					&& checkPictureAvailability(true)) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(new File(
							card.getCardFrontPicture()));
				} catch (FileNotFoundException e) {
					// TODO Bene: General Error
					e.printStackTrace();
				}
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

				imageBitmap = Bitmap.createScaledBitmap(imageBitmap,
						THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] byteArray = baos.toByteArray();

				pictureBtn.setVisibility(ImageButton.VISIBLE);
				pictureBtn.setImageBitmap(imageBitmap);
			} else {
				pictureBtn.setVisibility(ImageButton.GONE);
			}
		} else {
			Log.e("AdminEditCard", "CardBack:" + card.getCardBackPicture());
			Log.e("AdminEditCard", "Ergebnis Availability Check: "
					+ checkPictureAvailability(false));
			if (!card.getCardBackPicture().equals("")
					&& checkPictureAvailability(false)) {
				Log.e("AdminEditCard",
						"UpdateBackButton erreicht: "
								+ card.getCardBackPicture());
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(new File(
							card.getCardBackPicture()));
				} catch (FileNotFoundException e) {
					// TODO Add General Error
					e.printStackTrace();
				}
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

				imageBitmap = Bitmap.createScaledBitmap(imageBitmap,
						THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] byteArray = baos.toByteArray();

				pictureBtn.setVisibility(ImageButton.VISIBLE);
				pictureBtn.setImageBitmap(imageBitmap);
			} else {
				pictureBtn.setVisibility(ImageButton.GONE);
			}
		}

		return true;
	}

	/**
	 * Check if picture exists
	 * 
	 * @param front
	 *            : boolean if to check front picture (true) or back picture
	 *            (false)
	 * @return
	 */
	private boolean checkPictureAvailability(boolean front) {

		File picture;

		// Create file with the path where the picture is supposed to be stored
		if (front) {
			picture = new File(card.getCardFrontPicture());
		} else {
			picture = new File(card.getCardBackPicture());
		}

		// Check if picture exists and return result
		if (picture.exists()) {
			return true;
		} else {
			return false;
		}
	}

	// Options menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_edit_card, menu);
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
		case R.id.btn_admin_edit_card_save:
			if (isCardNotEmpty()) {
				// find the checked Tags from the list
				cardTagList.clear();
				for (Tag tag : Tag.allTags) {
					if (tag.isChecked()) {
						cardTagList.add(tag);
					}
				}
				String back = cardBack.getText().toString();
				String front = cardFront.getText().toString();

				setResult(LearningCard.RESULT_CHANGED);
				Edit.getInstance().changeCard(front, back,
						card.getCardFrontPicture(), card.getCardBackPicture(),
						cardTagList, card);
				finish();
			}
			return true;
		default:
			ErrorHandler error = new ErrorHandler(getApplicationContext());
			error.handleError(1);
			return false;
		}
	}

}
