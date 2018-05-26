package cu.IntegratedLanguages;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import cu.Interface.DialogFragmentListener;

public class SelectLanguageApp extends AppCompatDialogFragment {

    private static final int UNSELECTED = -1;
    private static final String ARG_SELECTED = "ARG_SELECTED";
    private static final String ARG_ITEMS = "ARG_ITEMS";
    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String STATE_SELECTED = "STATE_SELECTED";

    private DialogFragmentListener listener;
    private int selected;

    public static SelectLanguageApp newInstance(Integer selection, String[] items, String title) {
        SelectLanguageApp fragment = new SelectLanguageApp();
        Bundle args = new Bundle();
        if (selection != null) {
            args.putInt(ARG_SELECTED, selection);
        }
        args.putStringArray(ARG_ITEMS, items);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DialogFragmentListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement SelectLanguageApp");
        }
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) { //rotation
            selected = savedInstanceState.getInt(STATE_SELECTED, UNSELECTED);
        } else if (getArguments() != null) {
            selected = getArguments().getInt(ARG_SELECTED, UNSELECTED);
        }

        final String[] lang = getArguments().getStringArray(ARG_ITEMS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(getArguments().getString(ARG_TITLE))
                .setSingleChoiceItems(lang, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected = which;
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selected != UNSELECTED) {
                            listener.getSelected(selected, lang[selected]);
                            //Toast.makeText(getContext(), lang[selected] + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED, selected);
    }

}