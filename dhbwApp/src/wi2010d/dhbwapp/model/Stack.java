package wi2010d.dhbwapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import wi2010d.dhbwapp.control.Database;

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

	public static int getNextStackID() {
		lastStackID = lastStackID + 1;
		return lastStackID;
	}

	public boolean isDynamicGenerated() {
		return isDynamicGenerated;
	}

	public void setDynamicGenerated(boolean isDynamicGenerated) {
		this.isDynamicGenerated = isDynamicGenerated;
		Database.getInstance().changeStack(this);
	}

	public int getStackID() {
		return stackID;
	}

	public void setStackID(int stackID) {
		this.stackID = stackID;
		Database.getInstance().changeStack(this);
	}

	public String getStackName() {
		return stackName;
	}

	public void setStackName(String stackName) {
		this.stackName = stackName;
		Database.getInstance().changeStack(this);
	}

	public void setDynamicStackTags(List<Tag> dynamicStackTags) {
		this.dynamicStackTags = dynamicStackTags;
	}

	public int getSure() {
		return sure;
	}

	public void setSure(int sure) {
		this.sure = sure;
		Database.getInstance().changeStack(this);
	}

	public int getNotSure() {
		return notSure;
	}

	public void setNotSure(int notSure) {
		this.notSure = notSure;
		Database.getInstance().changeStack(this);
	}

	public int getDontKnow() {
		return dontKnow;
	}

	public void setDontKnow(int dontKnow) {
		this.dontKnow = dontKnow;
		Database.getInstance().changeStack(this);
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
