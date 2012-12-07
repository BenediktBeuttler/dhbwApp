package wi2010d.dhbwapp.control;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

public class Delete {

	public void deleteStack(Stack stack) {
		for(Card card : stack.getCards())
		{
			int totalStacks = card.decreaseTotalStacks();
			if(totalStacks == 0)
			{
				deleteCard(card);
			}
		}
		
		for(Runthrough run : stack.getLastRunthroughs())
		{
			run = null;
		}
		Runthrough run = stack.getOverallRunthrough();
		run = null;
		stack = null;
	}

	public void deleteCard(Card card) {
		
		for(Tag tag : card.getTags())
		{
			int totalCards = tag.decreaseTotalCards();
			if(totalCards == 0)
			{
				deleteTag(tag);
			}
		}
		card = null;		
	}

	public void deleteTag(Tag tag) {
				

	}
}
