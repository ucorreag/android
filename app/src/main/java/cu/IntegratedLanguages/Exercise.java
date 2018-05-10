package cu.IntegratedLanguages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import cu.DataBase.ConnectionDB;
import cu.Plurilingual.ExerciseTypeOne;
import cu.Plurilingual.Language;
import cu.Plurilingual.Sentence;


@SuppressLint("Registered")
public class Exercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        int id_subject = intent.getIntExtra("id_subject",1);
        //int id_user;

        List<cu.Plurilingual.Exercise> listExercises=new LinkedList<>();

        ConnectionDB db= new ConnectionDB(this);
        Cursor exercises= db.getExercisesBySubject(id_subject+"");

        if (exercises.moveToFirst()){
            int type=exercises.getInt(exercises.getColumnIndex("type"));
            int position= exercises.getPosition();
            Bundle args = new Bundle();
            args.putInt("position", position);
            args.putInt("id_subject", id_subject);

            switch (type){
                case 1:
                    FragmentTypeOne fragmentTypeOne = new FragmentTypeOne();
                    fragmentTypeOne.setArguments(args);
                    getFragmentManager().beginTransaction().add(R.id.content_exercise, fragmentTypeOne).commit();
                    break;
                case 2:
                    FragmentTypeTwo fragmentTypeTwo = new FragmentTypeTwo();
                    fragmentTypeTwo.setArguments(args);
                    getFragmentManager().beginTransaction().add(R.id.content_exercise, fragmentTypeTwo).commit();
                    break;
                case 3:
                    FragmentTypeThree fragmentTypeThree = new FragmentTypeThree();
                    fragmentTypeThree.setArguments(args);
                    getFragmentManager().beginTransaction().add(R.id.content_exercise, fragmentTypeThree).commit();
                    break;
                case 4:
                    FragmentTypeFour fragmentTypeFour = new FragmentTypeFour();
                    fragmentTypeFour.setArguments(args);
                    getFragmentManager().beginTransaction().add(R.id.content_exercise, fragmentTypeFour).commit();
                    break;

            }

        }
        //add exercise to list
        //call at fragment



        /**
         * if exists exercises in this subject
         * put next args of order
         * else default msg "there are not exercises"
         * */

        //create queue
        //put id, type and queue last in next fragment


    }


}
