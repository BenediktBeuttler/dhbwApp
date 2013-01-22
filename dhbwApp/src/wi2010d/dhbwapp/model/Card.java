package wi2010d.dhbwapp.model;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Init;

/**
 * A card object
 */
public class Card {

	public static List<Card> allCards = new ArrayList<Card>();
	private static int lastCardID = 0;

	private int cardID;
	private int drawer;
	private int totalStacks = 0;
	private String cardFront;
	private String cardBack;
	private String cardFrontPicture;
	private String cardBackPicture;
	private List<Tag> tags;

	/**
	 * Use this constructor, when loading from DB
	 */
	public Card(int cardID, int drawer, int totalStacks, String cardFront,
			String cardBack, String cardFrontPicture, String cardBackPicture) {
		this.cardID = cardID;
		this.drawer = drawer;
		this.totalStacks = totalStacks;
		this.cardFront = cardFront;
		this.cardBack = cardBack;
		this.cardFrontPicture = cardFrontPicture;
		this.cardBackPicture = cardBackPicture;

		this.tags = new ArrayList<Tag>();

		Card.allCards.add(this);

		if (lastCardID <= cardID) {
			lastCardID = cardID;
		}
	}

	/**
	 * Use this constructor, when creating a new card. Other necessary variables
	 * are set automatically
	 * 
	 * @param cardFront
	 *            The cards' Front text
	 * @param cardBack
	 *            The cards'Back text
	 * @param cardFrontPicture
	 *            The cards'Front Picture Path
	 * @param cardBackPicture
	 *            The cards'Back Picture Path
	 * @param tags
	 */
	public Card(String cardFront, String cardBack, String cardFrontPicture,
			String cardBackPicture, List<Tag> tags) {
		this.cardFront = cardFront;
		this.cardBack = cardBack;
		this.cardFrontPicture = cardFrontPicture;
		this.cardBackPicture = cardBackPicture;
		if (tags == null) {
			this.tags = new ArrayList<Tag>();
		} else
			this.tags = tags;

		this.cardID = Card.getNextCardID();
		this.drawer = 0; // A new card is always in drawer 0
		this.totalStacks = this.increaseTotalStacksInit();

		Card.allCards.add(this);
		Init.dataWritten = true;
	}

	/**
	 * Returns the drawer, the card is currently in.
	 * Drawer 0 = dont know
	 * Drawer 1 = not sure
	 * Drawer 2 = sure
	 * @return the drawer
	 */
	public int getDrawer() {
		return drawer;
	}

	/**
	 * Sets the drawer for the card
	 * @param drawer the new drawer for the card
	 */
	public void setDrawer(int drawer) {
		this.drawer = drawer;
	}

	/**
	 * returns the cards' front text
	 * @return the cards' front text
	 */
	public String getCardFront() {
		return cardFront;
	}

	/**
	 * Sets the the cards' front text
	 * @param cardFront the cards' front text
	 */
	public void setCardFront(String cardFront) {
		this.cardFront = cardFront;
	}

	/**
	 * Returns the cards' back text
	 * @return the cards' back text
	 */
	public String getCardBack() {
		return cardBack;
	}

	/**
	 * Sets the cards' back text
	 * @param cardBack the cards' back text
	 */
	public void setCardBack(String cardBack) {
		this.cardBack = cardBack;
	}

	/**
	 * Returns the path to the cards' front picture, if there's no front picture, an empty string is returned
	 * @return the path to the cards' front picture or an empty string
	 */
	public String getCardFrontPicture() {
		return cardFrontPicture;
	}

	/**
	 * Sets the path to the cards front picture
	 * @param cardFrontPicture the cards' front picture
	 */
	public void setCardFrontPicture(String cardFrontPicture) {
		this.cardFrontPicture = cardFrontPicture;
	}

	/**
	 * Returns the path to the cards' back picture, if there's no back picture, an empty string is returned
	 * @return the path to the cards' back picture or an empty string
	 */
	public String getCardBackPicture() {
		return cardBackPicture;
	}

	/**
	 Sets the path to the cards back picture
	 * @param cardBackPicture the cards' back picture
	 */
	public void setCardBackPicture(String cardBackPicture) {
		this.cardBackPicture = cardBackPicture;
	}

	/**
	 * Returns the unique card id
	 * @return the card id
	 */
	public int getCardID() {
		return cardID;
	}

	/**
	 * Returns the number of stacks the card is currently in
	 * @return the number of stacks the card is currently in
	 */
	public int getTotalStacks() {
		return totalStacks;
	}

	/**
	 * Returns a list with the associated tags
	 * @return list with the associated tags
	 */
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * increases the number of stacks the card is in
	 * @return   the number of stacks the card is in
	 */
	public int increaseTotalStacks() {
		this.totalStacks = this.totalStacks + 1;
		return totalStacks;
	}
	
	/**
	 * increases the number of stacks the card is in
	 * @return   the number of stacks the card is in
	 */
	public int increaseTotalStacksInit() {
		this.totalStacks = this.totalStacks + 1;
		return totalStacks;
	}

	/**
	 * decreases the number of stacks the card is in
	 * @return   the number of stacks the card is in
	 */
	public int decreaseTotalStacks() {
		this.totalStacks = this.totalStacks - 1;
		return totalStacks;
	}

	/**
	 * Gets the next free card ID
	 * @return the next free card ID
	 */
	public static int getNextCardID() {
		lastCardID = lastCardID + 1;
		return lastCardID;
	}

	/**
	 * Sets the tags for this card
	 * @param tags an array list with the tags for this card
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * Checks if the card object equals the given one
	 */
	@Override
	public boolean equals(Object o) {
		Card card = (Card) o;
		if (card.getCardID() == this.cardID) {
			return true;
		}
		return false;
	}

	/**
	 * Resets the Last card id
	 * @return true, if it worked
	 */
	public static boolean resetLastCardID() {
		lastCardID = 0;
		return true;
	}
}
