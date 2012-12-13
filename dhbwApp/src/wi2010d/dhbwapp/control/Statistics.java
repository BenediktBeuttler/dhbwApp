package wi2010d.dhbwapp.control;

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
	
	
	
	private String convertSecondsToString(int duration)
	{
		int seconds = 0;
		int minutes = 0;
		int hours = 0;
		
		seconds = duration % 60;
		minutes = duration / 60;
		hours = minutes / 60;
		minutes = minutes & 60;
		
		return hours + " hours, " + minutes + " minutes, " + seconds + " seconds";
	}
}
