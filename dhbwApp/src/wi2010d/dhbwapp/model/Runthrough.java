package wi2010d.dhbwapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import wi2010d.dhbwapp.control.Database;
import wi2010d.dhbwapp.control.Init;

/**
 * Represents one runthrough for a learning session. Is only associated with a
 * stack.
 */
public class Runthrough {

	public static List<Runthrough> allRunthroughs = new ArrayList<Runthrough>();
	public static int lastRunthroughID = 0;

	private int runthroughID;
	private int stackID;
	private boolean isOverall;
	private Date startDate;
	private Date endDate;
	private int duration = 0;

	/**
	 * [0] BeforeDontKnow [1] BeforeNotSure [2] BeforeSure
	 */
	private int[] statusBefore = new int[3];

	/**
	 * [0] AfterDontKnow [1] AfterNotSure [2] AfterSure
	 */
	private int[] statusAfter = new int[3];

	/**
	 * Use this constructor, when loading from DB Association to stack is made.
	 */
	public Runthrough(int runthroughID, int stackID, boolean isOverall,
			Date startDate, Date endDate, int[] statusBefore, int[] statusAfter) {
		this.runthroughID = runthroughID;
		this.stackID = stackID;
		this.isOverall = isOverall;
		this.startDate = startDate;
		this.endDate = endDate;
		this.statusBefore = statusBefore;
		this.statusAfter = statusAfter;

		Runthrough.allRunthroughs.add(this);

		if (lastRunthroughID <= runthroughID) {
			lastRunthroughID = runthroughID;
		}

		// Associate runthrough with stack
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackID() == this.stackID) {
				if (isOverall) {
					stack.setOverallRunthrough(this);
				} else {
					stack.initAddLastRunthrough(this);
				}
			}
		}
	}

	/**
	 * Use this constructor, when learning or creating a new overall runthrough
	 * Association to stack needs to be made.
	 * 
	 * @param stackID
	 * @param isOverall
	 * @param startDate
	 * @param statusBefore
	 * 
	 */
	public Runthrough(int stackID, boolean isOverall, int[] statusBefore) {
		this.stackID = stackID;
		this.isOverall = isOverall;
		this.statusBefore = statusBefore;
		this.runthroughID = Runthrough.getNextRunthroughID();

		this.startDate = new Date();
		this.endDate = new Date();

		Runthrough.allRunthroughs.add(this);

		Database.getInstance().addNewRunthrough(this);
		Init.dataWritten = true;
	}

	/**
	 * Gets the next free unique ID
	 * 
	 * @return the next free unique ID
	 */
	public static int getNextRunthroughID() {
		lastRunthroughID = lastRunthroughID + 1;
		return lastRunthroughID;
	}

	/**
	 * Returns the duration of this Runthrough, format: X min , X sec
	 * 
	 * @return duration : format: X min , X sec
	 */
	public String getDuration() {
		long durationMilliSecs = this.endDate.getTime()
				- this.startDate.getTime();

		String duration = String.format(
				"%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(durationMilliSecs),
				TimeUnit.MILLISECONDS.toSeconds(durationMilliSecs)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(durationMilliSecs)));
		return duration;

	}

	/**
	 * Sets the duration of the runthrough
	 * 
	 * @param newDuration
	 *            the duration of the runthrough in seconds
	 */
	public void setDurationSecs(int newDuration) {
		this.duration = newDuration;
	}

	/**
	 * Sets the duration of the runthrough while initialization
	 * 
	 * @param newDuration
	 *            the duration of the runthrough in seconds
	 */
	public void initSetDurationSecs(int newDuration) {
		this.duration = newDuration;
	}

	/**
	 * Returns the duration of this runthrough in seconds
	 * 
	 * @return the duration of this runthrough in seconds
	 */
	public int getDurationSecs() {
		return duration;
	}

	/**
	 * Sets the status before the runthrough
	 * 
	 * @param beforeDontKnow
	 *            Cards in the drawer dont know before the run
	 * @param beforeNotSure
	 *            Cards in the drawer not sure before the run
	 * @param beforeSure
	 *            Cards in the drawer sure before the run
	 */
	public void setStatusBefore(int beforeDontKnow, int beforeNotSure,
			int beforeSure) {
		this.statusBefore[0] = beforeDontKnow;
		this.statusBefore[1] = beforeNotSure;
		this.statusBefore[2] = beforeSure;
	}

	/**
	 * Sets the status after the runthrough
	 * 
	 * @param beforeDontKnow
	 *            Cards in the drawer dont know after the run
	 * @param beforeNotSure
	 *            Cards in the drawer not sure after the run
	 * @param beforeSure
	 *            Cards in the drawer sure after the run
	 */
	public void setStatusAfter(int afterDontKnow, int afterNotSure,
			int afterSure) {
		this.statusAfter[0] = afterDontKnow;
		this.statusAfter[1] = afterNotSure;
		this.statusAfter[2] = afterSure;
	}

	/**
	 * Sets the status after the runthrough while initialization
	 * 
	 * @param beforeDontKnow
	 *            Cards in the drawer dont know after the run
	 * @param beforeNotSure
	 *            Cards in the drawer not sure after the run
	 * @param beforeSure
	 *            Cards in the drawer sure after the run
	 */
	public void initSetStatusAfter(int afterDontKnow, int afterNotSure,
			int afterSure) {
		this.statusAfter[0] = afterDontKnow;
		this.statusAfter[1] = afterNotSure;
		this.statusAfter[2] = afterSure;
	}

	/**
	 * @return [0] BeforeDontKnow [1] BeforeNotSure [2] BeforeSure
	 */
	public int[] getStatusBefore() {
		return statusBefore;
	}

	/**
	 * @return [0] AfterDontKnow [1] AfterNotSure [2] AfterSure
	 */
	public int[] getStatusAfter() {
		return statusAfter;
	}

	/**
	 * @return EndDate of the run
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the EnDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return The runthroughs' id
	 */
	public int getRunthroughID() {
		return runthroughID;
	}

	/**
	 * @return the associated stacks' ID
	 */
	public int getStackID() {
		return stackID;
	}

	/**
	 * @return true, if the runthrough is an overall runthrough
	 */
	public boolean isOverall() {
		return isOverall;
	}

	/**
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Resets the ID counter
	 * @return
	 */
	public static boolean resetLastRunthroughID() {
		lastRunthroughID = 0;
		return true;
	}

	/**
	 * @return the next runthroughs' id
	 */
	public static int getLastRunthroughID() {
		return lastRunthroughID;
	}

	@Override
	public boolean equals(Object o) {
		Runthrough run = (Runthrough) o;
		if (run.getRunthroughID() == this.runthroughID) {
			return true;
		}
		return false;
	}

}
