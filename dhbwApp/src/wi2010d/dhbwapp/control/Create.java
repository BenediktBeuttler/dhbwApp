package wi2010d.dhbwapp.control;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class Create {

	public boolean newStack(String name, Card card)
	{
		List<Card> cards = new ArrayList<Card>();
		cards.add(card);
		new Stack(false, name, cards);
		return true;
		// TODO: insert into db
	}
	
	public boolean newDynStack(String name, List<Tag> tags)
	{
		for (Card card : Card.allCards)
		{
			
		}
		new Stack(true, )
	}
	
	public boolean updateDynStack()
	{
		
	}
	
	public Card newCard(String name, String front, String back, List<Tag> tags, String frontPic, String backPic)
	{
		
	}
	
	public Tag newTag(String name)
	{
		
	}
	
	public Runthrough newRunthrough(int sureBefore, int notSureBefore, int dontKnowBefore)
	{
		
	}
}
