package wi2010d.dhbwapp.model;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Runthrough {

	public static List<Runthrough> allRunthroughs;
	public static int lastRunthroughID = 0;

	private int runthroughID;
	private int stackID;
	private boolean isOverall;
	private Date startDate;
	private Date endDate;

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

		// Associate runthrough with stack
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackID() == this.stackID) {
				if (isOverall) {
					stack.setOverallRunthrough(this);
				} else {
					stack.addLastRunthrough(this);
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
	public Runthrough(int stackID, boolean isOverall, Date startDate,
			int[] statusBefore) {
		this.stackID = stackID;
		this.isOverall = isOverall;
		this.startDate = startDate;
		this.statusBefore = statusBefore;
		this.runthroughID = Runthrough.getNextRunthroughID();

		Runthrough.allRunthroughs.add(this);
	}

	/**
	 * [0] BeforeDontKnow [1] BeforeNotSure [2] BeforeSure
	 */
	private int[] statusBefore = new int[3];

	/**
	 * [0] AfterDontKnow [1] AfterNotSure [2] AfterSure
	 */
	private int[] statusAfter = new int[3];

	public static int getNextRunthroughID() {
		lastRunthroughID = lastRunthroughID + 1;
		return lastRunthroughID;
	}

	/**
	 * Returns the duration of this Runthrough, format: X min , X sec
	 * 
	 * @return duration : format: X min , X sec
	 */
	private String getDuration() {
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

	private void setStatusBefore(int beforeDontKnow, int beforeNotSure,
			int beforeSure) {
		this.statusBefore[0] = beforeDontKnow;
		this.statusBefore[1] = beforeNotSure;
		this.statusBefore[2] = beforeSure;
	}

	private void setStatusAfter(int afterDontKnow, int afterNotSure,
			int afterSure) {
		this.statusBefore[0] = afterDontKnow;
		this.statusBefore[1] = afterNotSure;
		this.statusBefore[2] = afterSure;
	}

	/**
	 * @return [0] BeforeDontKnow [1] BeforeNotSure [2] BeforeSure
	 */
	private int[] getStatusBefore() {
		return statusBefore;
	}

	/**
	 * @return [0] AfterDontKnow [1] AfterNotSure [2] AfterSure
	 */
	private int[] getStatusAfter() {
		return statusAfter;
	}

}
