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
	
	public int getNextCardID()
	{
		lastCardID = lastCardID +1;
		return lastCardID;
	}
	
}
