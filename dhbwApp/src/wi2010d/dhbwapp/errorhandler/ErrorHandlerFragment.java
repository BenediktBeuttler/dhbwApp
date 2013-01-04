package wi2010d.dhbwapp.errorhandler;

import wi2010d.dhbwapp.R;
import wi2010d.dhbwapp.control.DatabaseManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ErrorHandlerFragment extends DialogFragment{
	
	public static final int GENERAL_ERROR = 1;
	public static final int IMPORT_ERROR = 2;
	public static final int EXPORT_ERROR = 3;
	public static final int NO_SD = 4;
	public static final int NAME_TAKEN = 5;
	
	public static final int RESET_DB = 10;

	static int errorCode; 
	
	public static ErrorHandlerFragment newInstance(int title, int errorCode1) {
		errorCode = errorCode1;
		ErrorHandlerFragment frag = new ErrorHandlerFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        
        if (errorCode > 9)					//Dialogs with ErrorCode 10 or higher get only OK-Button
        {
        	return new AlertDialog.Builder(getActivity())
            .setIcon(R.drawable.alert)
            .setTitle(title)
            .setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	onOk(errorCode);
                    }
                }
            )
            .setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	onCancel();
                    }
                }
            )
            .create();
        }
        else
        {
        	return new AlertDialog.Builder(getActivity())
            .setIcon(R.drawable.alert)
            .setTitle(title)
            .setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	//nothing happends
                    }
                }
            )
            .create();
        }

        
    }

	private void onOk(int errorCode) {
		// TODO Auto-generated method stub
		switch (errorCode) {
		case 10:
		{
			DatabaseManager.getInstance().deleteDB();
			Toast toast;
			toast = Toast.makeText(getActivity(),
					"Database deleted!", Toast.LENGTH_LONG);
			toast.show();
		}
			break;
		default:
			break;
		}
	}
	
	private void onCancel(){
		this.dismiss();
	}
}
