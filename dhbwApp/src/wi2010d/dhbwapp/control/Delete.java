package wi2010d.dhbwapp.control;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

/**
 * Provides methods to delete the objects.
 */
public class Delete {

	private Database db = Database.getInstance();
	private static Delete delete;
	private boolean fromDeleteStack = false;

	public Delete() {
		super();
	}

	/**
	 * Singleton method
	 */
	public static Delete getInstance() {
		if (delete == null) {
			delete = new Delete();
		}
		return delete;
	}

	/**
	 * Deletes the given Stack and all correlations. If necessary it also
	 * deletes the cards/tags.
	 * 
	 * @param stack
	 *            The stack, which should be deleted
	 * @return always true
	 */
	public boolean deleteStack(Stack stack) {
		// delete the card from the stack, if the card isn't in any stack,
		// delete it
		for (Card card : stack.getCards()) {
			int totalStacks = card.decreaseTotalStacks();
			Database.getInstance().changeCard(card);
			if (totalStacks == 0) {
				fromDeleteStack = true;
				deleteCard(card);
			}
		}

		// Delete all associated runthroughs
		for (Runthrough run : stack.getLastRunthroughs()) {
			deleteRunthrough(run);
			run = null;
		}

		// Delete the overall runthrough
		Runthrough run = stack.getOverallRunthrough();
		deleteRunthrough(run);
		run = null;

		// delete the Stack from the Stack List
		Stack.allStacks.remove(stack);

		db.deleteStack(stack);
		stack = null;
		fromDeleteStack = false;
		return true;
	}

	/**
	 * Delete Runthrough from the Runthrough List and DB
	 * 
	 * @param run
	 *            The Runthrough, which should be deleted
	 * @return always true
	 */
	public boolean deleteRunthrough(Runthrough run) {
		Runthrough.allRunthroughs.remove(run);
		db.deleteRunthrough(run);
		return true;
	}

	/**
	 * Deletes the given Card and all Correlations. If necessary it also deletes
	 * Tags or the Stack.
	 * 
	 * @param card
	 *            The card, which should be deleted
	 */
	public boolean deleteCard(Card card) {
		// delete the tag from the card, if the tag isn't associated to a card,
		// delete it
		for (Tag tag : card.getTags()) {
			int totalCards = tag.decreaseTotalCards();
			if (totalCards == 0) {
				deleteTag(tag);
			}
		}

		if (fromDeleteStack == false) { // workaround to avoid
										// ConcurrentModificationException
			// remove the card from every stack it is in
			for (Stack stack : Stack.allStacks) {
				stack.getCards().remove(card);

				// decrease the number of cards in the drawer in the stack
				switch (card.getDrawer()) {
				case 0:
					stack.setDontKnow(stack.getDontKnow() - 1);
					break;
				case 1:
					stack.setNotSure(stack.getSure() - 1);
					break;
				case 2:
					stack.setSure(stack.getSure() - 1);
					break;
				default:
					// do nothing, case not possible
				}

				// if there's no card in the stack, delete it
				if (stack.getCards().size() == 0) {
					deleteStack(stack);
				}
			}
		}
		// Delete the card from the cardlist
		Card.allCards.remove(card);

		// delete the card
		db.deleteCard(card);
		card = null;
		return true;
	}

	/**
	 * Delete the given Tag and all correlations. If necessary, it also deltes
	 * 
	 * @param tag
	 *            The tag, which should be deleted
	 * @return always true
	 */
	public boolean deleteTag(Tag tag) {
		// delete the given Tag from the dynamic stacks
		for (Stack stack : Stack.allStacks) {
			if (stack.isDynamicGenerated()) {
				for (int i = 0; i < stack.getDynamicStackTags().size(); i++) {
					if (stack.getDynamicStackTags().get(i).getTagID() == tag
							.getTagID()) {
						stack.getDynamicStackTags().remove(i);
						Create.getInstance().updateDynStack(stack);
					}
				}
			}
		}

		// delete the given Tag from the cards
		if (tag.getTotalCards() > 0) {
			for (int i = 0; i < Card.allCards.size(); i++) {
				for (int j = 0; j < Card.allCards.get(i).getTags().size(); j++) {
					if (Card.allCards.get(i).getTags().get(j).getTagID() == tag
							.getTagID()) {
						Card.allCards.get(i).getTags().remove(j);
						break;
					}
				}
			}
		}

		// delete the Tag from the Tag list
		for (int i = 0; i < Tag.allTags.size(); i++) {
			if (Tag.allTags.get(i).getTagID() == tag.getTagID()) {
				Tag.allTags.remove(i);
			}
		}
		Tag.allTags.remove(tag);
		
		// delete the tag
		db.deleteTag(tag); 
		tag = null;
		return true;
	}
}
