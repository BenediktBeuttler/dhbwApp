package wi2010d.dhbwapp.model;

import java.util.List;

public class Stack {

	public static List<Stack> allStacks;
	public static Stack dynamicStack;
	private static int lastStackID = 0;
	
	private boolean isDynamicGenerated;
	private int stackID;
	private String stackName;
	private int sure;
	private int notSure;
	private int dontKnow;
	private Runthrough overallRunthrough;
	private List<Runthrough> lastRunthroughs;
	private List<Card> cards;
	
	
	
	/**
	 * Use this constructor when loading from DB
	 */
	public Stack(boolean isDynamicGenerated, int stackID, String stackName,
			int sure, int notSure, int dontKnow) {
		this.isDynamicGenerated = isDynamicGenerated;
		this.stackID = stackID;
		this.stackName = stackName;
		this.sure = sure;
		this.notSure = notSure;
		this.dontKnow = dontKnow;
		
		Stack.allStacks.add(this);
	}
	
	

	/**
	 * Use this constructor when creating new stack
	 * @param isDynamicGenerated
	 * @param stackName
	 * @param overallRunthrough
	 * @param cards
	 */
	public Stack(boolean isDynamicGenerated, String stackName,
			Runthrough overallRunthrough, List<Card> cards) {
		this.isDynamicGenerated = isDynamicGenerated;
		this.stackName = stackName;
		this.overallRunthrough = overallRunthrough;
		this.cards = cards;
		
		this.stackID = Stack.getNextStackID();
		
		Stack.allStacks.add(this);
	}



	private void addLastRunthrough(Runthrough run)
	{
		if(lastRunthroughs.size()>9)
		{
			lastRunthroughs.remove(0);
		}
		lastRunthroughs.add(run);
	}
	
	public static int getNextStackID(){
		lastStackID = lastStackID+1;
		return lastStackID;
	}
}
