package wi2010d.dhbwapp.control;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Database {

	private SQLiteDatabase database;
	private DatabaseManager dbManager;
	private static Database db;

	// Query Strings
	private final String QUERY_STACK = "Select _id, stackName, isDynamicGenerated, dontKnow, notSure, sure from stack;";
	private final String QUERY_CARD = "Select _id, cardFront, cardBack, cardFrontPicture, cardBackPicture, drawer from card;";
	private final String QUERY_TAG = "Select _id, tagName, totalCards from tag;";
	private final String QUERY_RUNTHROUGH = "Select _id, stackID, isOverall, startDate, endDate, beforeDontKnow, beforeNotSure, beforeSure, afterDontKnow, afterNotSure, afterSure from runthrough;";
	private final String QUERY_STACK_CARD = "Select stackID, cardID from stackcard;";
	private final String QUERY_CARD_TAG = "Select cardID, tagID from cardtag;";
	private final String QUERY_STACK_TAG = "Select stackID, tagID from stacktag;";

	// Delete Strings
	private final String DELETE_STACK = "Delete from stack where _id = ?;";
	private final String DELETE_CARD = "Delete from card where _id = ?;";
	private final String DELETE_TAG = "Delete from tag where _id = ?;";
	private final String DELETE_RUNTHROUGH = "Delete from tag where _id = ?;";
	private final String DELETE_STACK_CARD = "Delete from stackcard where stackID = ? and cardID = ?";
	private final String DELETE_CARD_TAG = "Delete from cardtag where cardID = ? and stackID = ?";
	private final String DELETE_STACK_TAG = "Delete from stacktag where stackID = ? and tagID = ?";

	public Database() {
		this.dbManager = DatabaseManager.getInstance();
	}

	public void openWrite() throws SQLException {
		this.database = dbManager.getWritableDatabase();
	}

	public void openRead() throws SQLException {
		this.database = dbManager.getReadableDatabase();
	}

	public void close() {
		this.dbManager.close();
	}

	public Cursor queryTag() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_TAG, null);
		this.close();
		return cursor;
	}

	public Cursor queryStack() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_STACK, null);
		this.close();
		return cursor;
	}

	public Cursor queryRunthrough() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_RUNTHROUGH, null);
		this.close();
		return cursor;
	}

	public Cursor queryCard() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_CARD, null);
		this.close();
		return cursor;
	}

	public Cursor queryStackCard() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_STACK_CARD, null);
		this.close();
		return cursor;
	}

	public Cursor queryCardTag() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_CARD_TAG, null);
		this.close();
		return cursor;
	}

	public Cursor queryStackTag() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_STACK_TAG, null);
		this.close();
		return cursor;
	}

	public boolean deleteStack(Stack stack) {
		this.openWrite();

		database.rawQuery(DELETE_STACK,
				new String[] { "" + stack.getStackID() });

		this.close();
		return true;
	}
	
	public boolean deleteCard(Card card) {
		this.openWrite();

		database.rawQuery(DELETE_CARD,
				new String[] { "" + card.getCardID()});

		this.close();
		return true;
	}
	
	public boolean deleteTag(Tag tag) {
		this.openWrite();

		database.rawQuery(DELETE_TAG,
				new String[] { "" + tag.getTagID() });

		this.close();
		return true;
	}
	
	public boolean deleteStack(Runthrough run) {
		this.openWrite();

		database.rawQuery(DELETE_STACK,
				new String[] { "" + run });

		this.close();
		return true;
	}

	public static Database getInstance() {
		if (db == null) {
			db = new Database();
		}
		return db;
	}
}
