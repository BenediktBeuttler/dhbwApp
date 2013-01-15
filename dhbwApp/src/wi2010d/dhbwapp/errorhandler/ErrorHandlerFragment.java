package wi2010d.dhbwapp.errorhandler;

import wi2010d.dhbwapp.R;
import wi2010d.dhbwapp.control.DatabaseManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Handles all kinds of Errors in an AlertDialog.
 */
public class ErrorHandlerFragment extends DialogFragment {

	public static Context applicationContext;
	public static final int GENERAL_ERROR = 1;
	public static final int IMPORT_ERROR = 2;
	public static final int EXPORT_ERROR = 3;
	public static final int NO_SD = 4;
	public static final int NAME_TAKEN = 5;
	public static final int NO_INPUT = 6;
	public static final int NO_TAG = 7;
	public static final int FILE_NOT_FOUND = 8;

	public static final int RESET_DB = 10;

	static int errorCode;

	/**
	 * Creates new Instance
	 * 
	 * @param title
	 *            Sets the title of the dialog
	 * @param errorCode
	 *            Passes the codeNummer which declares which error to show
	 * @return Bundle
	 */
	public static ErrorHandlerFragment newInstance(int title, int errorCode) {
		ErrorHandlerFragment.errorCode = errorCode;
		ErrorHandlerFragment frag = new ErrorHandlerFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	/**
	 * Gets called when dialog is build
	 */
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");

		if (errorCode > 9) // Dialogs with ErrorCode 10 or higher get only
							// OK-Button
		{
			// Create new dialog fragment
			return new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.alert)
					.setTitle(title)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// gets called when OK is pressed
									onOk(errorCode);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// gets called when canceled
									onCancel();
								}
							}).create();
		} else {
			// Creates dialog only with OK Button
			return new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.alert)
					.setTitle(title)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// nothing happends, dialog gets cancled
									onCancel();
								}
							}).create();
		}

	}

	/** Defines what happends when OK Buttons is clicked
	 * 
	 * @param errorCode ErrorCode needed to handle error
	 */
	private void onOk(int errorCode) {
		// show
		switch (errorCode) {
		case 10: {
			DatabaseManager.getInstance().deleteDB();
			Toast toast;
			toast = Toast.makeText(getActivity(), "Database deleted!",
					Toast.LENGTH_LONG);
			toast.show();
		}
			break;
		default:
			break;
		}
	}

	private void onCancel() {
		this.dismiss();
	}
}
