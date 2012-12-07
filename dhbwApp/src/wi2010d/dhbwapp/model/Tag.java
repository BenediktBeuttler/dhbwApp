package wi2010d.dhbwapp.model;

import java.util.List;

public class Tag {
	
	public static List<Tag> allTags;
	public static int lastTagID;
	
	private int tagID;
	private int totalCards;
	private String tagName;
	
	
	public int increaseTotalCards()
	{
		totalCards = totalCards + 1;
		return totalCards;
	}
	
	public int decreaseTotalCards()
	{
		
		totalCards = totalCards -1;
		return totalCards;
	}
	
	public static int getNextTagID()
	{
		lastTagID  = lastTagID + 1;
		return lastTagID;
	}
	
}
