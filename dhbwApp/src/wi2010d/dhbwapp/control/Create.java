package wi2010d.dhbwapp.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.widget.Toast;

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
	 * @return Class create
	 */
	public static Create getInstance() {
		if (create == null) {
			create = new Create();
		}

		return create;
	}

	/**
	 * Creates a new Stack
	 * 
	 * @param name
	 *            : Name of new Stack
	 * @param card
	 *            : Card supposed to be contained in the new Stack
	 * @return boolean if it worked
	 */
	public boolean newStack(String name, Card card) {

		// Create new List with Cards and add the card supposed to be in the new
		// Stack
		List<Card> cards = new ArrayList<Card>();
		cards.add(card);

		// Check if Name of new Stack is allowed or already taken
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				Toast.makeText(ErrorHandlerFragment.applicationContext,
						"Stack name already taken, please select another one",
						Toast.LENGTH_LONG).show();
				return false;
			}
		}

		// Increase totalStacks of Card and save new data in DB
		card.increaseTotalStacks();
		Database.getInstance().changeCard(card);
		Database.getInstance().addNewStack(new Stack(false, name, cards));
		return true;
	}

	/**
	 * Create a new Stack with random Cards
	 * 
	 * @param name
	 *            Name of random Stacks
	 * @param card
	 *            One Card that is supposed to be in the new random Stack
	 * @return the created Stack with random Cards
	 */
	public Stack newRandomStack(String name, Card card) {

		// Create List with new Cards, add Card and increase total Stacks of
		// Card
		List<Card> cards = new ArrayList<Card>();
		cards.add(card);
		card.increaseTotalStacks();

		// Check if the new Stack Name is allowed or already taken
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				Toast.makeText(ErrorHandlerFragment.applicationContext,
						"Stack name already taken", Toast.LENGTH_LONG).show();
				return null;
			}
		}

		// Add random Cards to Stack
		Random generator = new Random();

		for (int i = 0; i <= Card.allCards.size() / 5; i++) {
			Card cardToAdd = Card.allCards.get(generator.nextInt(Card.allCards
					.size()));
			cards.add(cardToAdd);
			cardToAdd.increaseTotalStacks();
		}

		return new Stack(false, name, cards);
	}

	/**
	 * Create a new dynamic Stack
	 * 
	 * @param name
	 *            : Name of new dynamic Stack
	 * @param tags
	 *            : Tags of dynamic Stack
	 * @return boolean, true if it worked
	 */
	public boolean newDynStack(String name, List<Tag> tags) {
		List<Card> cards = new ArrayList<Card>();
		Stack dynamicStack;
		boolean containsTag;

		// Check if Stack Name is allowed or already taken
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				Toast.makeText(ErrorHandlerFragment.applicationContext,
						"Stack name already taken", Toast.LENGTH_LONG).show();
				return false;
			}
		}

		// identify all cards that contain the selected tags
		for (Card card : Card.allCards) {
			containsTag = false;

			for (Tag tag : card.getTags()) {
				if (tags.contains(tag)) {
					containsTag = true;
				}
			}

			if (containsTag) {
				// add identified cards to list
				cards.add(card);
				card.increaseTotalStacks();
				Database.getInstance().changeCard(card);
			}
		}

		// Write new Data into DB
		dynamicStack = new Stack(true, name, cards);
		dynamicStack.setDynamicStackTags(tags);

		return Database.getInstance().addNewStack(dynamicStack);

	}

	/**
	 * Update all dynamic stacks
	 * 
	 * @return boolean - true, if it worked
	 */
	public boolean updateDynStacks() {
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
				if (stack.getDynamicStackTags().size() == 0) {
					Delete.getInstance().deleteStack(stack);
				}
			}
		}

		return true;
	}

	/**
	 * Update all dynamic stacks
	 * 
	 * @return boolean - true, if it worked
	 */
	public boolean updateDynStack(Stack stack) {

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
			if (stack.getDynamicStackTags().size() == 0) {
				Delete.getInstance().deleteStack(stack);
				Toast.makeText(
						ErrorHandlerFragment.applicationContext,
						"Dyn. Stack "
								+ stack.getStackName()
								+ " had no tags anymore, so it has been deleted",
						Toast.LENGTH_LONG).show();
			}
		}
		return true;
	}

	/**
	 * Creates a new card and returns it
	 * 
	 * @param front
	 *            : Front Text of Card
	 * @param back
	 *            : Back Text of Card
	 * @param tags
	 *            : List containing tags of new Card
	 * @param frontPic
	 *            : Path of FrontPic
	 * @param backPic
	 *            : Path of BacPic
	 * @return the new Card
	 */
	public Card newCard(String front, String back, List<Tag> tags,
			String frontPic, String backPic) {

		// Create new Card, using the constructor
		Card card = new Card(front, back, frontPic, backPic, tags);

		// Increase Number of Cards in the selected Tags
		if (tags != null) {
			for (Tag tag : tags) {
				tag.increaseTotalCards();
				Database.getInstance().changeTag(tag);
			}
		}

		// Write new Card in DB
		Database.getInstance().addNewCard(card);

		return card;
	}

	/**
	 * Creates a new tag and returns it
	 * 
	 * @param name
	 *            : Tag Name
	 * @return the new Tag
	 */
	public Tag newTag(String name) {

		// Check if Tag Name is already taken
		for (Tag tag : Tag.allTags) {
			if (tag.getTagName().equals(name)) {
				return null;
			}
		}

		// Create the new Tag, write it in DB and return it
		Tag tag = new Tag(name);
		Database.getInstance().addNewTag(tag);
		return tag;
	}

}
