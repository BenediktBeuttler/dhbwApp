package wi2010d.dhbwapp.control;

import android.database.sqlite.SQLiteDatabase;

public class Settings {
	
	private Boolean reminderOn;
	private Integer fontSize;
	
	private Boolean resetDB() {
		DatabaseManager.getInstance().deleteDB();
		return true;
	}

}
