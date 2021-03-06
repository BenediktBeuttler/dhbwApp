package wi2010d.dhbwapp.control;

import java.util.List;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

/**
 * Provides methods to Edit the objects, writes them to to DB after changing.
 */
public class Edit {

	private static Edit edit;

	/**
	 * Constructor
	 */
	public Edit() {
		;
	}

	/**
	 * Singleton Method
	 */
	public static Edit getInstance() {
		if (edit == null) {
			edit = new Edit();
		}

		return edit;
	}

	/**
	 * Add a selected card to existing stack
	 * 
	 * @param stack
	 * @param card
	 * @return
	 */
	public boolean addCardToStack(Stack stack, Card card) {
		stack.getCards().add(card);
		card.increaseTotalStacks();
		Database.getInstance().changeCard(card);
		Database.getInstance().addStackCardCorrelation(stack.getStackID(),
				card.getCardID());
		return true;
	}

	/**
	 * Removes card from stack
	 * 
	 * @param stack
	 * @param card
	 * @return
	 */
	public boolean removeCardFromStack(Stack stack, Card card) {
		stack.getCards().remove(card);
		card.decreaseTotalStacks();
		Database.getInstance().changeCard(card);
		Database.getInstance().deleteCardStackCorrelation(stack.getStackID(),
				card.getCardID());
		return true;
	}

	/**
	 * Add one tag to the whole selected stack
	 * 
	 * @param tag
	 * @param stack
	 * @return
	 */
	public boolean addTagToStack(Tag tag, Stack stack) {
		for (Card card : stack.getCards()) {
			if (!card.getTags().contains(tag)){
				card.getTags().add(tag);
				tag.increaseTotalCards();
				Database.getInstance().addCardTagCorrelation(card.getCardID(),
						tag.getTagID());
			}
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
	public boolean addTagToCard(List<Tag> tags, Card card) {
		for (Tag tag : tags) {
			card.getTags().add(tag);
			tag.increaseTotalCards();
			Database.getInstance().addCardTagCorrelation(card.getCardID(),
					tag.getTagID());
		}
		return true;
	}

	/**
	 * Change name of selected stack
	 * 
	 * @param name
	 * @param stack
	 * @return
	 */
	public boolean changeStackName(String name, Stack stack) {
		stack.setStackName(name);
		return Database.getInstance().changeStack(stack);
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
	public boolean changeCard(String front, String back, String frontPic,
			String backPic, List<Tag> tags, Card card) {
		card.setCardFront(front);
		card.setCardBack(back);
		card.setCardFrontPicture(frontPic);
		card.setCardBackPicture(backPic);

		for (Tag tag : card.getTags()) {
			tag.decreaseTotalCards();
			Database.getInstance().deleteCardTagCorrelation(card.getCardID(),
					tag.getTagID());
			Database.getInstance().changeTag(tag);
		}

		for (Tag tag : tags) {
			tag.increaseTotalCards();
			Database.getInstance().addCardTagCorrelation(card.getCardID(),
					tag.getTagID());
			Database.getInstance().changeTag(tag);
		}

		card.setTags(tags);

		return Database.getInstance().changeCard(card);
	}

	/**
	 * Reset Drawer of all cards of a selected stack
	 * 
	 * @param stack
	 * @return
	 */
	public boolean resetDrawer(Stack stack) {
		stack.setDontKnow(stack.getCards().size());
		stack.setNotSure(0);
		stack.setSure(0);
		Database.getInstance().changeStack(stack);

		for (Card card : stack.getCards()) {
			card.setDrawer(0);
			Database.getInstance().changeCard(card);

		}

		return true;
	}

	public boolean setDrawer(Card card, int drawer) {
		card.setDrawer(drawer);
		Database.getInstance().changeCard(card);
		return true;
	}

	/**
	 * Edit tag name
	 * 
	 * @param name
	 * @param tag
	 * @return
	 */
	public boolean editTag(String name, Tag tag) {
		tag.setTagName(name);
		return Database.getInstance().changeTag(tag);
	}

	/**
	 * Method to add a new picture to card front: false = back, true = front
	 * 
	 * @param front
	 * @param path
	 * @param card
	 * @return
	 */
	public boolean addNewPicToCard(boolean front, String path, Card card) {
		if (front) {
			card.setCardFrontPicture(path);
			Database.getInstance().changeCard(card);
			return true;
		} else {
			card.setCardBackPicture(path);
			Database.getInstance().changeCard(card);
			return true;
		}
	}
	
	/**
	 * Deletes Picture from selected Card in Card and DB
	 * 
	 * @param front: boolean, whether the front (true) or back (false) picture is to be deleted
	 * @param card: selected card
	 * @return boolean - true if the operation has worked
	 */
	public boolean deletePicFromCard(boolean front, Card card){
		
		// Evaluate whether to delete front (true) or back (false) Picture of the card
		if (front){
			card.setCardFrontPicture("");
			Database.getInstance().changeCard(card);
			return true;
		}else{
			card.setCardBackPicture("");
			Database.getInstance().changeCard(card);
			return true;
		}
	}

}
