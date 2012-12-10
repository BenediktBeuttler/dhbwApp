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

	public Init(Context context) {
		this.database = Database.getInstance();
	}

	public boolean newStack(int id, String name, boolean isDynamicGenerated,
			int dontKnow, int notSure, int sure) {
		try {
			new Stack(isDynamicGenerated, id, name, sure, notSure, dontKnow);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean newTag(int id, String name, int totalCards) {
		try {
			new Tag(id, totalCards, name);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean newCard(int id, int drawer, int totalStacks, String front,
			String back, String frontPic, String backPic) {
		try {
			new Card(id, drawer, totalStacks, front, back, frontPic, backPic);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean newRunthrough(int id, Date startDate, Date endDate,
			int sureBefore, int sureAfter, int dontKnowBefore,
			int dontKnowAfter, int notSureBefore, int notSureAfter,
			boolean isOverall, int stackID) {
		try {
			int[] statusBefore = { dontKnowBefore, notSureBefore, sureBefore };
			int[] statusAfter = { dontKnowAfter, notSureAfter, sureAfter };
			new Runthrough(id, stackID, isOverall, startDate, endDate,
					statusBefore, statusAfter);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean assignCardsToStacks() {
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

	public boolean assignTagsToCards() {
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

		return true;
	}

	public boolean assignTagstoStacks() {
		Cursor cursor = database.queryStackTag();
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast())
		{
			int stackID = cursor.getInt(0);
			int tagID  = cursor.getInt(1);
			
			for(Stack stack : Stack.allStacks)
			{	
				if(stack.isDynamicGenerated() && stackID == stack.getStackID())
				{
					for(Tag tag : Tag.allTags)
					{
						if(tagID == tag.getTagID())
						{
							stack.getDynamicStackTags();
							break;
						}
					}
				}
			}
		}
		return false;

	}
}
