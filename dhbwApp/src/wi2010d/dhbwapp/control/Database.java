package wi2010d.dhbwapp.control;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Database {

	private SQLiteDatabase database;
	private DatabaseManager dbManager;
	
	//Query Strings
	private final String QUERY_TAG = "Select _id, tagName, totalCards from tag;";
	private final String QUERY_STACK = "Select _id, stackName, isDynamicGenerated, dontKnow, notSure, sure from stack;";
	private final String QUERY_RUNTHROUGH = "Select _id, stackID, isOverall, startDate, endDate, beforeDontKnow, beforeNotSure, beforeSure, afterDontKnow, afterNotSure, afterSure from runthrough;";
	private final String QUERY_CARD = "Select _id, cardFront, cardBack, cardFrontPicture, cardBackPicture, drawer from card;";
	private final String QUERY_STACK_CARD = "Select stackID, cardID from stackcard;";
	private final String QUERY_CARD_TAG = "Select cardID, tagID from cardtag;";
	
	public Database(Context context) {
		this.dbManager = new DatabaseManager(context);
	}

	public void openWrite() throws SQLException {
		this.database = dbManager.getWritableDatabase();
	}
	
	public void openRead() throws SQLException{
		this.database = dbManager.getReadableDatabase();
	}

	public void close() {
		this.dbManager.close();
	}
	
	public Cursor queryTag()
	{
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_TAG, null);
		this.close();
		return cursor;
	}
	
	public Cursor queryStack()
	{
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_STACK, null);
		this.close();
		return cursor;
	}
	
	public Cursor queryRunthrough()
	{
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_RUNTHROUGH, null);
		this.close();
		return cursor;
	}
	
	public Cursor queryCard()
	{
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_CARD, null);
		this.close();
		return cursor;
	}
	
	public Cursor queryStackCard()
	{
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_STACK_CARD, null);
		this.close();
		return cursor;
	}
	
	public Cursor queryCardTag()
	{
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_CARD_TAG, null);
		this.close();
		return cursor;
	}
	
	
	
	
	
	

}
