package wi2010d.dhbwapp.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
	
	private static final String DB_NAME="knowitowl.db";
	private static final int DB_VERSION= 1;
	private static final String STACK_CREATE=
			"CREATE TABLE stack (stackID INTEGER PRIMARY KEY, stackName VARCHAR(100) NOT NULL, isDynamicGenerated BOOLEAN);";
	
	private static final String CARD_CREATE=
			"CREATE TABLE card (cardID INTEGER PRIMARY KEY,cardName VARCHAR(100) NOT NULL,cardFront VARCHAR(100),cardFrontPicture VARCHAR(100),cardBack VARCHAR(100),cardBackPicture VARCHAR(100),drawer INTEGER);";
	
	private static final String RUNTHROUGH_CREATE=
			"CREATE TABLE runthrough (runthroughID INTEGER PRIMARY KEY,stackID INTEGER NOT NULL,isOverall BOOLEAN,startDate DATE,endDate DATE,beforeDontKnow INTEGER,beforeNotSure INTEGER, beforeSure INTEGER,afterDontKnow INTEGER,afterNotSure INTEGER,afterSure INTEGER);";
	
	private static final String TAG=
			"CREATE TABLE tag (tagID INTEGER PRIMARY KEY,tagName VARCHAR(100),totalCards INTEGER);";
	private static final String STACK_CARD=
			"CREATE TABLE STACKCARD (stackID INTEGER,cardID INTEGER,stackIDcardId INTEGER PRIMARY KEY);";
	private static final String CARD_TAG=
			"CREATE TABLE tag (cardID INTEGER,tagID INTEGER,cardIDtagID INTEGER PRIMARY KEY);";
	

	public DatabaseManager(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
