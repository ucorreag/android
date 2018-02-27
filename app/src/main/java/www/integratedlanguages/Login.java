package www.integratedlanguages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import Utile.ConnectionDB;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ConnectionDB db=new ConnectionDB(this);
        if(db.isUserLoged()){
            startActivity( new Intent(this, Plurilingual.class));
            finish();
        }

        if (savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.content_login, new FragmentUser()).commit();

        }
    }

}
