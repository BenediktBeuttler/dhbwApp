package wi2010d.dhbwapp.control;


import java.text.SimpleDateFormat;
import java.util.List;

import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;

public class Statistics {

	private static Statistics statistics;
	
	/**
	 * Constructor
	 */
	public Statistics()
	{
		;
	}
	
	/**
	 * Singleton Method
	 * 
	 * @return
	 */
	public static Statistics getInstance()
	{
		if (statistics == null)
		{
			statistics = new Statistics();
		}
		
		return statistics;
	}
	
	//----------------Return data for last Review screen--------------
	
	/**
	 * Method that returns name of the stack with the last runthrough
	 * 
	 * @return
	 */
	public String getLastRunthroughName()
	{
		Runthrough lastRunthrough = getLastRunthrough();
		if (lastRunthrough != null)
		{
			for (Stack stack : Stack.allStacks)
			{
				if (stack.getStackID() == lastRunthrough.getStackID())
				{
					return stack.getStackName();
				}
			}
			
			return "No Data available";
		}
		else
		{
			return "No Data available";
		}
	}
	
	/**
	 * Returns date of last Runthrough
	 * 
	 * @return
	 */
	public String getLastRunthroughDate()
	{
		String date;
		Runthrough lastRunthrough = getLastRunthrough();
		if (lastRunthrough != null)
		{
			SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
			date = sd.format(lastRunthrough.getEndDate());
			return date;
		}
		else
		{
			return "No Data available";
		}
	}
	
	/**
	 * Returns duration of last Runthrough
	 * 
	 * @return
	 */
	public String getDurationOfLastRunthrough()
	{
		Runthrough lastRunthrough = getLastRunthrough();
		
		if (lastRunthrough != null)
		{
			return this.convertSecondsToString(lastRunthrough.getDurationSecs());
		}
		else
		{
			return "No Data available";
		}
	}
	
	/**
	 * returns Array with status of stack before the last runthrough
	 * 
	 * @return
	 */
	public String[] getStatusBefore()
	{
		String[] statusBefore = {"0","0","0"};
		Runthrough lastRunthrough = getLastRunthrough();
		
		if (lastRunthrough != null)
		{
			int[] statusBeforeInt = lastRunthrough.getStatusBefore();
			
			statusBefore[0] = "" + statusBeforeInt[0];
			statusBefore[1] = "" + statusBeforeInt[1];
			statusBefore[2] = "" + statusBeforeInt[2];
		}
		else
		{
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
	public String[] getStatusAfter()
	{
		String[] statusAfter = {"0","0","0"};
		Runthrough lastRunthrough = getLastRunthrough();
		
		if (lastRunthrough != null)
		{
			int[] statusAfterInt = lastRunthrough.getStatusAfter();
			
			statusAfter[0] = "" + statusAfterInt[0];
			statusAfter[1] = "" + statusAfterInt[1];
			statusAfter[2] = "" + statusAfterInt[2];
		}
		else
		{
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
	private Runthrough getLastRunthrough()
	{
		int lastID = Runthrough.getLastRunthroughID();
		List<Runthrough> allRunthroughs = Runthrough.allRunthroughs;
		
		for (int i = (allRunthroughs.size() - 1); i == 0; i--)
		{
			if (allRunthroughs.get(i).getRunthroughID() == lastID)
			{
				if (allRunthroughs.get(i).isOverall())
				{
					lastID = lastID - 1;
					i = allRunthroughs.size();
				}
				else
				{
					return allRunthroughs.get(i);
				}
			}
		}
		
		return null;
	}
	
	//----------------Return Duration-------------------
	
	/**
	 * adds the duration of all overall Runthroughs to calculate the total time
	 * @return
	 */
	public String getOverallDuration()
	{
		int duration = 0;

		for (Stack stack : Stack.allStacks)
		{
			duration = duration + stack.getOverallRunthrough().getDurationSecs();
		}
			
		return this.convertSecondsToString(duration);
	}
	
	/**
	 * returns overall duration of selected stack
	 * 
	 * @param name
	 * @return
	 */
	public String getStackOverallDuration(String name)
	{
		int duration = 0;
		
		for (Stack stack : Stack.allStacks)
		{
			if (stack.getStackName().equals(name))
			{
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
	private String convertSecondsToString(int duration)
	{
		int seconds = 0;
		int minutes = 0;
		int hours = 0;
		
		seconds = duration % 60;
		minutes = duration / 60;
		hours = minutes / 60;
		minutes = minutes & 60;
		
		if (hours > 0)
		{
			return minutes + " minutes, " + seconds + " seconds";
		}
		else
		{
			return hours + " hours, " + minutes + " minutes, " + seconds + " seconds";
		}
	}
	
	
	//----------------Return relevant Dates (stack created, last runthrough)----------
	

	
	
	//----------------Return Status of Drawers-------------------
	
	/**
	 * Returns the Actual Status of all Stacks
	 * [0] = dontKnow, [1] = notSure, [2] = sure
	 * 
	 * @return
	 */
	public String[] getOverallActualDrawerStatus()
	{
		int actualStatusAfter[];
		int actualStatus[] = {0,0,0}; 
		
		for (Stack stack : Stack.allStacks)
		{
			actualStatusAfter = stack.getOverallRunthrough().getStatusAfter();
			
			for (int i = 0; i<3; i++)
			{
				actualStatus[i] = actualStatus[i] + actualStatusAfter[i];
			}
		}
		
		return createActualDrawerStatusString(actualStatus);
	}
	
	/**
	 * Returns the actual status of selected stack
	 * [0] = dontKnow, [1] = notSure, [2] = sure
	 * 
	 * @param name	= name of selected stack
	 * @return
	 */
	public String[] getActualDrawerStatus(String name)
	{
		int actualStatus[] = {0,0,0};
		
		for (Stack stack : Stack.allStacks)
		{
			if (stack.getStackName().equals(name))
			{
				actualStatus = stack.getOverallRunthrough().getStatusAfter();
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
	private String[] createActualDrawerStatusString(int[] actualStatus)
	{
		String[] actualDrawerStatus = new String[3];
		
		for (int i = 0; i < 3; i++)
		{
			actualDrawerStatus[i] = "" + actualStatus[i] + "";
		}
		
		return actualDrawerStatus;
	}
	
}
