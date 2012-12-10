package wi2010d.dhbwapp.control;

import java.util.Date;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.content.Context;
import android.database.Cursor;

public class Init {

	private Database database;
	private static Init init;
	private boolean runComplete = false;

	public Init(Context context) {
		DatabaseManager.getInstance(context);
		this.database = Database.getInstance();
	}

	public boolean loadFromDB() {
		if (runComplete == false) {
			// load the objects from DB
			this.loadStacks();
			this.loadCards();
			this.loadTags();
			this.loadRunthroughs();

			// assign the objects
			this.assignCardsToStacks();
			this.assignTagsToCards();
			this.assignTagstoStacks();
		}
		runComplete = true;
		return true;
	}

	private boolean loadStacks() {

		int id;
		String name;
		boolean isDynamicGenerated;
		int sure;
		int notSure;
		int dontKnow;

		Cursor cursor = database.queryStack();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			id = cursor.getInt(0);
			name = cursor.getString(1);
			int isDynamicGeneratedInt = cursor.getInt(2);

			if (isDynamicGeneratedInt == 0) {
				isDynamicGenerated = true;
			} else {
				isDynamicGenerated = false;
			}

			dontKnow = cursor.getInt(3);
			notSure = cursor.getInt(4);
			sure = cursor.getInt(5);

			new Stack(isDynamicGenerated, id, name, sure, notSure, dontKnow);

			cursor.moveToNext();
		}
		cursor.close();
		return true;
	}

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
		return true;

	}

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
		return true;

	}

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

			startDate = new Date(cursor.getLong(3));
			endDate = new Date(cursor.getLong(4));
			dontKnowBefore = cursor.getInt(5);
			notSureBefore = cursor.getInt(6);
			sureBefore = cursor.getInt(7);
			dontKnowAfter = cursor.getInt(8);
			notSureAfter = cursor.getInt(9);
			sureAfter = cursor.getInt(10);

			int[] statusBefore = { dontKnowBefore, notSureBefore, sureBefore };
			int[] statusAfter = { dontKnowAfter, notSureAfter, sureAfter };
			new Runthrough(id, stackID, isOverall, startDate, endDate,
					statusBefore, statusAfter);

			cursor.moveToNext();

		}
		cursor.close();
		return true;
	}

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
		return true;
	}

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
		}
		cursor.close();
		return true;
	}

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
							stack.getDynamicStackTags();
							break;
						}
					}
				}
			}
		}
		cursor.close();
		return true;

	}

	public static Init getInstance(Context context) {
		if (init == null) {
			init = new Init(context);
		}
		return init;

	}
}
