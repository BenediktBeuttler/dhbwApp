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
	public final int NAME_ALREADY_TAKEN = 5;
	public final int TEXT_FIELD_FRONT_EMPTY = 6;
	public final int TEXT_FIELD_BACK_EMPTY = 7;
	public final int NO_STACK_AVAILABLE = 8;

	public ErrorHandler(Context context) {
		super();
		this.context = context;
		handler = this;
	}

	public static ErrorHandler getInstance() {
		return handler;
	}

	public void handleError(int errorCode) {
		CharSequence text;
		int duration;
		Toast toast;

		switch (errorCode) {
		case 1:
			text = "Hello toast!";
			duration = Toast.LENGTH_SHORT;
			toast = Toast.makeText(context, text, duration);
			toast.show();

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		case 5:
			text = "Could not create object, name already taken!";
			duration = Toast.LENGTH_SHORT;
			toast = Toast.makeText(context, text, duration);
			toast.show();
			break;
		case 6:
			text = "Cannot create Card, front text is empty!";
			duration = Toast.LENGTH_SHORT;
			toast = Toast.makeText(context, text, duration);
			toast.show();
			break;
		case 7:
			text = "Cannot create Card, back text is empty!";
			duration = Toast.LENGTH_SHORT;
			toast = Toast.makeText(context, text, duration);
			toast.show();
		case 8:
			text = "Cannot add Card to a existing Stack, there's no stack available!";
			duration = Toast.LENGTH_SHORT;
			toast = Toast.makeText(context, text, duration);
			toast.show();
		default:
			break;
		}
	}

}
