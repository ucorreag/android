package cu.IntegratedLanguages;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cu.DataBase.ConnectionDB;
import cu.Utile.Encrypt;
import cu.Utile.Images;


public class FragmentLogin extends Fragment {

    private TextView user,name;
    private EditText password;
    private ListView listUsers;
    private ConnectionDB db;
    boolean move;
    int start,end;
    //String sequence, value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        view=inflater.inflate(R.layout.fragment_login, container,false);

        String us = getArguments()!=null?getArguments().getString("user"):"";
        String nm = getArguments()!=null?getArguments().getString("name"):"";

        //Redondear imagen
        Images images =new Images();
        Drawable roundedDrawable= images.roundImage(view.getContext(),R.drawable.ic_action_user);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_user);
        imageView.setImageDrawable(roundedDrawable);


        user=(TextView)view.findViewById(R.id.user);
        name=(TextView)view.findViewById(R.id.name);
        password=(EditText)view.findViewById(R.id.password);
        listUsers=(ListView)view.findViewById(R.id.list_users);
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.fragment_login);

        user.setText(us);
        name.setText(nm);


        Button login=(Button) view.findViewById(R.id.sign_in_button);

        db= new ConnectionDB(view.getContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Encrypt en=new Encrypt();
                String pass=en.get_SHA_512_SecurePassword(password.getText().toString());

                if(db.existUser(user.getText().toString(),pass)){
                    db.setLoged(user.getText().toString(),1);
                    startActivity( new Intent(view.getContext(), Plurilingual.class));

                }else{
                    Toast.makeText(view.getContext(),"your user or password aren't correct",Toast.LENGTH_LONG).show();
                }

            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        start = x;
                        break;
                    case (MotionEvent.ACTION_MOVE):
                        end = x;
                        move = true;
                        break;
                    case (MotionEvent.ACTION_UP):
                        if(move && (end - start)>100){

                            getFragmentManager().beginTransaction().replace(R.id.content_login, new FragmentBaseTabs()).commit();

                        }

                        move = false;
                        break;
                }

                return true;
            }
        });


        return view;

    }
}
