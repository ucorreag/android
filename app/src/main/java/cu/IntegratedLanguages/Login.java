package cu.IntegratedLanguages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import cu.DataBase.UpdateFromServer;

import cu.DataBase.ConnectionDB;

public class Login extends AppCompatActivity {
    private ConnectionDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db=new ConnectionDB(this);

        //update database
        update();




        if(db.isUserLoged()){
            startActivity( new Intent(this, Plurilingual.class));
            this.finishAffinity();
        }

        setTitle("Iniciar sesión");



        if (savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.content_login, new FragmentBaseTabs()).commit();
            //getFragmentManager().beginTransaction().add(R.id.content_login, new FragmentUser()).commit();

        }


    }

    public void update(){
        UpdateFromServer update= new UpdateFromServer(this, db);
        update.updateLanguage();
        update.updateLevel();
        update.updateSubject();
        update.updateSubjectLanguage();
        update.updateSentence();
        update.updateExerciseFirstType();
        update.updateExerciseSecondType();
        update.updateExerciseThirdType();
        update.updateExerciseFourthType();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
