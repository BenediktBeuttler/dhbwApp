package wi2010d.dhbwapp.model;

import java.util.List;

public class Card {
	
	public static List<Card> allCards;
	private static int lastCardID=0;
	
	private int cardID;
	private int drawer;
	private int totalStacks;
	private String cardName;
	private String cardFront;
	private String cardBack;
	private String cardFrontPicture;
	private String cardBackPicture;
	private List<Tag> tags;
	
	
	/**
	 * Use this constructor, when loading from DB
	 */
	public Card(int cardID, int drawer, int totalStacks, String cardName,
			String cardFront, String cardBack, String cardFrontPicture,
			String cardBackPicture) {
		this.cardID = cardID;
		this.drawer = drawer;
		this.totalStacks = totalStacks;
		this.cardName = cardName;
		this.cardFront = cardFront;
		this.cardBack = cardBack;
		this.cardFrontPicture = cardFrontPicture;
		this.cardBackPicture = cardBackPicture;
		
		Card.allCards.add(this);
	}
	
	


	/**
	 * Use this constructor, when creating a new card
	 * Other necessary variables are set automatically
	 * 
	 * @param cardName The cards' Name
	 * @param cardFront The cards' Front text
	 * @param cardBack The cards'Back text
	 * @param cardFrontPicture The cards'Front Picture Path
	 * @param cardBackPicture The cards'Back Picture Path
	 * @param tags
	 */
	public Card(String cardName, String cardFront, String cardBack,
			String cardFrontPicture, String cardBackPicture, List<Tag> tags) {
		this.cardName = cardName;
		this.cardFront = cardFront;
		this.cardBack = cardBack;
		this.cardFrontPicture = cardFrontPicture;
		this.cardBackPicture = cardBackPicture;
		this.tags = tags;
		
		this.cardID = Card.getNextCardID();
		this.drawer = 0; //A new card is always in drawer 0
		this.totalStacks = this.increaseTotalStacks();
		
		Card.allCards.add(this);
	}


	
	public int increaseTotalStacks()
	{
		this.totalStacks = this.totalStacks +1;
		return totalStacks;
	}

	public int decreaseTotalStacks() {
		this.totalStacks = this.totalStacks - 1;
		return totalStacks;
	}

	public static int getNextCardID()
	{
		lastCardID = lastCardID +1;
		return lastCardID;
	}
	
}
