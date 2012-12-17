package wi2010d.dhbwapp.errorhandler;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ErrorHandlerFragment extends DialogFragment{
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Fehler (noch mit Variabeln ersetzen)")
               .setPositiveButton("Action (not Var)", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //Action goes here
                   }
               })
               .setNegativeButton("Chancel (not Var)", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dialog.cancel();
                	   //User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
		
		return builder.create();
	}

}
