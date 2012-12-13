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
	
	public String getOverallDuration()
	{
		String duration;
		
		for (Stack stack : Stack.allStacks)
		{
			
		}
			
		return duration
	}
}
