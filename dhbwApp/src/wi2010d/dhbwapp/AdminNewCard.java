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
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.ActionBar;
import android.app.Activity;
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
import android.support.v4.app.FragmentActivity;
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

public class AdminNewCard extends OnResumeFragmentActivity implements
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
				// find the checked Tags from the list
				cardTagList.clear();
				for (Tag tag : Tag.allTags) {
					if (tag.isChecked()) {
						cardTagList.add(tag);
					}
				}

				card = Create.getInstance().newCard(
						cardFront.getText().toString(),
						cardBack.getText().toString(), cardTagList, "", "");

				//create dialog to insert name of new stack
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
								//do sth
								Toast toast;
								if (input.getText().toString().equals("")) {
									toast = Toast.makeText(getApplicationContext(),
											"Please insert a stack name!", Toast.LENGTH_SHORT);
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
									String stackName = input.getText().toString();
									Create.getInstance().newStack(stackName, card);
									
									toast = Toast.makeText(getApplicationContext(), "Stack " + stackName
											+ " created and Card added", Toast.LENGTH_LONG);
									toast.show();
									finish();
									//finish();
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
						cardBack.getText().toString(), cardTagList, "", "");

				Intent i = new Intent(getApplicationContext(),
						AdminNewCardChooseStack.class);
				startActivityForResult(i, STACK_CHOSEN);
			}
		default:
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

	public class NewCardFront extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private Button takePictureFront;
		private ImageButton showPictureFront;
		public Uri imageUriFront;
		public static final int TAKE_PICTURE = 1;
		
		public NewCardFront() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			View v = inflater.inflate(R.layout.admin_new_card_front, null);

			cardFront = (EditText) v.findViewById(R.id.txt_new_card_front);
			
			takePictureFront = (Button) v.findViewById(R.id.btn_admin_new_card_picture_front);
			takePictureFront.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent takePicture = new Intent("android.media.action.IMAGE_CAPTURE");
					Date date = new Date();
					SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
					String picName = sd.format(date);
					
					if (!new File(Environment.getExternalStorageDirectory().getPath()
							+ "/knowItOwl/pictures").exists()) {
						new File(Environment.getExternalStorageDirectory().getPath()
								+ "/knowItOwl/pictures").mkdir();
					}
					
				    File photo = new File(Environment.getExternalStorageDirectory() 
				    		+ "/knowItOwl/pictures",  picName + ".jpg");
				    takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
				            Uri.fromFile(photo));
				    imageUriFront = Uri.fromFile(photo);

				    startActivityForResult(takePicture, TAKE_PICTURE);
					
				}
			});
			
			showPictureFront = (ImageButton) v.findViewById(R.id.btn_admin_new_card_picture_front_show);
			
			
			showPictureFront.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!card.getCardFrontPicture().equals("")){						
						Intent show = new Intent();
						show.setAction(Intent.ACTION_VIEW);
						show.setDataAndType(Uri.fromFile(new File(card.getCardFrontPicture())), "image/*");
						startActivity(show);
					}
				}
			});

			return v;
		}
		
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	
		       if (resultCode == Activity.RESULT_OK) {
				    
		    	   //card.setCardFrontPicture(imageUriFront.getPath());
		    	   Edit.getInstance().addNewPicToCard(true, imageUriFront.getPath(), card);
				   updateImageButtonNewCard(true, showPictureFront);
					Toast toast;
					toast = Toast.makeText(getApplicationContext(),
							"Picture saved under: " +  imageUriFront.getPath(), Toast.LENGTH_LONG);
					toast.show();
		        }
		  }
	}

	public class NewCardBack extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private Button takePictureBack;
		private ImageButton showPictureBack;
		public Uri imageUriBack;
		public static final int TAKE_PICTURE = 1;

		public NewCardBack() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View v = inflater.inflate(R.layout.admin_new_card_back, null);

			cardBack = (EditText) v.findViewById(R.id.txt_new_card_back);
			
			takePictureBack = (Button) v.findViewById(R.id.btn_admin_new_card_picture_back);
			takePictureBack.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent takePicture = new Intent("android.media.action.IMAGE_CAPTURE");
					Date date = new Date();
					SimpleDateFormat sd = new SimpleDateFormat("yyMMddhhmmss");
					String picName = sd.format(date);
					
					if (!new File(Environment.getExternalStorageDirectory().getPath()
							+ "/knowItOwl/pictures").exists()) {
						new File(Environment.getExternalStorageDirectory().getPath()
								+ "/knowItOwl/pictures").mkdir();
					}
					
				    File photo = new File(Environment.getExternalStorageDirectory() 
				    		+ "/knowItOwl/pictures",  picName + ".jpg");
				    takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
				            Uri.fromFile(photo));
				    imageUriBack = Uri.fromFile(photo);

				    startActivityForResult(takePicture, TAKE_PICTURE);
					
				}
			});
			
			showPictureBack = (ImageButton) v.findViewById(R.id.btn_admin_new_card_picture_back_show);
			
			
			showPictureBack.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!card.getCardBackPicture().equals("")){						
						Intent show = new Intent();
						show.setAction(Intent.ACTION_VIEW);
						show.setDataAndType(Uri.fromFile(new File(card.getCardBackPicture())), "image/*");
						startActivity(show);
					}
				}
			});


			return v;
		}
		
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	
		       if (resultCode == Activity.RESULT_OK) {
				    
		    	   //card.setCardFrontPicture(imageUriBack.getPath());
		    	  
		    	   Edit.getInstance().addNewPicToCard(false, imageUriBack.getPath(), card);
				   
		    	   updateImageButtonNewCard(false, showPictureBack);  
		    	  
				    
					Toast toast;
					toast = Toast.makeText(getApplicationContext(),
							"Picture saved under: " +  imageUriBack.getPath(), Toast.LENGTH_LONG);
					toast.show();
		        }
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

					Edit.getInstance().addCardToStack(stack, card);
					
					toast = Toast.makeText(this, "Card added to stack "
							+ stackName, Toast.LENGTH_SHORT);
					toast.show();
					finish();
					break;
				}
			}
			break;
		default:
			break;
		}
	}
	
	//TODO: Gucken, ob hier auch dieselbe Methode aus der Learner-Klasse aufgerufen werden kann
		public boolean updateImageButtonNewCard(boolean front, ImageButton pictureBtn){
			final int THUMBNAIL_SIZE = 64;
			
			if (front){
				if (!card.getCardFrontPicture().equals("")){
		            FileInputStream fis = null;
					try {
						fis = new FileInputStream(new File(card.getCardFrontPicture()));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
		
		            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 
		            		THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
		
		            
		            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		            byte[] byteArray = baos.toByteArray();
		            
		
					pictureBtn.setImageBitmap(imageBitmap);
				}else{
					pictureBtn.setVisibility(ImageButton.GONE);
				}}
			else{
				if (!card.getCardBackPicture().equals("")){
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(new File(card.getCardBackPicture()));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
		
		            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 
		            		THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
		
		            
		            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		            byte[] byteArray = baos.toByteArray();
		            
		
					pictureBtn.setImageBitmap(imageBitmap);
				}else{
					pictureBtn.setVisibility(ImageButton.GONE);
				}}
				
			return true;
		}
}
