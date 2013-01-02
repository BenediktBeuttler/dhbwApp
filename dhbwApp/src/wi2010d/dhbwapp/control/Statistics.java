package wi2010d.dhbwapp.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;

public class Statistics {

	private static Statistics statistics;

	/**
	 * Constructor
	 */
	public Statistics() {
		;
	}

	/**
	 * Singleton Method
	 * 
	 * @return
	 */
	public static Statistics getInstance() {
		if (statistics == null) {
			statistics = new Statistics();
		}

		return statistics;
	}

	// ----------------Return data for last Review screen--------------

	/**
	 * Method that returns name of the stack with the last runthrough
	 * 
	 * @return
	 */
	public String getLastRunthroughName() {
		Runthrough lastRunthrough = getLastRunthrough();
		if (lastRunthrough != null) {
			for (Stack stack : Stack.allStacks) {
				if (stack.getStackID() == lastRunthrough.getStackID()) {
					return stack.getStackName();
				}
			}

			return "No Data available";
		} else {
			return "No Data available";
		}
	}

	/**
	 * Returns date of last Runthrough
	 * 
	 * @return
	 */
	public String getLastRunthroughDate() {
		String date;
		Runthrough lastRunthrough = getLastRunthrough();
		if (lastRunthrough != null) {
			SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
			date = sd.format(lastRunthrough.getEndDate());
			return date;
		} else {
			return "No Data available";
		}
	}

	/**
	 * Returns duration of last Runthrough
	 * 
	 * @return
	 */
	public String getDurationOfLastRunthrough() {
		Runthrough lastRunthrough = getLastRunthrough();

		if (lastRunthrough != null) {
			return this
					.convertSecondsToString(lastRunthrough.getDurationSecs());
		} else {
			return "No Data available";
		}
	}

	/**
	 * returns Array with status of stack before the last runthrough
	 * 
	 * @return
	 */
	public String[] getStatusBefore() {
		String[] statusBefore = { "0", "0", "0" };
		Runthrough lastRunthrough = getLastRunthrough();

		if (lastRunthrough != null) {
			int[] statusBeforeInt = lastRunthrough.getStatusBefore();

			statusBefore[0] = "" + statusBeforeInt[0];
			statusBefore[1] = "" + statusBeforeInt[1];
			statusBefore[2] = "" + statusBeforeInt[2];
		} else {
			statusBefore[0] = "No Data available";
			statusBefore[1] = "No Data available";
			statusBefore[2] = "No Data available";
		}

		return statusBefore;
	}

	/**
	 * returns Array with the status of stack after last runthrough
	 * 
	 * @return
	 */
	public String[] getStatusAfter() {
		String[] statusAfter = { "0", "0", "0" };
		Runthrough lastRunthrough = getLastRunthrough();

		if (lastRunthrough != null) {
			int[] statusAfterInt = lastRunthrough.getStatusAfter();

			statusAfter[0] = "" + statusAfterInt[0];
			statusAfter[1] = "" + statusAfterInt[1];
			statusAfter[2] = "" + statusAfterInt[2];
		} else {
			statusAfter[0] = "No Data available";
			statusAfter[1] = "No Data available";
			statusAfter[2] = "No Data available";
		}

		return statusAfter;
	}

	/**
	 * Identifies and returns the last Runthrough
	 * 
	 * @return
	 */
	private Runthrough getLastRunthrough() {
		int lastID = Runthrough.getLastRunthroughID();
		List<Runthrough> allRunthroughs = Runthrough.allRunthroughs;

		
		for (Runthrough runthrough : allRunthroughs){
			if (runthrough.getRunthroughID() == lastID){
				return runthrough;
			}
		}
		
		
		/*
		for (int i = (allRunthroughs.size() - 1); i == 0; i--) {
			if (allRunthroughs.get(i).getRunthroughID() == lastID) {
				if (allRunthroughs.get(i).isOverall()) {
					lastID = lastID - 1;
					i = allRunthroughs.size();
				} else {
					return allRunthroughs.get(i);
				}
			}
		}
		*/
		

		return null;
	}

	// ---------------Returns Data for Overall Statistic Screen------------

	public String getTotalNumberOfCards() {
		return "" + Card.allCards.size();
	}

	public String getTotalNumberOfCards(String name) {
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				return "" + stack.getCards().size();
			}
		}

		return "";
	}

	// ----------------Return Duration-------------------

	/**
	 * adds the duration of all overall Runthroughs to calculate the total time
	 * 
	 * @return
	 */
	public String getOverallDuration() {
		int duration = 0;

		for (Stack stack : Stack.allStacks) {
			duration = duration
					+ stack.getOverallRunthrough().getDurationSecs();
		}

		return this.convertSecondsToString(duration);
	}

	/**
	 * returns overall duration of selected stack
	 * 
	 * @param name
	 * @return
	 */
	public String getStackOverallDuration(String name) {
		int duration = 0;

		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				duration = stack.getOverallRunthrough().getDurationSecs();
				break;
			}
		}

		return this.convertSecondsToString(duration);

	}

	/**
	 * Converts seconds into String (??? hours, ??? minutes, ??? seconds)
	 * 
	 * @param duration
	 * @return
	 */
	private String convertSecondsToString(int duration) {
		int seconds = 0;
		int minutes = 0;
		int hours = 0;

		seconds = duration % 60;
		minutes = duration / 60;
		hours = minutes / 60;
		minutes = minutes & 60;

		if (hours > 0) {
			return minutes + " min, " + seconds + " sec";
		} else {
			return hours + " h, " + minutes + " min, " + seconds
					+ " sec";
		}
	}

	// ----------------Return Data for Progress Screen------------
	
	/**
	 * Get last runthrough dates of all Stacks
	 * 
	 * @return
	 */
	public ArrayList<String> getLastRunthroughDates(){
		
		List<String> lastDates = new ArrayList<String>();
		SimpleDateFormat formater = new SimpleDateFormat();
		
		for (Runthrough runthrough : getLastRunthroughs()){
			lastDates.add(formater.format(runthrough.getEndDate()));
		}
		
		return (ArrayList<String>) lastDates;
	}
	
	/**
	 * Get last runthroughs dates of selected Stack (name)
	 * 
	 * @param name
	 * @return
	 */
	public ArrayList<String> getLastRunthroughDates(String name){
		
		List<String> lastDates = new ArrayList<String>();
		SimpleDateFormat formater = new SimpleDateFormat();
		
		for (Runthrough runthrough : getLastRunthroughs(name)){
			lastDates.add(formater.format(runthrough.getEndDate()));
		}
		
		return (ArrayList<String>) lastDates;
	}
	
	/**
	 * Method returns list of strings with the last progress (+/- change for selected drawer)
	 * drawer = 0 = dont Know, drawer = 1 = not Sure, drawer = 2 = sure
	 * 
	 * @param drawer
	 * @return
	 */
	public ArrayList<String> getLastProgress(int drawer){
		
		List<String> progress = new ArrayList<String>();
		int difference;
		int[] statusBefore = new int[3];
		int[] statusAfter = new int[3];
		
		for (Runthrough runthrough : getLastRunthroughs()){
			statusBefore = runthrough.getStatusBefore();
			statusAfter = runthrough.getStatusAfter();
			difference = statusAfter[drawer] - statusBefore[drawer];
			progress.add("" + difference);
		}
		
		return (ArrayList<String>) progress;
	}
	
	
	
	
	/**
	 * Get last runthroughs of all Stacks
	 * @return
	 */
	private List<Runthrough> getLastRunthroughs(){
		
		List<Runthrough> lastRunthroughs = new ArrayList<Runthrough>();
		
		for (Stack stack : Stack.allStacks){
			for (Runthrough runthrough : stack.getLastRunthroughs()){
				lastRunthroughs.add(runthrough);
			}
		}
		
		return lastRunthroughs;
	}
	
	/**
	 * Get last runthroughs of selected Stack (name)
	 * @param name
	 * @return
	 */
	private List<Runthrough> getLastRunthroughs(String name){
		
		List<Runthrough> lastRunthroughs = new ArrayList<Runthrough>();
		
		for (Stack stack : Stack.allStacks){
			
			if (stack.getStackName().equals(name)){
			
				for (Runthrough runthrough : stack.getLastRunthroughs()){
					lastRunthroughs.add(runthrough);
				}
				
			}
				
		}
		
		return lastRunthroughs;
		
	}

	// ----------------Return Status of Drawers-------------------

	/**
	 * Returns the Actual Status of all Stacks [0] = dontKnow, [1] = notSure,
	 * [2] = sure
	 * 
	 * @return
	 */
	public String[] getOverallActualDrawerStatus() {
		int actualStatus[] = { 0, 0, 0 };
		int statusCard;

		for (Card card : Card.allCards) {
			statusCard = card.getDrawer();

			switch (statusCard) {
			case 0:
				actualStatus[0] = actualStatus[0] + 1;
				break;
			case 1:
				actualStatus[1] = actualStatus[1] + 1;
				break;
			case 2:
				actualStatus[2] = actualStatus[2] + 1;
			}
		}

		return createActualDrawerStatusString(actualStatus);
	}

	/**
	 * Returns the actual status of selected stack [0] = dontKnow, [1] =
	 * notSure, [2] = sure
	 * 
	 * @param name
	 *            = name of selected stack
	 * @return
	 */
	public String[] getActualDrawerStatus(String name) {
		
		int statusCard;
		int actualStatus[] = { 0, 0, 0 };

		for (Stack stack : Stack.allStacks) {
			
			if (stack.getStackName().equals(name)) {
				
				for (Card card : stack.getCards()){
					statusCard = card.getDrawer();

					switch (statusCard) {
					case 0:
						actualStatus[0] = actualStatus[0] + 1;
						break;
					case 1:
						actualStatus[1] = actualStatus[1] + 1;
						break;
					case 2:
						actualStatus[2] = actualStatus[2] + 1;
					}
				}
				break;
			}
		}

		return createActualDrawerStatusString(actualStatus);
	}

	/**
	 * Creates String that contains the status of the drawer
	 * 
	 * @param actualStatus
	 * @return
	 */
	private String[] createActualDrawerStatusString(int[] actualStatus) {
		String[] actualDrawerStatus = new String[3];

		for (int i = 0; i < 3; i++) {
			actualDrawerStatus[i] = "" + actualStatus[i];
		}

		return actualDrawerStatus;
	}

}
