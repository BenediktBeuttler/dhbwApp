package wi2010d.dhbwapp.control;

import java.util.Date;

import wi2010d.dhbwapp.AdminImportExport;
import wi2010d.dhbwapp.R;
import wi2010d.dhbwapp.StartScreen;
import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Reads all the data from the DB and initializes the objects. While doing that,
 * the loading screen is shown, afterwards the MainScreen is shown.
 */
public class Init extends AsyncTask<Void, Void, Boolean> {

	private Database database;
	private static Init init;
	public static boolean runComplete = false;
	private Activity startScreenActivity;

	public Init(Context context, Activity activity) {
		DatabaseManager.getInstance(context);
		this.database = Database.getInstance();
		this.startScreenActivity = activity;
	}

	/**
	 * Show the loading screen while executing the task
	 */
	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		startScreenActivity.setContentView(R.layout.progress_screen);

		ProgressBar pb = (ProgressBar) startScreenActivity
				.findViewById(R.id.progress_bar_progress_screen);
	}

	/**
	 * When finished, show the start screen
	 */
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		new ErrorHandler(startScreenActivity.getApplicationContext());

		if (startScreenActivity.getIntent().getData() != null
				&& startScreenActivity.getIntent().getData().getPath() != null) {
			String path = startScreenActivity.getIntent().getData().getPath();
			Intent intent = new Intent(
					startScreenActivity.getApplicationContext(),
					AdminImportExport.class);
			intent.putExtra("Path", path);
			if(startScreenActivity.getIntent().getScheme().equals("content")){
				intent.putExtra("Data", startScreenActivity.getIntent().getData());
			}
			else{
				intent.putExtra("Data", "");
			}
			startScreenActivity.startActivity(intent);
			startScreenActivity.finish();
		} else {
			Intent i = new Intent(startScreenActivity.getApplicationContext(),
					StartScreen.class);
			startScreenActivity.startActivity(i);
			startScreenActivity.finish();
		}
	}

	/**
	 * First, reset all the variables to prevent multiple objects, then
	 * initialize everything
	 */
	@Override
	protected Boolean doInBackground(Void... Params) {
		DatabaseManager.getInstance().resetVariables();
		return this.loadFromDB();
	}

	/**
	 * Starts all the loading tasks one after the other
	 * 
	 * @return true, if everything worked
	 */
	public boolean loadFromDB() {
		// load the objects from DB
		this.loadStacks();
		this.loadCards();
		this.loadTags();
		this.loadRunthroughs();

		// assign the objects
		this.assignCardsToStacks();
		this.assignTagsToCards();
		this.assignTagstoStacks();

		this.deleteUnusedTags();
		runComplete = true;
		return true;
	}

	/**
	 * Reads the stacks from the database and initializes them as java objects
	 * 
	 * @return
	 */
	private boolean loadStacks() {

		int id;
		String name;
		boolean isDynamicGenerated;
		int sure;
		int notSure;
		int dontKnow;

		Cursor cursor = database.queryStack(); // get stacks from db

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {

			id = cursor.getInt(0);
			name = cursor.getString(1);
			int isDynamicGeneratedInt = cursor.getInt(2);

			if (isDynamicGeneratedInt == 0) {
				isDynamicGenerated = false;
			} else {
				isDynamicGenerated = true;
			}
			dontKnow = cursor.getInt(3);
			notSure = cursor.getInt(4);
			sure = cursor.getInt(5);

			new Stack(isDynamicGenerated, id, name, sure, notSure, dontKnow);

			cursor.moveToNext();
		}
		cursor.close();
		Database.getInstance().close();
		return true;
	}

	/**
	 * Reads the tags from the database and initializes them as java objects
	 * 
	 * @return true, if it worked
	 */
	private boolean loadTags() {
		int id;
		int totalCards;
		String name;

		Cursor cursor = database.queryTag();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			id = cursor.getInt(0);
			name = cursor.getString(1);
			totalCards = cursor.getInt(2);
			new Tag(id, totalCards, name);
			cursor.moveToNext();
		}
		cursor.close();
		Database.getInstance().close();
		return true;

	}

	/**
	 * Reads the cards from the database and initializes them as java objects
	 * 
	 * @return true, if it worked
	 */
	private boolean loadCards() {
		int id;
		int drawer;
		int totalStacks;
		String front;
		String back;
		String frontPic;
		String backPic;

		Cursor cursor = database.queryCard();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			id = cursor.getInt(0);
			front = cursor.getString(1);
			back = cursor.getString(2);
			frontPic = cursor.getString(3);
			backPic = cursor.getString(4);
			drawer = cursor.getInt(5);
			totalStacks = cursor.getInt(6);

			new Card(id, drawer, totalStacks, front, back, frontPic, backPic);
			cursor.moveToNext();
		}
		cursor.close();
		Database.getInstance().close();
		return true;

	}

	/**
	 * Reads the runthroughs from the database and initializes them as java
	 * objects
	 * 
	 * @return
	 */
	private boolean loadRunthroughs() {

		int id;
		Date startDate;
		Date endDate;
		int sureBefore;
		int sureAfter;
		int dontKnowBefore;
		int dontKnowAfter;
		int notSureBefore;
		int notSureAfter;
		boolean isOverall;
		int stackID;

		Cursor cursor = database.queryRunthrough();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {

			id = cursor.getInt(0);
			stackID = cursor.getInt(1);
			int isOverallInt = cursor.getInt(2);

			if (isOverallInt == 0) {
				isOverall = false;
			} else {
				isOverall = true;
			}

			// dates are stored as long vars
			startDate = new Date(cursor.getLong(3));
			endDate = new Date(cursor.getLong(4));

			// read data for the arrays
			dontKnowBefore = cursor.getInt(5);
			notSureBefore = cursor.getInt(6);
			sureBefore = cursor.getInt(7);
			dontKnowAfter = cursor.getInt(8);
			notSureAfter = cursor.getInt(9);
			sureAfter = cursor.getInt(10);

			// initialize the arrays
			int[] statusBefore = { dontKnowBefore, notSureBefore, sureBefore };
			int[] statusAfter = { dontKnowAfter, notSureAfter, sureAfter };
			new Runthrough(id, stackID, isOverall, startDate, endDate,
					statusBefore, statusAfter);

			cursor.moveToNext();

		}
		cursor.close();
		Database.getInstance().close();
		return true;
	}

	/**
	 * Reads the Card Stack correlations from the DB and assigns them
	 * 
	 * @return true, if it worked
	 */
	private boolean assignCardsToStacks() {
		Cursor cursor = database.queryStackCard();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			int stackID = cursor.getInt(0);
			int cardID = cursor.getInt(1);

			for (Stack stack : Stack.allStacks) {
				if (stackID == stack.getStackID()) {
					for (Card card : Card.allCards) {
						if (cardID == card.getCardID()) {
							stack.getCards().add(card);
							break;
						}
					}
				}
			}
			cursor.moveToNext();
		}
		cursor.close();
		Database.getInstance().close();
		return true;
	}

	/**
	 * Reads the Tag Card correlations from the DB and assigns them
	 * 
	 * @return true, if it worked
	 */
	private boolean assignTagsToCards() {
		Cursor cursor = database.queryCardTag();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			int cardID = cursor.getInt(0);
			int tagID = cursor.getInt(1);
			for (Card card : Card.allCards) {
				if (cardID == card.getCardID()) {
					for (Tag tag : Tag.allTags) {
						if (tagID == tag.getTagID()) {
							card.getTags().add(tag);
							break;
						}
					}
				}
			}
			cursor.moveToNext();
		}
		cursor.close();
		Database.getInstance().close();
		return true;
	}

	/**
	 * Reads the Tag Stack correlations from the DB and assigns them
	 * 
	 * @return true, if it worked
	 */
	private boolean assignTagstoStacks() {
		Cursor cursor = database.queryStackTag();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			int stackID = cursor.getInt(0);
			int tagID = cursor.getInt(1);

			for (Stack stack : Stack.allStacks) {
				if (stack.isDynamicGenerated() && stackID == stack.getStackID()) {
					for (Tag tag : Tag.allTags) {
						if (tagID == tag.getTagID()) {
							stack.getDynamicStackTags().add(tag);
							break;
						}
					}
				}
			}
			cursor.moveToNext();
		}
		cursor.close();
		Database.getInstance().close();
		return true;

	}

	public boolean deleteUnusedTags() {
		for (Tag tag : Tag.allTags) {
			if (tag.getTotalCards() == 0) {
				Delete.getInstance().deleteTag(tag);
			}
		}
		return true;
	}

	/**
	 * Checks if one or more Variables got garbage collected
	 * 
	 * @return true, if one or more variable got garbage collected
	 */
	public static boolean isSthGarabageCollected() {
		// optimization: Sorted the requests from the big to little, because
		// bigger variables are collected more likely
		if (Card.allCards == null || Runthrough.allRunthroughs == null
				|| Stack.allStacks == null || Tag.allTags == null) {
			return true;
		}
		return false;
	}

	/**
	 * Resets the instance, so the task can be executed again
	 */
	public static void resetInstance() {
		init = null;
	}

	/**
	 * 'Singleton' method, returns the current object of Init
	 * 
	 * @param context
	 *            The Applications' context
	 * @param activity
	 *            The loading screen activity
	 * @return the current object of init
	 */
	public static Init getInstance(Context context, Activity activity) {
		if (init == null) {
			init = new Init(context, activity);
		}
		return init;

	}
}
