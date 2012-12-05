package wi2010d.dhbwapp.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
	
	private static final String DB_NAME="sirlearnalot.db";
	private static final int DB_VERSION= 1;
	private static final String STACK_CREATE=
			"CREATE TABLE stack ("+
			"stackID INTEGER,"+
			"stackName STRING NOT_NULL,"+
			"isDynamicGenerated BOOLEAN," +
			"PRIMARY KEY(stackID));";
	private static final String CARD_CREATE=
			"CREATE TABLE card ("+
			"cardID INTEGER,"+
			"cardName STRING NOT_NULL,"+
			"cardFront STRING,"+
			"cardFrontPicture STRING,"+
			"cardBack STRING,"+
			"cardBackPicture STRING,"+
			"drawer INTEGER,"+
			"tags STRING,"+
			"PRIMARY KEY(cardID));";
	private static final String RUNTHROUGH_CREATE=
			"CREATE TABLE runthrough ("+
			"runthroughID INTEGER,"+
			"isOverall BOOLEAN,"+
			"startDate DATE,"+
			"endDate DATE,"+
			"beforeSure INTEGER,"+
			"beforeNotSure INTEGER,"+
			"beforeDontKnow INTEGER,"+
			"afterSure INTEGER,"+
			"afterNotSure INTEGER,"+
			"afterDontKnow INTEGER,"+
			"PRIMARY KEY(runthroughID));";
	

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
