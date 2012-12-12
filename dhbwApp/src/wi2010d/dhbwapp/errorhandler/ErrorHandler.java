package wi2010d.dhbwapp.errorhandler;

import android.content.Context;
import android.widget.Toast;

public class ErrorHandler {
	
	public Context context;
	public static ErrorHandler handler;
	public final int GENERAL_ERROR = 1;
	public final int IMPORT_ERROR = 2;
	public final int EXPORT_ERROR = 3;
	public final int PATH_NOT_FOUND_ERROR = 4;
	
	

	public ErrorHandler(Context context) {
		super();
		this.context = context;
		handler = this;
		//handleError(1);
	}
	
	public static ErrorHandler getInstance(){
		return handler;
	}	

	public void handleError(int errorCode){
	
		switch (errorCode) {
		case 1:
			CharSequence text = "Hello toast!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			
			break;
		case 2:
			
			break;
		case 3:
	
			break;
		case 4:
			
			break;
		case 5:
	
			break;
		case 6:
	
			break;
		default:
			break;
		}
	}
	
	
}
