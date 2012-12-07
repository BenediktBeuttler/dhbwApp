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
	
	
	private void addLastRunthrough(Runthrough run)
	{
		
	}
	
	public int getNextStackID(){
		lastStackID = lastStackID+1;
		return lastStackID;
	}
}
