package wi2010d.dhbwapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Tag;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
	TextView cardFront;
	TextView cardBack;
	Card card;
	int cardID;
	Context context;

	String cardFrontPic;
	String cardBackPic;
	boolean newFrontPictureTaken;
	boolean newBackPictureTaken;

	// The array list holding the cards tags, used to change the tags when
	// saving.
	private ArrayList<Tag> cardTagList = new ArrayList<Tag>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();
		context = this;

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
		mViewPager.setOffscreenPageLimit(3);
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
	 * Fragment for the cards' front, offering a TextView and a ImageButtons to
	 * take a picture or view it.
	 */
	public class EditCardFront extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private ImageButton mediacenter;
		private ImageButton deletePicture;
		private ImageButton showPictureButton;
		public Uri imageUriFront;
		public static final int TAKE_PICTURE = 1;
		private static final int SELECT_PHOTO = 100;

		public EditCardFront() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.admin_edit_card_front, null);

			cardFront = (TextView) v.findViewById(R.id.txt_edit_card_front);
			cardFront.setText(card.getCardFront());

			cardFront.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// create dialog to insert name of new stack
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle("Edit Text");

					// Set an EditText view to get user input
					final EditText input = new EditText(getApplicationContext());
					input.setText(cardFront.getText());
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
												"Please insert a text!",
												Toast.LENGTH_SHORT);
										toast.show();
									} else {
										String cardFrontText = input.getText()
												.toString();
										cardFront.setText(cardFrontText);
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

			// Initialize local variable containing the front picture
			cardFrontPic = card.getCardFrontPicture();
			newFrontPictureTaken = false;

			// Set up ImageButton for taking new picture
			mediacenter = (ImageButton) v
					.findViewById(R.id.btn_edit_picture_front);
			mediacenter.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {

					final CharSequence[] items = { "Take Picture",
							"Picture from Gallery", "Add Hyperlink" };

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle("Mediacenter");
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									switch (item) {
									case 0:

										// New intent to take a picture
										Intent takePicture = new Intent(
												"android.media.action.IMAGE_CAPTURE");

										checkFileAvailabilityPictures();

										// Create file for the new picture
										File photo = new File(Environment
												.getExternalStorageDirectory()
												+ "/knowItOwl/pictures",
												createPicName() + ".jpg");
										takePicture.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(photo));
										imageUriFront = Uri.fromFile(photo);

										// start activity
										startActivityForResult(takePicture,
												TAKE_PICTURE);
										break;

									case 1:

										checkFileAvailabilityPictures();

										// New intent to start gallery in order
										// to choose a
										// picture

										Intent photoPickerIntent = new Intent(
												Intent.ACTION_PICK);
										photoPickerIntent.setType("image/*");
										startActivityForResult(
												photoPickerIntent, SELECT_PHOTO);

										break;
									case 2:
										AlertDialog.Builder alert = new AlertDialog.Builder(
												v.getContext());
										// set Layout for dialog
										LinearLayout ll = new LinearLayout(v
												.getContext());
										ll.setOrientation(LinearLayout.VERTICAL);

										alert.setTitle("Add Hyperlink");

										// set edit text and text view to insert
										// link path and name it
										final TextView lblLinkPath = new TextView(
												v.getContext());
										lblLinkPath.setText("Paste Link:");

										final EditText editLinkPath = new EditText(
												v.getContext());
										ll.addView(lblLinkPath);
										ll.addView(editLinkPath);
										alert.setView(ll);
										alert.setPositiveButton(
												"Add",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														// check if inputs are
														// empty
														if (editLinkPath
																.getText()
																.toString()
																.equals("")) {
															Toast.makeText(
																	getApplicationContext(),
																	"Please insert a text.",
																	Toast.LENGTH_LONG)
																	.show();
														} else {
															// if fiels is not
															// empty open input
															// dialog for links
															String linkPathFront = editLinkPath
																	.getText()
																	.toString();
															// save text before
															// adding hyperlink
															cardFront
																	.append(linkPathFront);
															cardFront
																	.setText(cardFront
																			.getText());
														}

													}
												});

										alert.setNegativeButton(
												"Cancel",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														// cancel dialog
														dialog.cancel();
													}
												});
										builder.create();
										alert.show();
										break;
									default:
										break;
									}

								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			});

			// Set up ImageButton for showing the actual picture
			showPictureButton = (ImageButton) v
					.findViewById(R.id.btn_edit_picture_front_show);

			// Update Image ImageButton (Thumbnail)
			updateImageButtonAdminEdit(true, showPictureButton);

			// Set onClickListener on image ImageButton (to show picture in
			// Gallery)
			showPictureButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// if there is any picture available
					if (!cardFrontPic.equals("")
							&& checkPictureAvailability(true)) {
						Intent show = new Intent();
						show.setAction(Intent.ACTION_VIEW);
						show.setDataAndType(
								Uri.fromFile(new File(cardFrontPic)), "image/*");
						startActivity(show);
					}
				}
			});

			// Set up Butten to delete the Picture
			deletePicture = (ImageButton) v
					.findViewById(R.id.btn_admin_edit_card_front_picture_delete);

			// Set delete ImageButton only visible, if picture is available
			if (!cardFrontPic.equals("") && checkPictureAvailability(true)) {
				// If there is a picture available, do nothing, as the
				// ImageButton is
				// already visible
				;
			} else {
				// set ImageButton invisible
				deletePicture.setVisibility(ImageButton.GONE);
			}

			// Set on click listener
			deletePicture.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					deletePictureAndThumbnail(cardFrontPic);

					Toast.makeText(getApplicationContext(),
							"Picture has been deleted successfully",
							Toast.LENGTH_LONG).show();

					// Save changes in card and in DB
					// Edit.getInstance().deletePicFromCard(true, card);

					// Set local variable containing front picture ""
					cardFrontPic = "";
					newFrontPictureTaken = false;

					// Set delete ImageButton gone as there is no pic to delete
					// anymore
					deletePicture.setVisibility(ImageButton.GONE);

					// Update ImageButton
					updateImageButtonAdminEdit(true, showPictureButton);

				}
			});

			return v;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {

			switch (requestCode) {
			case TAKE_PICTURE:
				// Gets called when a picture is taken
				if (resultCode == Activity.RESULT_OK) {

					// If there is any picture that has been taken in edit mode
					// before
					if (newFrontPictureTaken) {
						deletePictureAndThumbnail(cardFrontPic);
					}

					// Save Path in local variable
					cardFrontPic = imageUriFront.getPath();
					newFrontPictureTaken = true;

					try {
						createThumbnail(cardFrontPic);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					// Updates the ImageButton to show the new picture as a
					// thumbnail
					updateImageButtonAdminEdit(true, showPictureButton);

					Toast.makeText(getApplicationContext(),
							"New picture was taken successfully",
							Toast.LENGTH_LONG).show();

					// Set delete ImageButton visible as there is a new pic
					// which can be deleted now
					deletePicture.setVisibility(ImageButton.VISIBLE);
					break;
				}
			case SELECT_PHOTO:

				if (resultCode == RESULT_OK) {

					// Create file with existing picture
					Uri selectedImage = data.getData();
					File picture = new File(getRealPathFromURI(selectedImage));

					// If there is any picture that has been taken in edit mode
					// before
					if (newFrontPictureTaken) {
						deletePictureAndThumbnail(cardFrontPic);
					}

					File destination = new File(
							Environment.getExternalStorageDirectory()
									+ "/knowItOwl/pictures", createPicName()
									+ ".jpg");

					try {
						copyFile(picture, destination);
						;
					} catch (IOException e) {
						// display a general error if the file could not be
						// copied
					}

					// Save path of picture in local variable
					cardFrontPic = destination.getPath();
					newFrontPictureTaken = true;

					// Create Thumbnail
					try {
						createThumbnail(cardFrontPic);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					// Updates the ImageButton to show the new picture as a
					// thumbnail
					updateImageButtonAdminEdit(true, showPictureButton);

					Toast.makeText(getApplicationContext(),
							"Set new picture successfully", Toast.LENGTH_LONG)
							.show();

					// Set delete ImageButton visible as there is a new pic
					// which
					// can be
					// deleted now
					deletePicture.setVisibility(ImageButton.VISIBLE);
					break;

				}
			}
		}

	}

	/**
	 * Fragment for the cards' back, offering text view + ImageButtons to take
	 * and view picture.
	 */
	public class EditCardBack extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		// ImageButton save;

		private ImageButton addMedia;
		private ImageButton deletePicture;
		private ImageButton showPictureButton;
		public Uri imageUri;
		public static final int TAKE_PICTURE = 1;
		public static final int SELECT_PHOTO = 100;

		public EditCardBack() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.admin_edit_card_back, null);

			cardBack = (TextView) v.findViewById(R.id.txt_edit_card_back);
			cardBack.setText(card.getCardBack());

			cardBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// create dialog to insert name of new stack
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle("Edit Text");

					// Set an EditText view to get user input
					final EditText input = new EditText(getApplicationContext());
					input.setText(cardBack.getText());
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
												"Please insert a text!",
												Toast.LENGTH_SHORT);
										toast.show();
									} else {
										String cardBackText = input.getText()
												.toString();
										cardBack.setText(cardBackText);
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

			// Initialize local variable containing the back picture path and
			// variable that indicates if there has been any new picture added
			// in current edit mode
			cardBackPic = card.getCardBackPicture();
			newBackPictureTaken = false;

			// Set up edit picture ImageButton
			addMedia = (ImageButton) v.findViewById(R.id.btn_edit_picture_back);

			addMedia.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					final CharSequence[] items = { "Take Picture",
							"Picture from Gallery", "Add Hyperlink" };

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle("Mediacenter");
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									switch (item) {
									case 0:

										// New intent to take a picture
										Intent takePicture = new Intent(
												"android.media.action.IMAGE_CAPTURE");

										checkFileAvailabilityPictures();

										// Create file for the new picture
										File photo = new File(Environment
												.getExternalStorageDirectory()
												+ "/knowItOwl/pictures",
												createPicName() + ".jpg");
										takePicture.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(photo));
										imageUri = Uri.fromFile(photo);

										// start activity
										startActivityForResult(takePicture,
												TAKE_PICTURE);
										break;

									case 1:

										checkFileAvailabilityPictures();

										// New intent to start gallery in order
										// to choose a picture

										Intent photoPickerIntent = new Intent(
												Intent.ACTION_PICK);
										photoPickerIntent.setType("image/*");
										startActivityForResult(
												photoPickerIntent, SELECT_PHOTO);

										break;
									case 2:
										AlertDialog.Builder alert = new AlertDialog.Builder(
												v.getContext());
										// set Layout for dialog
										LinearLayout ll = new LinearLayout(v
												.getContext());
										ll.setOrientation(LinearLayout.VERTICAL);

										alert.setTitle("Add Hyperlink");
										// set edit text and text view to insert
										// link path and name it
										final TextView lblLinkPath = new TextView(
												v.getContext());
										lblLinkPath.setText("Paste Link:");
										final EditText editLinkPath = new EditText(
												v.getContext());

										ll.addView(lblLinkPath);
										ll.addView(editLinkPath);
										alert.setView(ll);
										alert.setPositiveButton(
												"Add",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														// check if inputs are
														// empty
														if (editLinkPath
																.getText()
																.toString()
																.equals("")) {
															Toast.makeText(
																	getApplicationContext(),
																	"Please insert a text.",
																	Toast.LENGTH_LONG)
																	.show();
														} else {
															// if fiels is not
															// empty open input
															// dialog to insert
															// link
															String linkPathBack = editLinkPath
																	.getText()
																	.toString();
															// save text before
															// adding hyperlink

															cardBack.append(linkPathBack);
															cardBack.setText(cardBack
																	.getText());
														}

													}
												});

										alert.setNegativeButton(
												"Cancel",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
														// cancel dialog
														dialog.cancel();
													}
												});
										builder.create();
										alert.show();
										break;
									default:
										break;
									}

								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			});

			// Set up show picture ImageButton
			showPictureButton = (ImageButton) v
					.findViewById(R.id.btn_edit_picture_back_show);

			// Update the ImageButton
			updateImageButtonAdminEdit(false, showPictureButton);

			showPictureButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!cardBackPic.equals("")
							&& checkPictureAvailability(false)) {
						Intent show = new Intent();
						show.setAction(Intent.ACTION_VIEW);
						show.setDataAndType(
								Uri.fromFile(new File(cardBackPic)), "image/*");
						startActivity(show);
					}
				}
			});

			// Set up ImageButton to delete the Picture
			deletePicture = (ImageButton) v
					.findViewById(R.id.btn_admin_edit_card_back_picture_delete);

			// Set delete ImageButton only visible, if picture is available
			if (!cardBackPic.equals("") && checkPictureAvailability(false)) {
				// If there is a picture available, do nothing, as the
				// ImageButton is already visible
				;
			} else {
				// set ImageButton invisible
				deletePicture.setVisibility(ImageButton.GONE);
			}

			deletePicture.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					deletePictureAndThumbnail(cardBackPic);

					Toast toast = Toast.makeText(getApplicationContext(),
							"Picture has been deleted successfully",
							Toast.LENGTH_LONG);
					toast.show();

					/*
					 * // Save changes in card and in DB
					 * Edit.getInstance().deletePicFromCard(false, card);
					 */

					cardBackPic = "";
					newBackPictureTaken = false;

					// Set delete ImageButton gone as there is no pic to delete
					// anymore
					deletePicture.setVisibility(ImageButton.GONE);

					// Update ImageButton
					updateImageButtonAdminEdit(false, showPictureButton);

				}
			});

			return v;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {

			switch (requestCode) {
			case TAKE_PICTURE:
				// Gets called, when a picture is taken
				if (resultCode == Activity.RESULT_OK) {

					// If there is any picture that has been taken in edit mode
					// before
					if (newBackPictureTaken) {
						deletePictureAndThumbnail(cardBackPic);
					}

					// Save Path in local variable
					cardBackPic = imageUri.getPath();
					newBackPictureTaken = true;

					// Create Thumbnail
					try {
						createThumbnail(cardBackPic);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					// Update the Thumbnail on the back of the card
					updateImageButtonAdminEdit(false, showPictureButton);

					Toast.makeText(getApplicationContext(),
							"New picture was taken successfully",
							Toast.LENGTH_LONG).show();

					// Set delete ImageButton visible as there is any picture to
					// delete now
					deletePicture.setVisibility(ImageButton.VISIBLE);
				}
				break;
			case SELECT_PHOTO:

				if (resultCode == RESULT_OK) {

					// Create file with existing picture
					Uri selectedImage = data.getData();
					File picture = new File(getRealPathFromURI(selectedImage));

					// If there is any picture that has been taken in edit mode
					// before
					if (newBackPictureTaken) {
						deletePictureAndThumbnail(cardBackPic);
					}

					File destination = new File(
							Environment.getExternalStorageDirectory()
									+ "/knowItOwl/pictures", createPicName()
									+ ".jpg");

					try {
						copyFile(picture, destination);
					} catch (IOException e) {
						// display a general error if the file could not be
						// copied
						/*
						 * ErrorHandlerFragment newFragment =
						 * ErrorHandlerFragment
						 * .newInstance(R.string.error_handler_general,
						 * ErrorHandlerFragment.GENERAL_ERROR);
						 * newFragment.show(
						 * ErrorHandlerFragment.applicationContext., "dialog");
						 */
					}

					cardBackPic = destination.getPath();
					newBackPictureTaken = true;

					// Create thumbnail
					try {
						createThumbnail(cardBackPic);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					// Updates the ImageButton to show the new picture as a
					// thumbnail
					updateImageButtonAdminEdit(false, showPictureButton);

					Toast.makeText(getApplicationContext(),
							"Set new picture successfully", Toast.LENGTH_LONG)
							.show();

					// Set delete ImageButton visible as there is a new pic
					// which can be deleted now
					deletePicture.setVisibility(ImageButton.VISIBLE);
				}
				break;
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
			Toast.makeText(getApplicationContext(),
					"Cannot create Card, front text is empty!",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (cardBack.getText() == null
				|| cardBack.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(),
					"Cannot create Card, back text is empty!",
					Toast.LENGTH_LONG).show();
			return false;
		} else
			return true;
	}

	public boolean updateImageButtonAdminEdit(boolean front,
			ImageButton pictureBtn) {
		final int THUMBNAIL_SIZE = 128;

		// Check if ImageButton on front (true) or back (false) is to be updated
		if (front) {
			// Check if there is any picture available
			if (!cardFrontPic.equals("") && checkPictureAvailability(true)) {

				// Get name of thumbnail from path of picture
				File picture = new File(cardFrontPic);
				String pictureName = picture.getName();

				// Set up file input stream
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(new File(
							Environment.getExternalStorageDirectory()
									+ "/knowItOwl/thumbnails", pictureName));
				} catch (FileNotFoundException e) {
					// display a file not found error if the file is not found
					ErrorHandlerFragment newFragment = ErrorHandlerFragment
							.newInstance(R.string.error_handler_file_not_found,
									ErrorHandlerFragment.FILE_NOT_FOUND);
					newFragment.show(this.getFragmentManager(), "dialog");
					e.printStackTrace();
				}

				// Create Bitmap --> Thumbnail
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

				// Set visibility true and set thumbnail
				pictureBtn.setVisibility(ImageButton.VISIBLE);
				pictureBtn.setImageBitmap(imageBitmap);

				// if there is no front picture available
			} else {
				// Set visibility false
				pictureBtn.setVisibility(ImageButton.GONE);
			}

			// If back picture is to be updated
		} else {

			// If card back picture is available
			if (!cardBackPic.equals("") && checkPictureAvailability(false)) {

				// Get name of thumbnail from path of picture
				File picture = new File(cardBackPic);
				String pictureName = picture.getName();

				// set up file input stream
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(new File(
							Environment.getExternalStorageDirectory()
									+ "/knowItOwl/thumbnails", pictureName));
				} catch (FileNotFoundException e) {
					// display a file not found error if the file is not found
					ErrorHandlerFragment newFragment = ErrorHandlerFragment
							.newInstance(R.string.error_handler_file_not_found,
									ErrorHandlerFragment.FILE_NOT_FOUND);
					newFragment.show(this.getFragmentManager(), "dialog");
					e.printStackTrace();
				}

				// Create bitmap --> thumbnail
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

				// set image ImageButton visible and set new thumbnail
				pictureBtn.setVisibility(ImageButton.VISIBLE);
				pictureBtn.setImageBitmap(imageBitmap);

				// if there is no picture available
			} else {

				// set image ImageButton gone
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
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpEditCardsStackScreen.class));
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
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
				Edit.getInstance().changeCard(front, back, cardFrontPic,
						cardBackPic, cardTagList, card);
				finish();
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

	/**
	 * Copys the given source file to the destination
	 * 
	 * @param src
	 *            source file
	 * @param dst
	 *            destination folder
	 * @throws IOException
	 */
	public static void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	/**
	 * Returns the real path from Uri
	 * 
	 * @param contentUri
	 * @return
	 */
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * Checks if there is any file /knowItOwl/pictures existing if not -->
	 * create it
	 * 
	 */
	public void checkFileAvailabilityPictures() {
		// Check if there is file
		// /knowItOwl/pictures, if not -->
		// create it
		if (!new File(Environment.getExternalStorageDirectory().getPath()
				+ "/knowItOwl/pictures").exists()) {
			new File(Environment.getExternalStorageDirectory().getPath()
					+ "/knowItOwl/pictures").mkdir();
		}
	}

	/**
	 * Checks if there is any file /knowItOwl/thumbnails existing if not -->
	 * create it
	 * 
	 */
	public void checkFileAvailabilityThumbnails() {
		// Check if there is file
		// /knowItOwl/pictures, if not -->
		// create it
		if (!new File(Environment.getExternalStorageDirectory().getPath()
				+ "/knowItOwl/thumbnails").exists()) {
			new File(Environment.getExternalStorageDirectory().getPath()
					+ "/knowItOwl/thumbnails").mkdir();
		}
	}

	/**
	 * Creates thumbnail of new picture and saves it in "/knowitowl/thumbnails"
	 * 
	 * @param picPath
	 *            of the new picture
	 * @throws FileNotFoundException
	 */
	private void createThumbnail(String picPath) throws FileNotFoundException {

		final int THUMBNAIL_SIZE = 100;


		// Check if the thumbnail file has already been created - if not -->
		// create it!
		checkFileAvailabilityThumbnails();

		Log.e("AdminNewCard", "Thumbnail erstellen erreicht");

		// Get name of selected picture
		File picture = new File(picPath);
		String picName = picture.getName();

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(picPath));
		} catch (FileNotFoundException e) {
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_file_not_found,
							ErrorHandlerFragment.FILE_NOT_FOUND);
			newFragment.show(this.getFragmentManager(), "dialog");
		}

		// new bitmap (thumbnail)
		Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

		// Resize picture to thumbnail size
		imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE,
				THUMBNAIL_SIZE, false);

		File destination = new File(Environment.getExternalStorageDirectory()
				+ "/knowItOwl/thumbnails", picName);

		// destination.createNewFile();

		// write the bytes in file
		FileOutputStream fo = new FileOutputStream(destination);
		imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fo);

		// close FileOutput
		try {
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes Picture and Thumbnail of that picture
	 * 
	 * @param picPath
	 *            : Path where the picture that is to be deleted is stored
	 */
	private void deletePictureAndThumbnail(String picPath) {

		// Create file of picture that is to be deleted
		File pictureToDelete = new File(picPath);

		// Get Name of picture with help of the file, before this one is deleted
		String picName = pictureToDelete.getName();

		// Delete picture
		pictureToDelete.delete();

		// Create file of thumbnail that is to be deleted
		File thumbnailToDelete = new File(
				Environment.getExternalStorageDirectory()
						+ "/knowItOwl/thumbnails", picName);

		// Delete thumbnail
		thumbnailToDelete.delete();
	}

	/**
	 * Creates unique picture name with help of the actual date
	 * 
	 * @return String containing the new picture name
	 */
	private String createPicName() {
		// Create a unique PicName with help of
		// the actual date
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
		String picName = sd.format(date);
		return picName;
	}

}