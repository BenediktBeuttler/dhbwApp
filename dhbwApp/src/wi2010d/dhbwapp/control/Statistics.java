package wi2010d.dhbwapp.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

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
	 * @return Class Statistics
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
	 * @return name of stack (with the newest runthrough)
	 */
	public String getLastRunthroughName() {
		
		//Get last Runthrough
		Runthrough lastRunthrough = getLastRunthrough();
		
		if (lastRunthrough != null) {
			for (Stack stack : Stack.allStacks) {
				//Compare IDs (Stack ID and Stack ID stored in Runthrough to identify relevant Stack
				if (stack.getStackID() == lastRunthrough.getStackID()) {
					return stack.getStackName();
				}
			}
			// Return "No Data available" if there is no match
			return "No Data available";
		} else {
			
			// Return "No Data available" if there is no last Runthrough
			return "No Data available";
		}
	}

	/**
	 * Returns date of last Runthrough
	 * 
	 * @return String, containing the date of the last runthrough (formatted)
	 */
	public String getLastRunthroughDate() {
		String date;
		
		// Get last Runthrough
		Runthrough lastRunthrough = getLastRunthrough();
		
		if (lastRunthrough != null) {
			// If there is a last Runthrough, get and return End Date
			SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
			date = sd.format(lastRunthrough.getEndDate());
			return date;
		} else {
			// If there is no last Runthrough, return "No Data available"
			return "No Data available";
		}
	}

	/**
	 * Returns duration of last Runthrough
	 * 
	 * @return String, containing the duration of last runthrough (formatted)
	 */
	public String getDurationOfLastRunthrough() {
		
		// Get last runthrough
		Runthrough lastRunthrough = getLastRunthrough();

		if (lastRunthrough != null) {
			// If there is an existing last Runthrough
			// Get Seconds of last Runthrough, convert them into String and return this one
			return this
					.convertSecondsToString(lastRunthrough.getDurationSecs());
		} else {
			
			// If there is no existing last Runthrough, return "No Data available"
			return "No Data available";
		}
	}

	/**
	 * Returns Array with status of Stack before the last runthrough
	 * 
	 * @return status of Stack before last Runthrough
	 */
	public String[] getStatusBefore() {
		
		String[] statusBefore = { "0", "0", "0" };
		
		//Get last Runthrough
		Runthrough lastRunthrough = getLastRunthrough();

		if (lastRunthrough != null) {
			// If there is any last Runthrough, get statusBefore
			int[] statusBeforeInt = lastRunthrough.getStatusBefore();

			statusBefore[0] = "" + statusBeforeInt[0];
			statusBefore[1] = "" + statusBeforeInt[1];
			statusBefore[2] = "" + statusBeforeInt[2];
		} else {
			// If there is no last Runthrough
			statusBefore[0] = "No Data available";
			statusBefore[1] = "No Data available";
			statusBefore[2] = "No Data available";
		}

		return statusBefore;
	}

	/**
	 * Returns Array with the Status of stack after last runthrough
	 * 
	 * @return status of stack (with last Runthrough)
	 */
	public String[] getStatusAfter() {
		
		String[] statusAfter = { "0", "0", "0" };
		
		//Get last Runthrough
		Runthrough lastRunthrough = getLastRunthrough();

		if (lastRunthrough != null) {
			// If there is any existing last Runthrough, get statusAfter
			int[] statusAfterInt = lastRunthrough.getStatusAfter();

			statusAfter[0] = "" + statusAfterInt[0];
			statusAfter[1] = "" + statusAfterInt[1];
			statusAfter[2] = "" + statusAfterInt[2];
		} else {
			// If there is no existing last Runthrough
			statusAfter[0] = "No Data available";
			statusAfter[1] = "No Data available";
			statusAfter[2] = "No Data available";
		}

		return statusAfter;
	}

	/**
	 * Identifies and returns the last Runthrough
	 * 
	 * @return last Runthrough
	 */
	private Runthrough getLastRunthrough() {
		
		// Get all runthroughs
		List<Runthrough> allRunthroughs = Runthrough.allRunthroughs;
		Runthrough lastRunthrough = null;
		
		//Search first non-overall runthrough in list
		for (Runthrough runthrough : allRunthroughs){
			if (!runthrough.isOverall()){
				lastRunthrough = runthrough;
				break;
				// Then break, first existing non-overall Runthrough = parameter lastRunthrough
			}
		}
		
		//Compare date of all runthroughs and update result
		for (Runthrough runthrough : allRunthroughs){
			//Just for the Runthroughs that are not overall Runthroughs
			if (!runthrough.isOverall()){
				// If the next Stack newer, take this one
				if (runthrough.getEndDate().getTime() > lastRunthrough.getEndDate().getTime()){
					lastRunthrough = runthrough;
				}
			}
		}
		

		return lastRunthrough;
	}

	// ---------------Returns Data for Overall Statistic Screen------------

	/**
	 * Method that returns the total Number of Cards (used, if "All Stacks" is selected in 
	 * Statistic Screen
	 * 
	 * @return String containing the total number of cards
	 */
	public String getTotalNumberOfCards() {
		return "" + Card.allCards.size();
	}

	/**
	 * Method that returns the total Number of Cards from selected Stack (used from Statistic Screen)
	 * 
	 * @param name selected Stack name
	 * @return String containing the total number of cards (of selected stack)
	 */
	public String getTotalNumberOfCards(String name) {
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				// Compare selected Stack Name
				return "" + stack.getCards().size();
			}
		}

		return "";
	}

	// ----------------Return Duration-------------------

	/**
	 * Adds the duration of all overall Runthroughs to calculate the total time (all Stacks)
	 * 
	 * @return String, containing the overall duration of all stacks
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
	 * Returns overall duration of selected stack (Stack Name = param name)
	 * 
	 * @param name selected Stack name
	 * @return String, containing the formatted overall duration of selected Stack
	 */
	public String getStackOverallDuration(String name) {
		int duration = 0;

		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(name)) {
				// Get relevant Stack (by comparing Stack Names), get duration and return it
				duration = stack.getOverallRunthrough().getDurationSecs();
				break;
			}
		}

		return this.convertSecondsToString(duration);

	}

	/**
	 * Converts seconds into String (??? hours, ??? minutes, ??? seconds)
	 * OR if hours = 0 (??? minutes, ??? seconds)
	 * 
	 * @param duration containing int with duration in seconds
	 * @return String containing the converted duration as shown above
	 */
	private String convertSecondsToString(int duration) {
		int seconds = 0;
		int minutes = 0;
		int hours = 0;

		// Get seconds
		seconds = duration % 60;
		
		// Get miutes
		minutes = duration / 60;
		
		// Get hours, if there are more than 60 minutes
		if (minutes >= 60){
			hours = minutes / 60;
			minutes = minutes % 60;
		}
		
		// Create String depending on duration (>0h with hours)
		if (hours <= 0) {
			return minutes + " min, " + seconds + " sec";
		} else {
			return hours + " h, " + minutes + " min, " + seconds
					+ " sec";
		}
	}

	// ----------------Return Data for Progress Screen------------

	/**
	 * Returns number of Runthroughs for selected stack
	 * 
	 * @param name Stack name
	 * @return int number of Runthroughs
	 */
	public int getNumberOfRunthroughs(String name){
		
		for (Stack stack : Stack.allStacks){
			if (stack.getStackName().equals(name)){
				// Get relevant Stack, get number of Runthroughs
				return stack.getLastRunthroughs().size();
			}
		}
		
		return 0;
	}
	
	
	/**
	 * Get last Runthrough Dates of selected Stack
	 * 
	 * @param name selected Stack name
	 * @return ArrayList with last Runthrough Dates (formatted)
	 */
	public long[] getLastRunthroughDatesAsDate(String name){
				
		long[] lastDates = new long[10];
		int i=0;
		
		// Get last Runthroughs of selected Stack
		for (Runthrough runthrough : getLastRunthroughs(name)){
			// Add the End Date to the result
			lastDates[i]=runthrough.getEndDate().getTime();
			i++;
		}
		
		return lastDates;
	}
	
	/**
	 * Get last Runthrough Dates of selected Stack
	 * 
	 * @param name selected Stack name
	 * @return ArrayList with last Runthrough Dates (formatted)
	 */
	public ArrayList<String> getLastRunthroughDates(String name){
		
		List<String> lastDates = new ArrayList<String>();
	
		// SimpleDateFormat formater = new SimpleDateFormat();
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yy',' HH:mm");
		
		// Get last Runthroughs of selected Stack
		for (Runthrough runthrough : getLastRunthroughs(name)){
			// Add the End Date to the result
			lastDates.add(sd.format(runthrough.getEndDate()));
		}
		
		return (ArrayList<String>) lastDates;
	}
	/**
	 * Get Progress (statusAfter for all Runthroughs) for selected Stack
	 * 
	 * @param name selected Stack name
	 * @return int Array with the number of cards in each drawer after Runthroughs
	 */
	public int[][] getLastProgress(String name){
		
		// Instantiate and Initialize Array with Progress
		int[][] progress;
		progress = new int[10][3];
		
		// Get all last Runthroughs of selected Stack
		List<Runthrough> lastRunthroughs = getLastRunthroughs(name);
		
		// Get number of last Runthroughs
		int numberOfRunthroughs = lastRunthroughs.size();
		
		float result;
		
		// for (number of last Runthroughs)
		for (int i = 0; i < numberOfRunthroughs; i++){
			
			// Get status after and calculate total cards of last Runthrough
			int statusAfter[] = lastRunthroughs.get(i).getStatusAfter();
			int totalCards = statusAfter[0] + statusAfter[1] + statusAfter[2];
			
			// If there were more cards than 0
			if (totalCards != 0){
				for (int j = 0; j < 3; j++){
					// Calculate progress for each drawer (j)
					result = (((float) statusAfter[j] / totalCards) * 100);
					progress[i][j] = (int) result;
				}
			}
		}
		
		return progress;
	}
	
	
	/**
	 * Get last Runthroughs of selected Stack (name)
	 * 
	 * @param name selected Stack name
	 * @return List with last Runthroughs of selected Stacks (used for calculation of Progress)
	 */
	private List<Runthrough> getLastRunthroughs(String name){
		
		// for all Stacks
		for (Stack stack : Stack.allStacks){
			
			// Find relevant Stack and get all Runthroughs
			if (stack.getStackName().equals(name)){
				return stack.getLastRunthroughs();
			}
		}
		
		return null;
	}

	// ----------------Return Status of Drawers-------------------

	/**
	 * Returns the Actual Status of all Stacks [0] = dontKnow, [1] = notSure,
	 * [2] = sure
	 * 
	 * @return String-Array with actualDrawer Status
	 */
	public String[] getOverallActualDrawerStatus() {
		
		int actualStatus[] = { 0, 0, 0 };
		int statusCard;

		// Get all Cards and add status to the actualStatus-Array
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
	 * @param name selected Stack name
	 * @return actual Status of selected Stack (String-Array)
	 */
	public String[] getActualDrawerStatus(String name) {
		
		int statusCard;
		int actualStatus[] = { 0, 0, 0 };

		// For all Stacks
		for (Stack stack : Stack.allStacks) {
			
			// Get relevant Stack
			if (stack.getStackName().equals(name)) {
				
				// Get status of each card and update result
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
	 * @param actualStatus int[] containing the actual Status
	 * @return String[] with actual Status
	 */
	private String[] createActualDrawerStatusString(int[] actualStatus) {
		String[] actualDrawerStatus = new String[3];

		for (int i = 0; i < 3; i++) {
			actualDrawerStatus[i] = "" + actualStatus[i];
		}

		return actualDrawerStatus;
	}

}
