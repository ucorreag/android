package www.integratedlanguages;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import Plurilingual.*;
import Utile.ConnectionDB;

import java.util.ArrayList;

public class FragmentUser extends Fragment {

    private ListView listUser;
    private AdapterUser adapter;
    private ConnectionDB db;
    private TextView delete, register;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,container,false);

        db=new ConnectionDB(view.getContext());
        adapter=new AdapterUser(view.getContext(),new ArrayList<User>());
        listUser=(ListView)view.findViewById(R.id.list_users);
        register=(TextView)view.findViewById(R.id.create_user);
        delete=(TextView)view.findViewById(R.id.delete_user);

        Cursor c=db.getUsers();

        if(c!=null){
            while (c.moveToNext()){
                User newUser=new User(
                        c.getString(c.getColumnIndex("user")),
                        c.getString(c.getColumnIndex("password")),
                        c.getString(c.getColumnIndex("name")));
                adapter.add(newUser);
            }
        }


        listUser.setAdapter(adapter);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_login,new FragmentRegister()).commit();
            }
        });


        return view;

    }



}
