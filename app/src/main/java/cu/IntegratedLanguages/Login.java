package cu.IntegratedLanguages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import cu.DataBase.UpdateFromServer;

import cu.DataBase.ConnectionDB;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        ConnectionDB db=new ConnectionDB(this);

        UpdateFromServer update= new UpdateFromServer();
        update.updateLanguage(this);
        update.updateLevel(this);
        update.updateSubject(this);
        update.updateSubjectLanguage(this);
        update.updateSentence(this);

        if(db.isUserLoged()){
            startActivity( new Intent(this, Plurilingual.class));
            this.finishAffinity();
        }

        setTitle("Login");



        if (savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.content_login, new FragmentBaseTabs()).commit();
            //getFragmentManager().beginTransaction().add(R.id.content_login, new FragmentUser()).commit();

        }


    }


}
