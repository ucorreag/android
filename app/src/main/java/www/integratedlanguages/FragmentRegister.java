package www.integratedlanguages;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import Utile.ConnectionDB;

public class FragmentRegister extends Fragment {

    private EditText user, password, name;
    private ConnectionDB db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view;
        view=inflater.inflate(R.layout.fragment_register, container,false);

        user=(EditText)view.findViewById(R.id.register_user);
        password=(EditText)view.findViewById(R.id.register_password);
        name=(EditText)view.findViewById(R.id.register_name);
        db=new ConnectionDB(view.getContext());


        Button register=(Button)view.findViewById(R.id.register_button);

        ImageButton users=(ImageButton) view.findViewById(R.id.return_login);
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_login, new FragmentUser()).commit();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long add=db.addUser(name.getText().toString(),user.getText().toString(),password.getText().toString());

                if(add==-1){
                    Toast.makeText(view.getContext(),"Error in insert into database",Toast.LENGTH_LONG).show();
                }else{
                    getFragmentManager().beginTransaction().replace(R.id.content_login, new FragmentLogin()).commit();

                }
            }
        });


        return  view;

    }
}
