package cu.IntegratedLanguages;



import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import cu.DataBase.ConnectionDB;
import cu.Utile.Encrypt;
import cu.Plurilingual.User;



public class FragmentBaseTabs extends Fragment {

    TabHost tabHost;

    private ListView listUser;
    private AdapterUser adapter;
    private ConnectionDB db;

    private EditText user;

    private EditText password;
    private EditText password1;


    private  EditText name;
    TextInputLayout layoutRepeat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_base_tabs,container,false);

        tabHost= (TabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec1=tabHost.newTabSpec("tab1");
        tabSpec1.setContent(R.id.fragment_users);
        tabSpec1.setIndicator(getResources().getString(R.string.users));
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2=tabHost.newTabSpec("tab2");
        tabSpec2.setContent(R.id.fragment_register);
        tabSpec2.setIndicator(getResources().getString(R.string.register));
        tabHost.addTab(tabSpec2);

        tabHost.setCurrentTab(0);


        //users

        db=new ConnectionDB(view.getContext());
        adapter=new AdapterUser(view.getContext(),new ArrayList<User>());
        listUser=(ListView)view.findViewById(R.id.list_users);


        registerForContextMenu(listUser);

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





        //register

        user=(EditText)view.findViewById(R.id.register_user);
        password=(EditText)view.findViewById(R.id.register_password);
        password1=(EditText)view.findViewById(R.id.repeat_password);
        name=(EditText)view.findViewById(R.id.register_name);
        layoutRepeat=(TextInputLayout)view.findViewById(R.id.repeat);

        Button register=(Button)view.findViewById(R.id.register_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pas=password.getText().toString().trim();
                String nam=name.getText().toString().trim();
                String us=user.getText().toString().trim();
                String conf=password1.getText().toString().trim();

                if(nam.isEmpty() || us.isEmpty() || pas.isEmpty() || !pas.equals(conf)){
                    Toast.makeText(getActivity(), "Fill your information as appropriate", Toast.LENGTH_LONG).show();

                }else {
                    Encrypt en = new Encrypt();
                    String pass = en.get_SHA_512_SecurePassword(pas);
                    Long add = db.addUser(nam, us, pass);

                    if (add == -1) {
                        Toast.makeText(getActivity(), "Error in insert into database", Toast.LENGTH_LONG).show();
                    } else {
                        Bundle args = new Bundle();
                        args.putString("user", us);
                        args.putString("name", nam);
                        FragmentLogin fragment = new FragmentLogin();
                        fragment.setArguments(args);

                        getFragmentManager().beginTransaction().replace(R.id.content_login, fragment).commit();

                    }
                }
            }
        });




        return view;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {



        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
