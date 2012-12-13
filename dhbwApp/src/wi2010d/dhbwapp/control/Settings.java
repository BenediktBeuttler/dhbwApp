package wi2010d.dhbwapp.control;

import android.database.sqlite.SQLiteDatabase;

public class Settings {
	
	private static Settings settings;
	
	/**
	 * Constructor
	 */
	public Settings()
	{
		;
	}
	
	/**
	 * Singleton Method
	 * 
	 * @return
	 */
	public static Settings getInstance()
	{
		if (settings == null)
		{
			settings = new Settings();
		}
		
		return settings;
	}
	
	
	private Integer fontSize;
	
	private Boolean resetDB() {
		DatabaseManager.getInstance().deleteDB();
		return true;
	}
	
	private Integer changeFont(Integer input) {		//Input from TextField has to be converted to Integer and put into the constructor
		input = fontSize;
		return fontSize;
	}

}
