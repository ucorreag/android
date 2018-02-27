package www.integratedlanguages;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Utile.ConnectionDB;
import Plurilingual.*;
/**
 * Created by Isai on 7/01/2018.
 */

public class FragmentSubject extends Fragment {
    private ListView lista;
    private final static String ID_LEVEL=Integer.toString(1);

    public FragmentSubject() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view;
        view=inflater.inflate(R.layout.fragment_subject, container,false);

        ConnectionDB cdb=new ConnectionDB(view.getContext());

        final ArrayList<Subject> subject=new ArrayList<>();
        final AdapterSubject adapter= new AdapterSubject(view.getContext(), subject);

        adapter.clear();

        try {
            Cursor c= cdb.getSubject(ID_LEVEL,getResources().getConfiguration().locale.getLanguage().toUpperCase());
            while (c.moveToNext()){
                Subject subj=new Subject(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("caption_subject"))
                );
                adapter.add(subj);
        }

        }catch(Exception ex){
            Toast.makeText(view.getContext(),"error DB: "+ex.getMessage(),Toast.LENGTH_LONG).show();


        }

        lista=(ListView)view.findViewById(R.id.list_view_subject);
        lista.setAdapter(adapter);


        return  view;
    }
}
