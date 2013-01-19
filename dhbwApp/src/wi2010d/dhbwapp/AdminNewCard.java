package wi2010d.dhbwapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	TextView cardFront;
	TextView cardBack;
	Context context;

	public static final int STACK_CHOSEN = 10;

	public static final int TAKE_PICTURE_FRONT = 1;
	private ImageButton takePictureFront;
	private ImageButton deletePictureFront;
	private ImageButton showPictureFront;
	public Uri imageUriFront;

	public static final int TAKE_PICTURE_BACK = 2;
	private ImageButton addMedia;
	private ImageButton deletePictureBack;
	private ImageButton showPictureBack;
	public Uri imageUriBack;
	
	public static final int SELECT_PHOTO_FRONT = 100;
	public static final int SELECT_PHOTO_BACK = 200;

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
		context = this;
		
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
		mViewPager.setOffscreenPageLimit (3);
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

			cardFront = (TextView) v.findViewById(R.id.txt_new_card_front);

			cardFront.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// create dialog to insert name of new stack
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle("Text Input");

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

			// Set up ImageButton to take new picture
			takePictureFront = (ImageButton) v
					.findViewById(R.id.btn_admin_new_card_picture_front);
			takePictureFront.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {

					final CharSequence[] items = { "Take Picture",
							"Picture from Gallery", "Add Hyperlink" };

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							v.getContext());
					builder.setTitle("Mediacenter");
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									switch (item) {
									case 0:
										// New intent to take picture
										Intent takePicture = new Intent(
												"android.media.action.IMAGE_CAPTURE");

										
										checkFileAvailabilityPictures();

										// New file for the picture
										File photo = new File(Environment
												.getExternalStorageDirectory()
												+ "/knowItOwl/pictures",
												createPicName() + ".jpg");
										takePicture.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(photo));
										imageUriFront = Uri.fromFile(photo);

										// Start activity
										startActivityForResult(takePicture,
												TAKE_PICTURE_FRONT);

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
												photoPickerIntent, SELECT_PHOTO_FRONT);
										
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
																	.setAutoLinkMask(Linkify.WEB_URLS);
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

					deletePictureAndThumbnail(cardFrontPic);
					
					Toast toast = Toast.makeText(getApplicationContext(),
								"Picture has been deleted successfully",
								Toast.LENGTH_LONG);
					toast.show();
					

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

			cardBack = (TextView) v.findViewById(R.id.txt_new_card_back);

			cardBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// create dialog to insert name of new stack
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					alert.setTitle("Text Input");

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

			addMedia = (ImageButton) v
					.findViewById(R.id.btn_admin_new_card_picture_back);
			addMedia.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {

					final CharSequence[] items = { "Take Picture",
							"Picture from Gallery", "Add Hyperlink" };

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							v.getContext());
					builder.setTitle("Mediacenter");
					builder.setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									switch (item) {
									case 0:

										// New intent to take picture
										Intent takePicture = new Intent(
												"android.media.action.IMAGE_CAPTURE");

										
										checkFileAvailabilityPictures();

										// Create new file
										File photo = new File(Environment
												.getExternalStorageDirectory()
												+ "/knowItOwl/pictures",
												createPicName() + ".jpg");
										takePicture.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(photo));
										imageUriBack = Uri.fromFile(photo);

										// Start activity and take picture
										startActivityForResult(takePicture,
												TAKE_PICTURE_BACK);
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
												photoPickerIntent, SELECT_PHOTO_BACK);
										
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
															String linkPathBack = editLinkPath
																	.getText()
																	.toString();
															// save text before
															// adding hyperlink
															cardBack.setAutoLinkMask(Linkify.WEB_URLS);
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

					deletePictureAndThumbnail(cardBackPic);

					Toast toast = Toast.makeText(getApplicationContext(),
								"Picture has been deleted successfully",
								Toast.LENGTH_LONG);
					toast.show();
					
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String stackName;
		Toast toast;

		Log.e("AdminNewCard", "Request Code: " + requestCode);
		
		switch (requestCode) {
		case STACK_CHOSEN:
			if (resultCode == STACK_CHOSEN) {
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
			}
		case 65537:

			// If picture is taken (front)
			if (resultCode == RESULT_OK) {

				// If there is any old picture for this card available
				if (!cardFrontPic.equals("") && checkPictureAvailability(true)) {
					deletePictureAndThumbnail(cardFrontPic);
				}

				// Save path in cardFrontPic
				cardFrontPic = imageUriFront.getPath();


				Toast.makeText(getApplicationContext(),
						"Picture saved under: " + imageUriFront.getPath(),
						Toast.LENGTH_LONG).show();
				
				// Create and save thumbnail
				try {
					createThumbnail(cardFrontPic);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Update Buttons
				updateImageButtonNewCard(true, showPictureFront);
				deletePictureFront.setVisibility(ImageButton.VISIBLE);
				
				break;
			}
			break;
		case 131074:
			// If picture is taken (back)
			if (resultCode == RESULT_OK) {

				// if there is any old picture for this card available
				if (!cardBackPic.equals("") && checkPictureAvailability(false)) {
					deletePictureAndThumbnail(cardBackPic);
				}

				// Save path in cardBackPic
				cardBackPic = imageUriBack.getPath();

				Toast.makeText(getApplicationContext(),
						"Picture saved under: " + imageUriBack.getPath(),
						Toast.LENGTH_LONG).show();
				
				// create and save thumbnail
				try {
					createThumbnail(cardBackPic);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Update ImageButtons
				updateImageButtonNewCard(false, showPictureBack);
				deletePictureBack.setVisibility(ImageButton.VISIBLE);

				
				break;
			}
			break;
		case 65636:
			
			if (resultCode == RESULT_OK) {

				// Create file with existing picture
				Uri selectedImage = data.getData();
				File picture = new File(getRealPathFromURI(selectedImage));

				// Delete former picture if available
				if (!cardFrontPic.equals("")) {
					deletePictureAndThumbnail(cardFrontPic);
				}


				File destination = new File(
						Environment.getExternalStorageDirectory()
								+ "/knowItOwl/pictures", createPicName() + ".jpg");

				try {
					copyFile(picture, destination);
				} catch (IOException e) {
					// display a general error if the file could not be
					// copied
				}

				// Save path of picture in local variable
				cardFrontPic = destination.getPath();

				// Create and save thumbnail of picture
				try {
					createThumbnail(cardFrontPic);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Updates the ImageButton to show the new picture as a
				// thumbnail
				updateImageButtonNewCard(true, showPictureFront);

				Toast.makeText(getApplicationContext(),
						"Set new picture successfully", Toast.LENGTH_LONG)
						.show();

				// Set delete ImageButton visible as there is a new pic which can be
				// deleted now
				deletePictureFront.setVisibility(ImageButton.VISIBLE);
			}
			break;
		case 131272:
			if (resultCode == RESULT_OK) {

				// Create file with existing picture
				Uri selectedImage = data.getData();
				File picture = new File(getRealPathFromURI(selectedImage));

				// Delete former picture if available
				if (!cardBackPic.equals("")) {
					deletePictureAndThumbnail(cardBackPic);
				}

				File destination = new File(
						Environment.getExternalStorageDirectory()
								+ "/knowItOwl/pictures", createPicName() + ".jpg");

				try {
					copyFile(picture, destination);
				} catch (IOException e) {
					// display a general error if the file could not be
					// copied
				}

				// Save path of picture in local variable
				cardBackPic = destination.getPath();

				// Create and save thumbnail of picture
				try {
					createThumbnail(cardBackPic);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Updates the ImageButton to show the new picture as a
				// thumbnail
				updateImageButtonNewCard(false, showPictureBack);

				Toast.makeText(getApplicationContext(),
						"Set new picture successfully", Toast.LENGTH_LONG)
						.show();

				// Set delete ImageButton visible as there is a new pic which can be
				// deleted now
				deletePictureFront.setVisibility(ImageButton.VISIBLE);
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

		// if the ImageButton on front is to be updated
		if (front) {

			// check if there is any existing front picture
			if (!cardFrontPic.equals("") && checkPictureAvailability(true)) {

				// Get name of thumbnail from path of picture
				File picture = new File(cardFrontPic);
				String pictureName = picture.getName();
				
				FileInputStream fis = null;
				
				try {
					fis = new FileInputStream(new File(Environment
							.getExternalStorageDirectory()
							+ "/knowItOwl/thumbnails",
							pictureName));
				} catch (FileNotFoundException e) {
					ErrorHandlerFragment newFragment = ErrorHandlerFragment
							.newInstance(R.string.error_handler_file_not_found,
									ErrorHandlerFragment.FILE_NOT_FOUND);
					newFragment.show(this.getFragmentManager(), "dialog");
				}
				
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

				// Update ImageButton
				pictureBtn.setVisibility(ImageButton.VISIBLE);
				pictureBtn.setImageBitmap(imageBitmap);

				// if there is no front picture available
			} else {
				pictureBtn.setVisibility(ImageButton.GONE);
			}
			// If the imageButton on back is to be updated
		} else {
			
			// check if there is any existing front picture
			if (!cardBackPic.equals("") && checkPictureAvailability(false)) {

				// Get name of thumbnail from path of picture
				File picture = new File(cardBackPic);
				String pictureName = picture.getName();
				
				FileInputStream fis = null;
				
				try {
					fis = new FileInputStream(new File(Environment
							.getExternalStorageDirectory()
							+ "/knowItOwl/thumbnails",
							pictureName));
				} catch (FileNotFoundException e) {
					ErrorHandlerFragment newFragment = ErrorHandlerFragment
							.newInstance(R.string.error_handler_file_not_found,
									ErrorHandlerFragment.FILE_NOT_FOUND);
					newFragment.show(this.getFragmentManager(), "dialog");
				}
				
				Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

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
		ArrayAdapter<String> lvAdapter;
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
									Toast.makeText(getApplicationContext(),
											"Please insert a stack name!",
											Toast.LENGTH_SHORT).show();
								} else if (input.getText().toString()
										.equals("All Stacks")) {
									Toast.makeText(
											getApplicationContext(),
											"Stack name cannot be 'All Stacks', please select another one!",
											Toast.LENGTH_LONG).show();

								} else {
									String stackName = input.getText()
											.toString();
									if (Create.getInstance().newStack(
											stackName, card)) {
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

		case R.id.btn_admin_new_card_existing_stack: {
			if (Stack.allStacks.size() == 0) {
				Toast.makeText(getApplicationContext(), "No Stacks available",
						Toast.LENGTH_LONG).show();
				break;
			} else if (isCardNotEmpty()) {
				boolean stackAvailable = false;
				final Dialog builder = new Dialog(this);
				LayoutInflater li = (LayoutInflater) this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = li.inflate(R.layout.admin_choose_stack_screen, null,
						false);
				ListView lv = (ListView) v.findViewById(R.id.admin_stack_list);
				ArrayList<String> items = new ArrayList<String>();
				for (Stack stack : Stack.allStacks) {
					if (stack.isDynamicGenerated()) {
						if (stack.getStackName().startsWith("<Dyn>")) {
							items.add(stack.getStackName());
							stackAvailable = true;
						} else {
							items.add("<Dyn> " + stack.getStackName());
							stackAvailable = true;
						}
					} else {
						items.add(stack.getStackName());
						stackAvailable = true;
					}
				}
				if (items.size() == 0) {
					items.add("No stacks available");
					stackAvailable = false;
				}

				Collections.sort(items);
				lvAdapter = new ArrayAdapter<String>(this,
						R.layout.layout_listitem, items);
				lv.setClickable(true);
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						String stackName = ((TextView) v).getText().toString();

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
						}

						for (Stack stack : Stack.allStacks) {
							if (stack.getStackName().equals(stackName)) {
								Edit.getInstance().addCardToStack(stack, card);
								Toast.makeText(getApplicationContext(),
										"Card added to " + stackName,
										Toast.LENGTH_LONG).show();
								builder.dismiss();
								break;
							}
						}

					}
				});
				lv.setAdapter(lvAdapter);
				builder.setTitle("Choose Stack");
				builder.setContentView(v);
				builder.show();
				break;
			}
		}
		default:
			return false;
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
		// /knowItOwl/thumbnails, if not -->
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
	 * @param picPath of the new picture
	 * @throws FileNotFoundException
	 */
	private void createThumbnail(String picPath) throws FileNotFoundException {

		final int THUMBNAIL_SIZE = 128;

		// Check if the thumbnail file has already been created - if not --> create it!
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
		
		//destination.createNewFile();
		
		
		 //write the bytes in file 
		 FileOutputStream fo = new FileOutputStream(destination);
		 imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fo);
		  
		 // close FileOutput 
		 try {
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes Picture and Thumbnail of that picture
	 * 
	 * @param picPath: Path where the picture that is to be deleted is stored
	 */
	private void deletePictureAndThumbnail(String picPath){
		
		// Create file of picture that is to be deleted
		File pictureToDelete = new File(picPath);
		
		// Get Name of picture with help of the file, before this one is deleted
		String picName = pictureToDelete.getName();
		
		// Delete picture
		pictureToDelete.delete();
		
		// Create file of thumbnail that is to be deleted
		File thumbnailToDelete = new File (Environment.getExternalStorageDirectory()
				+ "/knowItOwl/thumbnails", picName);
		
		// Delete thumbnail
		thumbnailToDelete.delete();
	}
	
	/**
	 * Creates unique picture name with help of the actual date
	 * 
	 * @return String containing the new picture name
	 */
	private String createPicName(){
		// Create a unique PicName with help of
		// the actual date
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
		String picName = sd.format(date);
		return picName;
	}
	

}
