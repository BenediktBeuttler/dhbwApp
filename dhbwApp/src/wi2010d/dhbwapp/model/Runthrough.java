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
	 * [0] BeforeDontKnow [1] BeforeNotSure [2] BeforeSure
	 */
	private int[] statusBefore = new int[3];
	
	/**
	 * [0] AfterDontKnow [1] AfterNotSure [2] AfterSure
	 */
	private int[] statusAfter = new int[3];


	public static int getNextRunthroughID(){
		lastRunthroughID = lastRunthroughID + 1;
		return lastRunthroughID;
	}
	
	/**
	 * Returns the duration of this Runthrough, format: X min , X sec
	 * @return duration : format: X min , X sec
	 */
	private String getDuration()
	{
		long durationMilliSecs = this.endDate.getTime()-this.startDate.getTime();
		
		String duration =String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(durationMilliSecs),
			    TimeUnit.MILLISECONDS.toSeconds(durationMilliSecs) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationMilliSecs))
			);
		return duration;
		
	}
	
	private void setStatusBefore(int beforeDontKnow, int beforeNotSure, int beforeSure)
	{
		this.statusBefore[0] = beforeDontKnow;
		this.statusBefore[1] = beforeNotSure;
		this.statusBefore[2] = beforeSure;
	}
	
	private void setStatusAfter(int afterDontKnow, int afterNotSure, int afterSure)
	{
		this.statusBefore[0] = afterDontKnow;
		this.statusBefore[1] = afterNotSure;
		this.statusBefore[2] = afterSure;
	}
	
	/**
	 * @return [0] BeforeDontKnow [1] BeforeNotSure [2] BeforeSure
	 */
	private int[] getStatusBefore()
	{
		return statusBefore;
	}
	
	/**
	 * @return [0] AfterDontKnow [1] AfterNotSure [2] AfterSure
	 */
	private int[] getStatusAfter()
	{
		return statusAfter;
	}

}
