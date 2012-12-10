package wi2010d.dhbwapp.control;

import android.database.sqlite.SQLiteDatabase;

public class Settings {
	
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
