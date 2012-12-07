package wi2010d.dhbwapp.control;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

public class Delete {

	public void deleteStack(Stack stack) {
		// delete the card from the stack, if the card isn't in any stack,
		// delete it
		for (Card card : stack.getCards()) {
			int totalStacks = card.decreaseTotalStacks();
			if (totalStacks == 0) {
				deleteCard(card);
			}
		}

		for (Runthrough run : stack.getLastRunthroughs()) {
			run = null;
		}
		Runthrough run = stack.getOverallRunthrough();
		run = null;
		stack = null;
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
				}
			}
		}
		// delete the card
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
					}
				}
			}
		}

		// delete the given Tag from the cards
		for (Card card : Card.allCards) {
			for (int i = 0; i < card.getTags().size(); i++) {
				if (tag.getTagID() == card.getTags().get(i).getTagID()) {
					card.getTags().remove(i);
				}
			}
		}

		// delete the tag
		tag = null;
	}
}
