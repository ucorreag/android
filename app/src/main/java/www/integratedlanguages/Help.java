package www.integratedlanguages;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;


/**
 * Created by Isai on 16/02/2018.
 */

public class Help extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.help)

                .setView(R.layout.content_help)
                .setPositiveButton(android.R.string.ok, null);

        return builder.create();


    }
}