package wi2010d.dhbwapp.control;

/**
 * Offers different adjustments to the App. Used in the Settings-Activity
 */
public class Settings {

	private static Settings settings;

	/**
	 * Constructor
	 */
	public Settings() {
		;
	}

	/**
	 * Singleton Method
	 * 
	 * @return
	 */
	public static Settings getInstance() {
		if (settings == null) {
			settings = new Settings();
		}

		return settings;
	}

	private Integer fontSize;

	/**
	 * Reset the Database
	 * 
	 * @return true if it worked
	 */
	private Boolean resetDB() {
		return DatabaseManager.getInstance().deleteDB();
	}

	/**
	 * Changes the Font for every TextView
	 * 
	 * @param input
	 *            TextField
	 * @return the converted TextField
	 */
	private Integer changeFont(Integer input) { // Input from TextField has to
												// be converted to Integer and
												// put into the constructor
		input = fontSize;
		return fontSize;
	}

}
