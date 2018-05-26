package cu.IntegratedLanguages;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

public class Info extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.info)

                .setView(R.layout.content_info)
                .setPositiveButton(android.R.string.ok, null);

        return builder.create();


    }
}