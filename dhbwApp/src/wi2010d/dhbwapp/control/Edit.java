package wi2010d.dhbwapp.control;

import java.util.List;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;


public class Edit {
	
	/**
	 * Add a selected card to existing stack
	 * 
	 * @param stack
	 * @param card
	 * @return
	 */
	public boolean addCardToStack(Stack stack, Card card)
	{
		stack.getCards().add(card);
		card.increaseTotalStacks();
		//TODO: insert into database
		return true;
	}
	
	public boolean removeCardFromStack(Stack stack, Card card)
	{
		for (Card cards : )
		return true;
	}

	/**
	 * Add some one tag to the whole selected stack
	 * 
	 * @param tag
	 * @param stack
	 * @return
	 */
	public boolean addTagToStack(Tag tag, Stack stack)
	{
		for (Card card : stack.getCards())
		{
			card.getTags().add(tag);
			tag.increaseTotalCards();
			//TODO: insert into database
		}
		return true;
	}
	
	/**
	 * Add a Tag to a single card
	 * 
	 * @param tag
	 * @param card
	 * @return
	 */
	public boolean addTagToCard(Tag tag, Card card)
	{
		card.getTags().add(tag);
		tag.increaseTotalCards();
		//TODO: insert into database
		//TODO: insert multiple tags???
		return true;
	}
	
	/**
	 * Change name of selected stack
	 * 
	 * @param name
	 * @param stack
	 * @return
	 */
	public boolean changeStackName(String name, Stack stack)
	{
		stack.setStackName(name);
		//TODO: Change in database
		return true;
	}
	
	/**
	 * Change card properties
	 * 
	 * @param front
	 * @param back
	 * @param frontPic
	 * @param backPic
	 * @param tags
	 * @param card
	 * @return
	 */
	public boolean changeCard(String front, String back, String frontPic, String backPic, List<Tag> tags, Card card)
	{
		card.setCardFront(front);
		card.setCardBack(back);
		card.setCardFrontPicture(frontPic);
		card.setCardBackPicture(backPic);
		//TODO: set Tags and update tag number
		//TODO: change in database
		return true;
	}
	
	/**
	 * Reset Drawer of all cards of a selected stack
	 * 
	 * @param stack
	 * @return
	 */
	public boolean resetDrawer(Stack stack)
	{
		stack.setDontKnow(stack.getCards().size());
		stack.setNotSure(0);
		stack.setSure(0);
		
		for (Card card : stack.getCards())
		{
			card.setDrawer(0);
			
		}
		
		return true;
	}
	
	/**
	 * Edit tag name
	 * 
	 * @param name
	 * @param tag
	 * @return
	 */
	public boolean editTag(String name, Tag tag)
	{
		tag.setTagName(name);
		//TODO: change in database
		return true;
	}
	

}
