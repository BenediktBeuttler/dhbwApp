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
		new Stack(false, name, cards);
		// TODO: insert into db
		
		return true;
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
		
		new Stack(true, name, cards);
		
		// TODO: insert into db
		
		return true;
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
						if (stack.getDynamicStackTags().contains(tag))
						{
							//TODO: add selected card to selected stack
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
