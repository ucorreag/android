package cu.IntegratedLanguages;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import cu.DataBase.ConnectionDB;
import cu.Plurilingual.Sentence;

public class Sentences extends AppCompatActivity {
    LinearLayout layout;
    ListView listSentences;
    ArrayList<Sentence> adapter;
    ConnectionDB cdb;


    Intent intent;
    String idTema,sequence, value;
    boolean move;
    int start,end;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout=(LinearLayout)findViewById(R.id.content_sentences);
        listSentences = (ListView) findViewById(R.id.list_sentences);
        cdb=new ConnectionDB(this);
        intent=getIntent();
        idTema=intent.getStringExtra("id");

        adapter=new ArrayList<>();
        AdapterSentences ads=new AdapterSentences(this,adapter);

        listSentences.setAdapter(ads);
        sequence=1+"";

        addElementToList(sequence);
        start=0;
        end=0;
        move = false;
        value=1+"";


        listSentences.setOnTouchListener(new View.OnTouchListener(){

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
                        if(move && (start - end)>100){
                           value=(Integer.parseInt(sequence)+1)+"";
                            addElementToList(value);

                        }else if(move && (end - start)>100){
                            value=(Integer.parseInt(sequence)-1)+"";
                            addElementToList(value);

                        }

                        move = false;
                        break;
                }

                return true;
            }
        });



    }



    private void addElementToList(String seq){
        Cursor c;
        try{
            c=cdb.getSentencesBySequence(idTema,seq);
            if (c!=null)adapter.clear();

            assert c != null;
            while (c.moveToNext()){
                String lang=cdb.getLanguageById(c.getInt(c.getColumnIndex("id_language"))+"");
                Sentence cn=new Sentence(
                        c.getInt(c.getColumnIndex("id")),lang ,
                        c.getString(c.getColumnIndex("caption_sentence"))
                );

                adapter.add(cn);
                listSentences.setAdapter(new AdapterSentences(this,adapter));
                sequence=seq;

                //
                int sequ=Integer.parseInt(seq);
                Cursor left=cdb.getSentencesBySequence(idTema,(sequ-1)+"");
                Cursor right=cdb.getSentencesBySequence(idTema,(sequ+1)+"");
                ImageButton imgLeft=(ImageButton)this.findViewById(R.id.btn_left);
                ImageButton imgRight=(ImageButton)this.findViewById(R.id.btn_right);


                if(!left.moveToFirst() & !right.moveToFirst()){
                    imgLeft.setImageDrawable(null);
                    imgRight.setImageDrawable(null);
                }
                else if(!right.moveToFirst()){
                    imgLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_left));
                    imgRight.setImageDrawable(null);
                }else if(!left.moveToFirst()){
                    imgRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_right));
                    imgLeft.setImageDrawable(null);
                }else{
                    imgLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_left));
                    imgRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_right));
                }

            }

        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Error DB: "+e.getMessage(),Toast.LENGTH_LONG).show();

        }

    }
@Override
public boolean onOptionsItemSelected(MenuItem item){
    switch (item.getItemId()){
        case android.R.id.home:
            Intent upIntent = getParentActivityIntent();
            navigateUpTo(upIntent);

            break;
    }
  return  true;
}



}
