package wi2010d.dhbwapp.control;

import java.util.List;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Database {

	private SQLiteDatabase database;
	private DatabaseManager dbManager;
	private static Database db;

	// Query Strings
	private final String QUERY_STACK = "Select _id, stackName, isDynamicGenerated, dontKnow, notSure, sure from stack;";
	private final String QUERY_CARD = "Select _id, cardFront, cardBack, cardFrontPicture, cardBackPicture, drawer, totalStacks from card;";
	private final String QUERY_TAG = "Select _id, tagName, totalCards from tag;";
	private final String QUERY_RUNTHROUGH = "Select _id, stackID, isOverall, startDate, endDate, beforeDontKnow, beforeNotSure, beforeSure, afterDontKnow, afterNotSure, afterSure from runthrough;";
	private final String QUERY_STACK_CARD = "Select stackID, cardID from stackcard;";
	private final String QUERY_CARD_TAG = "Select cardID, tagID from cardtag;";
	private final String QUERY_STACK_TAG = "Select stackID, tagID from stacktag;";

	// Delete Strings
	// --- Delete queries for Stack and all references
	private final String DELETE_STACK = "Delete from stack where _id = ?;";
	private final String DELETE_STACK_CARD_STACK = "Delete from stackcard where stackID = ?;";
	private final String DELETE_STACK_TAG_STACK = "Delete from stacktag where stackID = ?;";

	// --- Delete queries for Card and all references
	private final String DELETE_CARD = "Delete from card where _id = ?;";
	private final String DELETE_STACK_CARD_CARD = "Delete from stackcard where cardID = ?;";
	private final String DELETE_CARD_TAG_CARD = "Delete from cardtag where cardID = ?";

	// --- Delete queries for Tag and all references
	private final String DELETE_TAG = "Delete from tag where _id = ?;";
	private final String DELETE_CARD_TAG_TAG = "Delete from cardtag where tagID = ?";
	private final String DELETE_STACK_TAG_TAG = "Delete from stacktag where tagID = ?;";

	// Delete queries for Runthrough
	private final String DELETE_RUNTHROUGH = "Delete from tag where _id = ?;";

	// -------------END VAR DECLARATION

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

		database.rawQuery(DELETE_STACK_TAG_STACK,
				new String[] { "" + stack.getStackID() });

		database.rawQuery(DELETE_STACK_CARD_STACK,
				new String[] { "" + stack.getStackID() });

		this.close();
		return true;
	}

	public boolean deleteCard(Card card) {
		this.openWrite();

		database.rawQuery(DELETE_CARD, new String[] { "" + card.getCardID() });

		database.rawQuery(DELETE_STACK_CARD_CARD,
				new String[] { "" + card.getCardID() });

		database.rawQuery(DELETE_CARD_TAG_CARD,
				new String[] { "" + card.getCardID() });

		this.close();
		return true;
	}

	public boolean deleteTag(Tag tag) {
		this.openWrite();

		database.rawQuery(DELETE_TAG, new String[] { "" + tag.getTagID() });

		database.rawQuery(DELETE_CARD_TAG_TAG,
				new String[] { "" + tag.getTagID() });

		database.rawQuery(DELETE_STACK_TAG_TAG,
				new String[] { "" + tag.getTagID() });

		this.close();
		return true;
	}

	public boolean deleteRunthrough(Runthrough run) {
		this.openWrite();

		database.rawQuery(DELETE_RUNTHROUGH,
				new String[] { "" + run.getRunthroughID() });

		this.close();
		return true;
	}

	public boolean addNewStack(Stack stack) {
		ContentValues stackContent = new ContentValues();
		stackContent.put("_id", stack.getStackID());
		stackContent.put("stackName", stack.getStackName());
		stackContent.put("isDynamicGenerated", stack.isDynamicGenerated());
		stackContent.put("dontKnow", stack.getDontKnow());
		stackContent.put("notSure", stack.getNotSure());
		stackContent.put("sure", stack.getSure());

		ContentValues stackCardContent = new ContentValues();
		for (Card card : stack.getCards()) {
			String stackcardID = stack.getStackID() + "" + card.getCardID();

			stackCardContent.put("stackID", stack.getStackID());
			stackCardContent.put("cardID", card.getCardID());
			stackCardContent.put("_id", Integer.parseInt(stackcardID));

			this.openWrite();
			database.insert("stackcard", null, stackCardContent);
			this.close();
			stackCardContent.clear();

		}

		this.openWrite();
		database.insert("stack", null, stackContent);
		this.close();

		return true;
	}

	public boolean addNewCard(Card card, int stackID) {
		// Set content for card table
		ContentValues cardContent = new ContentValues();
		cardContent.put("_id", card.getCardID());
		cardContent.put("cardFront", card.getCardFront());
		cardContent.put("cardBack", card.getCardBack());
		cardContent.put("cardFrontPicture", card.getCardFrontPicture());
		cardContent.put("cardBackPicture", card.getCardBackPicture());
		cardContent.put("drawer", card.getDrawer());
		cardContent.put("totalStacks", card.getTotalStacks());

		// Set Content for stackcard table
		ContentValues stackCardContent = new ContentValues();
		stackCardContent.put("stackID", stackID);
		stackCardContent.put("cardID", card.getCardID());
		stackCardContent.put("_id",
				Integer.parseInt(stackID + "" + card.getCardID()));

		// write it to the DB
		this.openWrite();
		database.insert("card", null, cardContent);
		database.insert("stackcard", null, stackCardContent);
		this.close();

		// write the Card-Tag-Correlation to the cardtag table
		ContentValues cardTagValues = new ContentValues();
		for (Tag tag : card.getTags()) {
			cardTagValues.put("cardID", card.getCardID());
			cardTagValues.put("tagID", tag.getTagID());
			cardTagValues.put("_id",
					Integer.parseInt(card.getCardID() + "" + tag.getTagID()));

			this.openWrite();
			database.insert("cardtag", null, cardTagValues);
			this.close();
			cardTagValues.clear();
		}

		return true;
	}

	public boolean addNewTag(Tag tag, List<Card> cards) {
		// Set the contents for the tag table
		ContentValues tagValues = new ContentValues();
		tagValues.put("_id", tag.getTagID());
		tagValues.put("tagName", tag.getTagName());
		tagValues.put("totalCards", tag.getTotalCards());

		this.openWrite();
		database.insert("tag", null, tagValues);
		this.close();

		ContentValues cardTagValues = new ContentValues();
		for (Card card : cards) {
			cardTagValues.put("cardID", card.getCardID());
			cardTagValues.put("tagID", tag.getTagID());
			cardTagValues.put("_id",
					Integer.parseInt(card.getCardID() + "" + tag.getTagID()));

			this.openWrite();
			database.insert("cardtag", null, cardTagValues);
			this.close();
			cardTagValues.clear();
		}

		return true;
	}

	public boolean addNewRunthrough(Runthrough run) {
		ContentValues runValues = new ContentValues();
		runValues.put("_id", run.getRunthroughID());
		runValues.put("stackID", run.getStackID());
		runValues.put("isOverall", run.isOverall());
		runValues.put("startDate", run.getStartDate().getTime());
		runValues.put("endDate", run.getEndDate().getTime());
		runValues.put("beforeDontKnow", run.getStatusBefore()[0]);
		runValues.put("beforeNotSure", run.getStatusBefore()[1]);
		runValues.put("beforeSure", run.getStatusBefore()[2]);
		runValues.put("afterDontKnow", run.getStatusAfter()[0]);
		runValues.put("afterNotSure", run.getStatusAfter()[1]);
		runValues.put("afterSure", run.getStatusAfter()[2]);

		this.openWrite();
		database.insert("runthrough", null, runValues);
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
