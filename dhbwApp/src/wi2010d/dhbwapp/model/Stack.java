package wi2010d.dhbwapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import wi2010d.dhbwapp.control.Database;

/**
 * Represents a stack with cards.
 *
 */
public class Stack {

	public static List<Stack> allStacks = new ArrayList<Stack>();
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

		this.lastRunthroughs = new ArrayList<Runthrough>();
		this.dynamicStackTags = new ArrayList<Tag>();
		this.cards = new ArrayList<Card>();

		if (lastStackID <= stackID) {
			lastStackID = stackID;
		}

		Stack.allStacks.add(this);
	}

	/**
	 * Use this constructor when creating new stack
	 * 
	 * @param isDynamicGenerated
	 * @param stackName
	 * @param cards
	 * 
	 *            This constructor also creates a new overall Runthrough
	 */
	public Stack(boolean isDynamicGenerated, String stackName, List<Card> cards) {

		this.isDynamicGenerated = isDynamicGenerated;
		this.stackName = stackName;
		this.cards = cards;
		this.lastRunthroughs = new ArrayList<Runthrough>();

		this.stackID = Stack.getNextStackID();

		int statusBefore[] = { 1, 0, 0 };

		// if stack is dynamic, the statusBefore for the overall Runthrough
		// must
		// be identified
		if (isDynamicGenerated) {
			statusBefore[0] = 0;
			for (Card card : cards) {
				int drawer = card.getDrawer();
				switch (drawer) {
				case 0:
					statusBefore[0] = statusBefore[0] + 1;
				case 1:
					statusBefore[1] = statusBefore[1] + 1;
				case 2:
					statusBefore[2] = statusBefore[2] + 1;
				}
			}
		}

		this.overallRunthrough = new Runthrough(this.stackID, true,
				statusBefore);

		Stack.allStacks.add(this);
	}

	/**
	 * Adds an runthrough to this stack. If there are already 10 stacks, the oldest one is deleted
	 * @param run the runthrough
	 */
	public void addLastRunthrough(Runthrough run) {
		Runthrough overallRunthrough = this.getOverallRunthrough();
		int actualStatus[] = run.getStatusAfter();
		
		long durationMilliSecs = run.getEndDate().getTime()
				- run.getStartDate().getTime();

		int duration = (int) TimeUnit.MILLISECONDS.toSeconds(durationMilliSecs);
		run.setDurationSecs(duration);

		overallRunthrough.setDurationSecs(
				duration + overallRunthrough.getDurationSecs());
		
		overallRunthrough.setStatusAfter(actualStatus[0], actualStatus[1], actualStatus[2]);

		if (lastRunthroughs.size() > 9) {
			lastRunthroughs.remove(0);
		}
		
		lastRunthroughs.add(run);
		
		Database.getInstance().changeRunthrough(run);
		Database.getInstance().changeRunthrough(overallRunthrough);
	}
	
	
	/**
	 * Adds an runthrough to this stack. If there are already 10 stacks, the oldest one is deleted
	 * @param run the runthrough
	 */
	public void initAddLastRunthrough(Runthrough run) {
		Runthrough overallRunthrough = this.getOverallRunthrough();
		int actualStatus[] = run.getStatusAfter();
		
		long durationMilliSecs = run.getEndDate().getTime()
				- run.getStartDate().getTime();

		int duration = (int) TimeUnit.MILLISECONDS.toSeconds(durationMilliSecs);
		run.initSetDurationSecs(duration);

		overallRunthrough.initSetDurationSecs(
				duration + overallRunthrough.getDurationSecs());
		
		overallRunthrough.initSetStatusAfter(actualStatus[0], actualStatus[1], actualStatus[2]);

		if (lastRunthroughs.size() > 9) {
			lastRunthroughs.remove(0);
		}
		
		lastRunthroughs.add(run);
	}

	/**
	 * @return The next stack ID
	 */
	public static int getNextStackID() {
		lastStackID = lastStackID + 1;
		return lastStackID;
	}

	/**
	 * @return true, if the stack is dynamic generated
	 */
	public boolean isDynamicGenerated() {
		return isDynamicGenerated;
	}

	/**
	 * @return The stacks' id
	 */
	public int getStackID() {
		return stackID;
	}

	/**
	 * @return The stacks' name
	 */
	public String getStackName() {
		return stackName;
	}

	/**
	 * Sets the stacks' name
	 * @param stackName the stacks' name
	 */
	public void setStackName(String stackName) {
		this.stackName = stackName;
	}

	/**
	 * Sets the list with tags for the dynamicstack
	 * @param dynamicStackTags the list with tags
	 */
	public void setDynamicStackTags(List<Tag> dynamicStackTags) {
		this.dynamicStackTags = dynamicStackTags;
	}

	/**
	 * @return the number of cards in the sure drawer
	 */
	public int getSure() {
		return sure;
	}

	/**
	 * sets the number of cards in the sure drawer
	 * @param sure the number of cards in the sure drawer
	 */
	public void setSure(int sure) {
		this.sure = sure;
	}

	/**
	 * @return the number of cards in the not sure drawer
	 */
	public int getNotSure() {
		return notSure;
	}

	/**
	 * Sets the number of cards in the not sure drawer
	 * @param notSure the number of cards in the not sure drawer
	 */
	public void setNotSure(int notSure) {
		this.notSure = notSure;
	}

	/**
	 * @return the number of cards in the dont know drawer
	 */
	public int getDontKnow() {
		return dontKnow;
	}

	/**
	 * Sets the number of cards in the dont know drawer
	 * @param dontKnow the number of cards in the dont know drawer
	 */
	public void setDontKnow(int dontKnow) {
		this.dontKnow = dontKnow;
	}

	/**
	 * @return the overall runthrough associated with this stack
	 */
	public Runthrough getOverallRunthrough() {
		return overallRunthrough;
	}

	/**
	 * Sets the overall runthrough associated with this stack
	 * @param overallRunthrough the overall runthrough associated with this stack
	 */
	public void setOverallRunthrough(Runthrough overallRunthrough) {
		this.overallRunthrough = overallRunthrough;
	}

	/**
	 * @return The list with cards' associated with this stack
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * @return The list with last runthroughs
	 */
	public List<Runthrough> getLastRunthroughs() {
		return lastRunthroughs;
	}

	/**
	 * @return The list with tags for this dynamic stack
	 */
	public List<Tag> getDynamicStackTags() {
		return dynamicStackTags;
	}

	/**
	 * Resets the Stack ID Counter
	 * @return true, if it worked
	 */
	public static boolean resetLastStackID() {
		lastStackID = 0;
		return true;
	}

	@Override
	public boolean equals(Object o) {
		Stack stack = (Stack) o;
		if (stack.getStackID() == this.stackID) {
			return true;
		}
		return false;
	}
}
