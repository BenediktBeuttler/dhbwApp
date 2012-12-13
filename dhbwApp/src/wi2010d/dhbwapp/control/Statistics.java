package wi2010d.dhbwapp.control;

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
}
