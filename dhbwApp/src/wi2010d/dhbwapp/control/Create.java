package wi2010d.dhbwapp.control;

import wi2010d.dhbwapp.errorhandler.ErrorHandler;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Create {

	private static Create create;

	/**
	 * Constructor
	 */
	public Create() {
	}

	/**
	 * Singleton Method
	 * 
	 * @return
	 */
	public static Create getInstance() {
		if (create == null) {
			create = new Create();
		}

		return create;
	}

	/**
	 * Create a new Stack
	 * 
	 * @param name
	 * @param card
	 * @return
	 */
	public boolean newStack(String name, Card card) {
		List<Card> cards = new ArrayList<Card>();
		cards.add(card);
		

		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				ErrorHandler.getInstance().handleError(
						ErrorHandler.getInstance().NAME_ALREADY_TAKEN);
				return false;
			}
		}
		 Database.getInstance()
			.addNewStack(new Stack(false, name, cards));
		return true;
	}

	/**
	 * Create a new dynamic Stack
	 * 
	 * @param name
	 * @param tags
	 * @return
	 */
	public boolean newDynStack(String name, List<Tag> tags) {
		List<Card> cards = new ArrayList<Card>();
		Stack dynamicStack;
		
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				ErrorHandler.getInstance().handleError(
						ErrorHandler.getInstance().NAME_ALREADY_TAKEN);
				return false;
			}
		}

		// identify all cards that contain the selected tags
		for (Card card : Card.allCards) {
			for (Tag tag : card.getTags()) {
				if (tags.contains(tag)) {
					// add identified cards to list
					cards.add(card);
					
				}
			}
		}
		
		if (cards.size() > 0){
			dynamicStack = new Stack(true, name, cards);
			Log.e("StackID: ", ""+Stack.getNextStackID());
			dynamicStack.setDynamicStackTags(tags);
			return Database.getInstance().addNewStack(dynamicStack);
		}else{
			//TODO: Bene: Error handler
			//ErrorHandler.blablabla
			return false;
		}

	}

	/**
	 * Update all dynamic stacks
	 * 
	 * @return
	 */
	public boolean updateDynStack() {
		for (Stack stack : Stack.allStacks) {
			if (stack.isDynamicGenerated()) {
				for (Card card : Card.allCards) {
					for (Tag tag : card.getTags()) {
						// If card has same tag as stack and is not in the
						// actual stack
						if (stack.getDynamicStackTags().contains(tag)
								&& !stack.getCards().contains(card)) {
							Edit.getInstance().addCardToStack(stack, card);
						}

						// If card is in stack but doesn't have the tag anymore
						if (stack.getCards().contains(card)
								&& !stack.getDynamicStackTags().contains(tag)) {
							Edit.getInstance().removeCardFromStack(stack, card);
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
	public Card newCard(String front, String back, List<Tag> tags,
			String frontPic, String backPic) {
		Log.d("Card front:", front);
		Card card = new Card(front, back, frontPic, backPic, tags);
		Log.d("Card ID: ", ""+card.getCardID());
		Database.getInstance().addNewCard(card);
		return card;
	}

	/**
	 * Creates a new tag and returns it
	 * 
	 * @param name
	 * @return
	 */
	public Tag newTag(String name) {
		
		for(Tag tag : Tag.allTags)
		{ 
			if(tag.getTagName().equals(name))
			{
				return null;
			}
		}
		
		Tag tag = new Tag(name);
		Database.getInstance().addNewTag(tag);
		return tag;
	}

}
