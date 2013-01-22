package wi2010d.dhbwapp.control;

import java.util.ArrayList;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Holds and manages the current SQLite Database Methods for creating, deleting
 * and reseting the DB are implemented
 * 
 */
public class DatabaseManager extends SQLiteOpenHelper {

	private static final String DB_NAME = "knowitowl.db";
	private static final int DB_VERSION = 1;
	private static DatabaseManager databaseManager;

	// Create Table SQL Queries
	private static final String STACK_CREATE = "CREATE TABLE stack (_id INTEGER PRIMARY KEY, stackName VARCHAR(100) NOT NULL, isDynamicGenerated BOOLEAN, dontKnow INTEGER, notSure INTEGER, sure INTEGER);";
	private static final String CARD_CREATE = "CREATE TABLE card (_id INTEGER PRIMARY KEY,cardFront VARCHAR(100), cardBack VARCHAR(100),cardFrontPicture VARCHAR(100),cardBackPicture VARCHAR(100),drawer INTEGER,totalStacks INTEGER);";
	private static final String RUNTHROUGH_CREATE = "CREATE TABLE runthrough (_id INTEGER PRIMARY KEY,stackID INTEGER NOT NULL,isOverall BOOLEAN,startDate LONG,endDate LONG,beforeDontKnow INTEGER,beforeNotSure INTEGER, beforeSure INTEGER,afterDontKnow INTEGER,afterNotSure INTEGER,afterSure INTEGER);";
	private static final String TAG_CREATE = "CREATE TABLE tag (_id INTEGER PRIMARY KEY,tagName VARCHAR(100),totalCards INTEGER);";
	private static final String STACK_CARD_CREATE = "CREATE TABLE stackcard (stackID INTEGER,cardID INTEGER,_id INTEGER PRIMARY KEY AUTOINCREMENT);";
	private static final String CARD_TAG_CREATE = "CREATE TABLE cardtag (cardID INTEGER,tagID INTEGER,_id INTEGER PRIMARY KEY AUTOINCREMENT);";
	private static final String STACK_TAG_CREATE = "CREATE TABLE stacktag (stackID INTEGER,tagID INTEGER,_id INTEGER PRIMARY KEY AUTOINCREMENT);";

	// Drop Table SQL Queries
	private static final String DROP_STACK = "DROP TABLE IF EXISTS 'stack';";
	private static final String DROP_CARD = "DROP TABLE IF EXISTS 'card';";
	private static final String DROP_RUNTHROUGH = "DROP TABLE IF EXISTS 'runthrough';";
	private static final String DROP_TAG = "DROP TABLE IF EXISTS 'tag';";
	private static final String DROP_STACK_CARD = "DROP TABLE IF EXISTS 'stackcard';";
	private static final String DROP_CARD_TAG = "DROP TABLE IF EXISTS 'cardtag';";
	private static final String DROP_STACK_TAG = "DROP TABLE IF EXISTS 'stacktag';";

	public DatabaseManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * Create the DB
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.d("Database Manager", "Create Database");

		database.execSQL(STACK_CREATE);
		database.execSQL(CARD_CREATE);
		database.execSQL(RUNTHROUGH_CREATE);
		database.execSQL(TAG_CREATE);
		database.execSQL(STACK_CARD_CREATE);
		database.execSQL(CARD_TAG_CREATE);
		database.execSQL(STACK_TAG_CREATE);

	}

	/**
	 * Upgrade the DB with a new version
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {

		Log.w(DatabaseManager.class.getName(),
				"Upgrading Database from version " + oldVersion
						+ "to new Version " + newVersion);
		database.execSQL(DROP_STACK);
		database.execSQL(DROP_CARD);
		database.execSQL(DROP_RUNTHROUGH);
		database.execSQL(DROP_TAG);
		database.execSQL(DROP_STACK_CARD);
		database.execSQL(DROP_STACK_TAG);
		database.execSQL(DROP_CARD_TAG);
		onCreate(database);
	}

	/**
	 * Delete the DB and create a new one
	 * 
	 * @return
	 */
	public boolean deleteDB() {

		resetVariables(); // reset all the objects

		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL(DROP_STACK);
		database.execSQL(DROP_CARD);
		database.execSQL(DROP_RUNTHROUGH);
		database.execSQL(DROP_TAG);
		database.execSQL(DROP_STACK_CARD);
		database.execSQL(DROP_STACK_TAG);
		database.execSQL(DROP_CARD_TAG);

		onCreate(database);
		return true;
	}

	/**
	 * Resets all the objects, so there are no duplicates when loading again
	 * 
	 * @return true, if it worked
	 */
	public boolean resetVariables() {

		for (Stack stack : Stack.allStacks) {
			stack = null;
		}
		for (Card card : Card.allCards) {
			card = null;
		}
		for (Tag tag : Tag.allTags) {
			tag = null;
		}
		for (Runthrough run : Runthrough.allRunthroughs) {
			run = null;
		}
		Stack.allStacks = new ArrayList<Stack>();
		Card.allCards = new ArrayList<Card>();
		Tag.allTags = new ArrayList<Tag>();
		Runthrough.allRunthroughs = new ArrayList<Runthrough>();

		Stack.resetLastStackID();
		Card.resetLastCardID();
		Tag.resetLastTagID();
		Runthrough.resetLastRunthroughID();
		
		Init.dataWritten = false;

		return true;

	}

	/**
	 * 'Singleton' method
	 * 
	 * @return The current object of DBManager
	 */
	public static DatabaseManager getInstance() {

		return databaseManager;
	}

	/**
	 * Singleton method for initialization
	 * 
	 * @param context
	 *            The applications' context
	 * @return The current object of DBManager
	 */
	public static DatabaseManager getInstance(Context context) {
		databaseManager = new DatabaseManager(context);
		return databaseManager;
	}

}
