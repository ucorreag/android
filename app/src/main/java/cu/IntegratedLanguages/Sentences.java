package cu.IntegratedLanguages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

public class Sentences extends AppCompatActivity implements
        TextToSpeech.OnInitListener  {
    LinearLayout layout;
    ListView listSentences;
    ArrayList<Sentence> adapter;
    ConnectionDB cdb;
    private Cursor sequence;

    Intent intent;
    String idTema;
    int start,end, value;
    ImageButton btnLeft, btnRight;
    private AdapterSentences ads;
    private TextToSpeech tts;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout=(LinearLayout)findViewById(R.id.content_sentences);
        listSentences = (ListView) findViewById(R.id.list_sentences);
        btnLeft=(ImageButton)findViewById(R.id.btn_left);
        btnRight=(ImageButton)findViewById(R.id.btn_right);

        cdb=new ConnectionDB(this);
        intent=getIntent();
        idTema=intent.getStringExtra("id");
        tts=new TextToSpeech(this,this);
        adapter=new ArrayList<>();
        ads=new AdapterSentences(this,adapter, tts);

        listSentences.setAdapter(ads);

        sequence=cdb.getSequence(idTema);
        if(sequence.moveToFirst()){
            addElementToList(sequence.getInt(0));
        }
        paintIconsMoves();

        start=0;
        end=0;


        listSentences.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();

                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        start = x;
                        end = x;
                        break;
                    case (MotionEvent.ACTION_MOVE):
                        end = x;
                        break;
                    case (MotionEvent.ACTION_UP):
                        if((start - end)>100 && sequence.moveToNext()){
                           addElementToList(sequence.getInt(0));
                        }else if((end - start)>100 && sequence.moveToPrevious()){
                           addElementToList(sequence.getInt(0));
                        }
                        paintIconsMoves();
                        break;
                }

                return true;
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sequence.moveToPrevious()) {
                    addElementToList(sequence.getInt(0));
                    paintIconsMoves();
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //value=(Integer.parseInt(sequence)+1)+"";
                if(sequence.moveToNext()) {
                    addElementToList(sequence.getInt(0));
                    paintIconsMoves();
                }
            }
        });




    }



    private void addElementToList(int seq){
        Cursor c;
        try{
            c=cdb.getSentencesBySequence(idTema,seq+"");
            if (c!=null)adapter.clear();

            assert c != null;
            while (c.moveToNext()){
                String lang="ES";
                Cursor cursor=cdb.getLanguageById(c.getInt(
                        c.getColumnIndex("id_language"))+"");
                if(cursor.moveToFirst()){
                    lang=cursor.getString(2);
                }
                Sentence cn=new Sentence(
                        c.getInt(0),//id
                        lang ,
                        c.getString(c.getColumnIndex("caption_sentence"))
                );

                adapter.add(cn);

                listSentences.setAdapter(new AdapterSentences(this,adapter,tts));
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

public void paintIconsMoves(){
    if(sequence.moveToNext()){
        btnRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_right));
    }
    else{
        btnRight.setImageDrawable(null);
    }
    sequence.moveToPrevious();

    if(!sequence.moveToPrevious()){
        btnLeft.setImageDrawable(null);
    }
    else{
        btnLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_left));

    }
    sequence.moveToNext();
}

    @Override
    protected void onDestroy() {
        tts.shutdown();
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

    }
}
