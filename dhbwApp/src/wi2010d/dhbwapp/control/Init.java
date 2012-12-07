package wi2010d.dhbwapp.control;

import java.util.Date;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

public class Init {

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
		// TODO: Read Database, then assign by IDs
		return false;
	}

	public boolean assignTagsToCards() {
		// TODO: Read DB, assign by IDs
		return false;
	}

	public boolean assignTagstoStacks() {
		// TODO: Read DB, assign by IDs
		return false;
	}
}
