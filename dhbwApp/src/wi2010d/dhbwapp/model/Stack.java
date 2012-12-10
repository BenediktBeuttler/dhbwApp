package wi2010d.dhbwapp.model;

import java.util.ArrayList;
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
	private List<Tag> dynamicStackTags;
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
	 * 
	 * @param isDynamicGenerated
	 * @param stackName
	 * @param cards
	 * 
	 * This constructor also creates a new overall Runthrough
	 */
	public Stack(boolean isDynamicGenerated, String stackName,
			List<Card> cards) {
		this.isDynamicGenerated = isDynamicGenerated;
		this.stackName = stackName;
		this.cards = cards;

		this.stackID = Stack.getNextStackID();
		
		//if stack is dynamic, the statusBefore for the overall Runthrough must be identified
		if (isDynamicGenerated)
		{
			for (card : cards)
			{
				
			}
		}
		else
		{
			int statusBefore[] = {1,0,0};
		}
			
		this.overallRunthrough = new Runthrough(this.stackID, true, statusBefore);

		Stack.allStacks.add(this);

	}

	public void addLastRunthrough(Runthrough run) {
		if (lastRunthroughs.size() > 9) {
			lastRunthroughs.remove(0);
		}
		lastRunthroughs.add(run);
	}

	public static int getNextStackID() {
		lastStackID = lastStackID + 1;
		return lastStackID;
	}

	public boolean isDynamicGenerated() {
		return isDynamicGenerated;
	}

	public void setDynamicGenerated(boolean isDynamicGenerated) {
		this.isDynamicGenerated = isDynamicGenerated;
	}

	public int getStackID() {
		return stackID;
	}

	public void setStackID(int stackID) {
		this.stackID = stackID;
	}

	public String getStackName() {
		return stackName;
	}

	public void setStackName(String stackName) {
		this.stackName = stackName;
	}

	public int getSure() {
		return sure;
	}

	public void setSure(int sure) {
		this.sure = sure;
	}

	public int getNotSure() {
		return notSure;
	}

	public void setNotSure(int notSure) {
		this.notSure = notSure;
	}

	public int getDontKnow() {
		return dontKnow;
	}

	public void setDontKnow(int dontKnow) {
		this.dontKnow = dontKnow;
	}

	public Runthrough getOverallRunthrough() {
		return overallRunthrough;
	}

	public void setOverallRunthrough(Runthrough overallRunthrough) {
		this.overallRunthrough = overallRunthrough;
	}

	public List<Card> getCards() {
		return cards;
	}

	public List<Runthrough> getLastRunthroughs() {
		return lastRunthroughs;
	}

	public List<Tag> getDynamicStackTags() {
		return dynamicStackTags;
	}

}
