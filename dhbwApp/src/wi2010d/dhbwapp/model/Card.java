package wi2010d.dhbwapp.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

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
		this.tags = tags;

		this.cardID = Card.getNextCardID();
		this.drawer = 0; // A new card is always in drawer 0
		this.totalStacks = this.increaseTotalStacks();

		Card.allCards.add(this);
	}

	public int getDrawer() {
		return drawer;
	}

	public void setDrawer(int drawer) {
		this.drawer = drawer;
	}

	public String getCardFront() {
		return cardFront;
	}

	public void setCardFront(String cardFront) {
		this.cardFront = cardFront;
	}

	public String getCardBack() {
		return cardBack;
	}

	public void setCardBack(String cardBack) {
		this.cardBack = cardBack;
	}

	public String getCardFrontPicture() {
		return cardFrontPicture;
	}

	public void setCardFrontPicture(String cardFrontPicture) {
		this.cardFrontPicture = cardFrontPicture;
	}

	public String getCardBackPicture() {
		return cardBackPicture;
	}

	public void setCardBackPicture(String cardBackPicture) {
		this.cardBackPicture = cardBackPicture;
	}

	public int getCardID() {
		return cardID;
	}

	public int getTotalStacks() {
		return totalStacks;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public int increaseTotalStacks() {
		this.totalStacks = this.totalStacks + 1;
		return totalStacks;
	}

	public int decreaseTotalStacks() {
		this.totalStacks = this.totalStacks - 1;
		return totalStacks;
	}

	public static int getNextCardID() {
		lastCardID = lastCardID + 1;
		return lastCardID;
	}
	
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public boolean equals(Object o) {
		Card card = (Card) o;
		if (card.getCardID() == this.cardID) {
			return true;
		}
		return false;
	}

}
