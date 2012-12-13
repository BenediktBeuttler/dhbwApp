package wi2010d.dhbwapp.control;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class Create {

	
	/**
	 * Create a new Stack
	 * 
	 * @param name
	 * @param card
	 * @return
	 */
	public boolean newStack(String name, Card card)
	{
		List<Card> cards = new ArrayList<Card>();
		
		cards.add(card);		
		return Database.getInstance().addNewStack(new Stack(false, name, cards));
	}
	
	/**
	 * Create a new dynamic Stack
	 * 
	 * @param name
	 * @param tags
	 * @return
	 */
	public boolean newDynStack(String name, List<Tag> tags)
	{
		List<Card> cards = new ArrayList<Card>();
		
		//identify all cards that contain the selected tags
		for (Card card : Card.allCards)
		{
			for (Tag tag : card.getTags())
			{
				if (tags.contains(tag))
				{
					//add identified cards to list
					cards.add(card);
				}
			}
		}
		
		return Database.getInstance().addNewStack(new Stack(true, name, cards));
		
	}
	
	/**
	 * Update all dynamic stacks
	 * 
	 * @return
	 */
	public boolean updateDynStack()
	{
		for (Stack stack : Stack.allStacks)
		{
			if (stack.isDynamicGenerated())
			{
				for (Card card : Card.allCards)
				{
					for (Tag tag : card.getTags())
					{
						//If card has same tag as stack and is not in the actual stack
						if (stack.getDynamicStackTags().contains(tag) && !stack.getCards().contains(card))
						{
							Edit.getInstance().addCardToStack(stack, card);
							Database.getInstance().addStackCardCorrelation(stack.getStackID(), card.getCardID());
						}
						
						//If card is in stack but doesn't have the tag anymore
						if (stack.getCards().contains(card) && !stack.getDynamicStackTags().contains(tag))
						{
							stack.getCards().add(card);
							// remove correlation
						}
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Creates a new card and returns it
	 * 
	 * @param front
	 * @param back
	 * @param tags
	 * @param frontPic
	 * @param backPic
	 * @return
	 */
	public Card newCard(String front, String back, List<Tag> tags, String frontPic, String backPic)
	{
		Card card = new Card(front, back, frontPic, backPic, tags);
		return card;
	}
	
	/**
	 * Creates a new tag and returns it
	 * 
	 * @param name
	 * @return
	 */
	public Tag newTag(String name)
	{
		Tag tag = new Tag(name);
		return tag;
	}
	
	
}
