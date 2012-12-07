package wi2010d.dhbwapp.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

	private static final String DB_NAME = "knowitowl.db";
	private static final int DB_VERSION = 1;

	//Create Table SQL Statements
	private static final String STACK_CREATE = "CREATE TABLE stack (_id INTEGER PRIMARY KEY, stackName VARCHAR(100) NOT NULL, isDynamicGenerated BOOLEAN, dontKnow INTEGER, notSure INTEGER, sure INTEGER);";
	private static final String CARD_CREATE = "CREATE TABLE card (_id INTEGER PRIMARY KEY,cardFront VARCHAR(100), cardBack VARCHAR(100),cardFrontPicture VARCHAR(100),cardBackPicture VARCHAR(100),drawer INTEGER);";
	private static final String RUNTHROUGH_CREATE = "CREATE TABLE runthrough (_id INTEGER PRIMARY KEY,stackID INTEGER NOT NULL,isOverall BOOLEAN,startDate DATE,endDate DATE,beforeDontKnow INTEGER,beforeNotSure INTEGER, beforeSure INTEGER,afterDontKnow INTEGER,afterNotSure INTEGER,afterSure INTEGER);";
	private static final String TAG_CREATE = "CREATE TABLE tag (_id INTEGER PRIMARY KEY,tagName VARCHAR(100),totalCards INTEGER);";
	private static final String STACK_CARD_CREATE = "CREATE TABLE stackcard (stackID INTEGER,cardID INTEGER,_id INTEGER PRIMARY KEY);";
	private static final String CARD_TAG_CREATE = "CREATE TABLE cardtag (cardID INTEGER,tagID INTEGER,_id INTEGER PRIMARY KEY);";

	private static final String DROP_TABLES="DROP TABLE IF EXISTS stack,card,runthrough,tag,stackcard,cardtag";
	
	public DatabaseManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		database.execSQL(STACK_CREATE);
		database.execSQL(CARD_CREATE);
		database.execSQL(RUNTHROUGH_CREATE);
		database.execSQL(TAG_CREATE);
		database.execSQL(STACK_CARD_CREATE);
		database.execSQL(CARD_TAG_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

		Log.w(DatabaseManager.class.getName(), "Upgrading Database from version "+ oldVersion + "to new Version "+newVersion);
		database.execSQL(DROP_TABLES);
		onCreate(database);
	}

}
