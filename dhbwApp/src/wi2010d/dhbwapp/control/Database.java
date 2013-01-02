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

	// Delete queries for Correlations
	private final String DELETE_CARD_STACK_CORRELATION = "Delete from stackcard where stackID = ? and cardID = ?;";
	private final String DELETE_CARD_TAG_CORRELATION = "Delete from cardtag where cardID = ? and tagID = ?";

	
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

	// --------------Query Methods -----------------

	/**
	 * Queries the stack table
	 * 
	 * @return Cursor containing complete stack table
	 */
	public Cursor queryStack() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_STACK, null);
		return cursor;
	}

	/**
	 * Queries the card table
	 * 
	 * @return Cursor containing complete card table
	 */
	public Cursor queryCard() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_CARD, null);
		return cursor;
	}

	/**
	 * Queries the tag table
	 * 
	 * @return Cursor containing complete tag table
	 */
	public Cursor queryTag() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_TAG, null);
		return cursor;
	}

	/**
	 * Queries the runthrough table
	 * 
	 * @return Cursor containing complete runthrough table
	 */
	public Cursor queryRunthrough() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_RUNTHROUGH, null);
		return cursor;
	}

	/**
	 * Queries the Stack-Card-Table
	 * 
	 * @return Cursor containing complete stackcard table
	 */
	public Cursor queryStackCard() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_STACK_CARD, null);
		return cursor;
	}

	/**
	 * Queries the Card-Tag-Table
	 * 
	 * @return Cursor containing complete cardtag table
	 */
	public Cursor queryCardTag() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_CARD_TAG, null);
		return cursor;
	}

	/**
	 * Queries the Stack-Tag-Table
	 * 
	 * @return Cursor containing complete stacktag table
	 */
	public Cursor queryStackTag() {
		this.openRead();
		Cursor cursor = database.rawQuery(QUERY_STACK_TAG, null);
		return cursor;
	}

	// --------------Delete Methods -----------------

	/**
	 * Deletes the given stack and all correlations in the db
	 * 
	 * @param stack
	 *            The stack, which should be deleted
	 * @return always true
	 */
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

	/**
	 * Deletes the given card and all correlations in the db
	 * 
	 * @param card
	 *            The card, which should be deleted
	 * @return always true
	 */
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

	/**
	 * Deletes the given tag and all correlations in the db
	 * 
	 * @param tag
	 *            The tag, which should be deleted
	 * @return always true
	 */
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

	/**
	 * Deletes the given runthrough from the db
	 * 
	 * @param run
	 *            The runthrough, which should be deleted
	 * @return always true
	 */
	public boolean deleteRunthrough(Runthrough run) {
		this.openWrite();

		database.rawQuery(DELETE_RUNTHROUGH,
				new String[] { "" + run.getRunthroughID() });

		this.close();
		return true;
	}

	/** 
	 * Deletes the Card Stack Correlation for the given Stack ID and Card ID
	 * @param stackID The Stack ID
	 * @param cardID The Card ID
	 * @return always true
	 */
	public boolean deleteCardStackCorrelation(int stackID, int cardID) {
		this.openWrite();

		database.rawQuery(DELETE_CARD_STACK_CORRELATION, new String[] {
				"" + stackID, "" + cardID });

		this.close();
		return true;
	}
	
	/**
	 * Deletes the Card Tag Correlation for the given Card ID and Tag ID
	 * @param cardID The Card ID
	 * @param tagID The Tag ID
	 * @return always true
	 */
	public boolean deleteCardTagCorrelation(int cardID, int tagID)
	{
		this.openWrite();

		database.rawQuery(DELETE_CARD_TAG_CORRELATION, new String[] {
				"" + cardID, "" + tagID });

		this.close();
		return true;

	}

	// --------------Add Methods -----------------

	/**
	 * Adds a new Stack and all correlations to the DB
	 * 
	 * @param stack
	 *            The new Stack
	 * @return always true
	 */
	public boolean addNewStack(Stack stack) {
		ContentValues stackContent = putStackValues(stack);

		for (Card card : stack.getCards()) {
			this.addStackCardCorrelation(stack.getStackID(), card.getCardID());

		}

		// If it's a dynamic generated stack, add the correlations
		if (stack.isDynamicGenerated()) {
			for (Tag tag : stack.getDynamicStackTags()) {
				this.addStackTagCorrelation(stack.getStackID(), tag.getTagID());
			}
		}

		this.openWrite();
		database.insert("stack", null, stackContent);
		this.close();

		return true;
	}

	/**
	 * Adds a new Card and all correlations to the DB
	 * 
	 * @param card
	 *            The new Card
	 * @return always true
	 */
	public boolean addNewCard(Card card, int stackID) {
		// Set content for card table
		ContentValues cardContent = putCardValues(card);

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
		for (Tag tag : card.getTags()) {
			this.addCardTagCorrelation(card.getCardID(), tag.getTagID());
		}

		return true;
	}

	/**
	 * Adds a new Card and the Card-Tag-Correlation to the DB !! Use this method
	 * only, when Creating a new Stack, because the Stack-Card-Correlation isn't
	 * set
	 * 
	 * @param card
	 *            The Card to write to the DB
	 * @return Always true
	 */
	public boolean addNewCard(Card card) {
		// Set content for card table
		ContentValues cardContent = putCardValues(card);

		// write it to the DB
		this.openWrite();
		database.insert("card", null, cardContent);
		this.close();

		// write the Card-Tag-Correlation to the cardtag table
		for (Tag tag : card.getTags()) {
			this.addCardTagCorrelation(card.getCardID(), tag.getTagID());
		}

		return true;
	}

	/**
	 * Adds a new Tag and the correlations to the cards
	 * 
	 * @param tag
	 *            The new tag
	 * @param cards
	 *            The list of cards, containing this tag
	 * @return always true
	 */
	public boolean addNewTag(Tag tag, List<Card> cards) {
		// Set the contents for the tag table
		ContentValues tagValues = putTagValues(tag);

		this.openWrite();
		database.insert("tag", null, tagValues);
		this.close();

		for (Card card : cards) {
			this.addCardTagCorrelation(card.getCardID(), tag.getTagID());
		}

		return true;
	}

	/**
	 * Adds a new Tag, without any correlations
	 * 
	 * @param tag
	 *            The new Tag
	 * @return always true
	 */
	public boolean addNewTag(Tag tag) {
		// Set the contents for the tag table
		ContentValues tagValues = putTagValues(tag);
		this.openWrite();
		database.insert("tag", null, tagValues);
		this.close();
		return true;
	}

	/**
	 * Adds a new Runthrough to the DB
	 * 
	 * @param run
	 *            The new Runthrough
	 * @return always true
	 */
	public boolean addNewRunthrough(Runthrough run) {
		ContentValues runValues = putRunthroughValues(run);

		this.openWrite();
		database.insert("runthrough", null, runValues);
		this.close();

		return true;
	}

	/**
	 * Adds a new Stack Card Correlation
	 * 
	 * @param stackID
	 *            The stackID
	 * @param cardID
	 *            The cardID
	 * @return always true
	 */
	public boolean addStackCardCorrelation(int stackID, int cardID) {
		ContentValues stackCardContent = new ContentValues();

		stackCardContent.put("stackID", stackID);
		stackCardContent.put("cardID", cardID);
		stackCardContent.put("_id", Integer.parseInt(stackID + "" + cardID));

		this.openWrite();
		database.insert("stackcard", null, stackCardContent);
		this.close();

		return true;
	}

	/**
	 * Add a new Stack Tag Correlation
	 * 
	 * @param stackID
	 *            The stackID
	 * @param tagID
	 *            The tagID
	 * @return always true
	 */
	public boolean addStackTagCorrelation(int stackID, int tagID) {
		ContentValues StackTagValues = new ContentValues();

		StackTagValues.put("stackID", stackID);
		StackTagValues.put("tagID", tagID);
		StackTagValues.put("_id", Integer.parseInt(stackID + "" + tagID));

		this.openWrite();
		database.insert("stacktag", null, StackTagValues);
		this.close();

		return true;

	}

	/**
	 * Adds a new Card Tag Correlation
	 * 
	 * @param cardID
	 *            The cardID
	 * @param tagID
	 *            The tagID
	 * @return always true
	 */
	public boolean addCardTagCorrelation(int cardID, int tagID) {
		ContentValues CardTagValues = new ContentValues();

		CardTagValues.put("cardID", cardID);
		CardTagValues.put("tagID", tagID);
		CardTagValues.put("_id", Integer.parseInt(cardID + "" + tagID));

		this.openWrite();
		database.insert("cardtag", null, CardTagValues);
		this.close();

		return true;

	}

	// --------------Change Methods -----------------
	// not useful to use the addNewXXXXX() and deleteXXXX() methods,
	// because there's too much overhead -> correlations are deleted and
	// created, which is not needed

	/**
	 * Changes the given stack in the DB !! stackID needs to be the same !!
	 * 
	 * @param stack
	 *            The changed stack
	 * @return always true
	 */
	public boolean changeStack(Stack stack) {
		this.openWrite();

		database.rawQuery(DELETE_STACK,
				new String[] { "" + stack.getStackID() });

		ContentValues stackContent = putStackValues(stack);
		database.insert("stack", null, stackContent);
		this.close();

		return true;
	}

	/**
	 * Changes the given Card in the DB !! cardID needs to be the same !!
	 * 
	 * @param card
	 *            The changed Card
	 * @return always true
	 */
	public boolean changeCard(Card card) {

		this.openWrite();		
		database.delete("card", "_id = ?", new String[] { "" + card.getCardID() });
		
		ContentValues cardContent = putCardValues(card);

		database.insert("card", null, cardContent);
		this.close();

		return true;

	}

	/**
	 * Changes the given runthrough
	 * 
	 * @param run
	 *            The changed runthrough
	 * @return always true
	 */
	public boolean changeRunthrough(Runthrough run) {
		this.openWrite();
		database.rawQuery(DELETE_RUNTHROUGH,
				new String[] { "" + run.getRunthroughID() });

		ContentValues runthroughContent = putRunthroughValues(run);

		database.insert("runthrough", null, runthroughContent);
		this.close();

		return true;
	}

	/**
	 * Changes the given Tag !! TagID needs to be the same !!
	 * 
	 * @param tag
	 *            The changed tag
	 * @return always true
	 */
	public boolean changeTag(Tag tag) {
		this.openWrite();
		database.rawQuery(DELETE_TAG, new String[] { "" + tag.getTagID() });

		ContentValues tagValues = putTagValues(tag);

		database.insert("tag", null, tagValues);
		this.close();

		return true;
	}

	// --------------Put Values Methods -----------------

	/**
	 * Puts the values of the stack to a new Content Value-Map and returns it
	 * 
	 * @param stack
	 *            The stack, whose values should be set
	 * @return The Content Value Map filled with the stacks' values
	 */
	private ContentValues putStackValues(Stack stack) {
		ContentValues stackContent = new ContentValues();
		stackContent.put("_id", stack.getStackID());
		stackContent.put("stackName", stack.getStackName());
		stackContent.put("isDynamicGenerated", stack.isDynamicGenerated());
		stackContent.put("dontKnow", stack.getDontKnow());
		stackContent.put("notSure", stack.getNotSure());
		stackContent.put("sure", stack.getSure());

		return stackContent;
	}

	/**
	 * Puts the values of the card to a new Content Value-Map and returns it
	 * 
	 * @param card
	 *            The card, whose values should be set
	 * @return The Content Value Map filled with the cards' values
	 */
	private ContentValues putCardValues(Card card) {
		ContentValues cardContent = new ContentValues();
		cardContent.put("_id", card.getCardID());
		cardContent.put("cardFront", card.getCardFront());
		cardContent.put("cardBack", card.getCardBack());
		cardContent.put("cardFrontPicture", card.getCardFrontPicture());
		cardContent.put("cardBackPicture", card.getCardBackPicture());
		cardContent.put("drawer", card.getDrawer());
		cardContent.put("totalStacks", card.getTotalStacks());

		return cardContent;

	}

	/**
	 * Puts the values of the runthrough to a new Content Value-Map and returns
	 * it
	 * 
	 * @param run
	 *            The runthough, whose values should be set
	 * @return The Content Value Map filled with the runthroughs' values
	 */
	private ContentValues putRunthroughValues(Runthrough run) {

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

		return runValues;
	}

	/**
	 * Puts the values of the tag to a new Content Value-Map and returns it
	 * 
	 * @param tag
	 *            The tag, whose values should be set
	 * @return The Content Value Map filled with the tags' values
	 */
	private ContentValues putTagValues(Tag tag) {
		ContentValues tagValues = new ContentValues();

		tagValues.put("_id", tag.getTagID());
		tagValues.put("tagName", tag.getTagName());
		tagValues.put("totalCards", tag.getTotalCards());

		return tagValues;

	}

	// --------------"Singleton" Method -----------------

	public static Database getInstance() {
		if (db == null) {
			db = new Database();
		}
		return db;
	}
}
