package wi2010d.dhbwapp.control;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

public class Delete {

	private Database db = Database.getInstance();

	public void deleteStack(Stack stack) {
		// delete the card from the stack, if the card isn't in any stack,
		// delete it
		for (Card card : stack.getCards()) {
			int totalStacks = card.decreaseTotalStacks();
			if (totalStacks == 0) {
				deleteCard(card);
			}
		}

		// Delete all associated runthroughs
		for (Runthrough run : stack.getLastRunthroughs()) {
			deleteRunthrough(run);
			db.deleteRunthrough(run);
			run = null;
		}

		// Delete the overall runthrough
		Runthrough run = stack.getOverallRunthrough();
		deleteRunthrough(run);
		db.deleteRunthrough(run);
		run = null;

		// delete the Stack from the Stack List
		for (int i = 0; i < Stack.allStacks.size(); i++) {
			if (Stack.allStacks.get(i).getStackID() == stack.getStackID()) {
				Stack.allStacks.remove(i);
				break;
			}
		}

		db.deleteStack(stack);
		stack = null;

	}

	public void deleteRunthrough(Runthrough run) {
		// Delete Runthrough from the Runthrough List
		for (int i = 0; i < Runthrough.allRunthroughs.size(); i++) {
			if (Runthrough.allRunthroughs.get(i).getRunthroughID() == run
					.getRunthroughID()) {
				Runthrough.allRunthroughs.remove(i);
				break;
			}
		}

	}

	public void deleteCard(Card card) {
		// delete the tag from the card, if the tag isn't associated to a card,
		// delete it
		for (Tag tag : card.getTags()) {
			int totalCards = tag.decreaseTotalCards();
			if (totalCards == 0) {
				deleteTag(tag);
			}
		}

		// remove the card from every stack it is in
		for (Stack stack : Stack.allStacks) {
			for (int i = 0; i < stack.getCards().size(); i++) {
				if (stack.getCards().get(i).getCardID() == card.getCardID()) {
					stack.getCards().remove(i);
					i = i - 1;
				}
			}
		}

		// Delete the card from the cardlist
		for (int i = 0; i < Card.allCards.size(); i++) {
			if (Card.allCards.get(i).getCardID() == card.getCardID()) {
				Card.allCards.remove(i);
				break;
			}
		}

		// delete the card
		db.deleteCard(card);
		card = null;
	}

	public void deleteTag(Tag tag) {
		// delete the given Tag from the dynamic stacks
		for (Stack stack : Stack.allStacks) {
			if (stack.isDynamicGenerated()) {
				for (int i = 0; i < stack.getDynamicStackTags().size(); i++) {
					if (tag.getTagID() == stack.getDynamicStackTags().get(i)
							.getTagID()) {
						stack.getDynamicStackTags().remove(i);
						i = i - 1;
					}
				}
			}
		}

		// delete the given Tag from the cards
		for (Card card : Card.allCards) {
			for (int i = 0; i < card.getTags().size(); i++) {
				if (tag.getTagID() == card.getTags().get(i).getTagID()) {
					card.getTags().remove(i);
					i = i - 1;
				}
			}
		}

		// delete the Tag from the Tag list
		for (int i = 0; i < Tag.allTags.size(); i++) {
			if (Tag.allTags.get(i).getTagID() == tag.getTagID()) {
				Tag.allTags.remove(i);
				break;
			}
		}

		// delete the tag
		db.deleteTag(tag);
		tag = null;
	}
}
