package wi2010d.dhbwapp.control;

import java.util.List;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

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
		Database.getInstance().deleteCardStackCorrelation(stack.getStackID(),
				card.getCardID());
		card.decreaseTotalStacks();
		return true;
	}

	/**
	 * Add some one tag to the whole selected stack
	 * 
	 * @param tag
	 * @param stack
	 * @return
	 */
	public boolean addTagToStack(Tag tag, Stack stack) {
		for (Card card : stack.getCards()) {
			card.getTags().add(tag);
			tag.increaseTotalCards();
			Database.getInstance().addCardTagCorrelation(card.getCardID(),
					tag.getTagID());
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

		for (Card card : stack.getCards()) {
			card.setDrawer(0);
			Database.getInstance().changeCard(card);

		}

		return true;
	}
	
	public boolean setDrawer(Card card, int drawer){
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

}
