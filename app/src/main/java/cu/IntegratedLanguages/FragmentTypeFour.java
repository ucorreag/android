package cu.IntegratedLanguages;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import cu.DataBase.ConnectionDB;
import cu.Utile.Images;

public class FragmentTypeFour extends Fragment implements TextToSpeech.OnInitListener{

    private Cursor exercises;
    private int  id_subject, id_user, order_exercise;
    private String lang;
    private String answers;
    private Context context;
    private EditText answer;
    private ConnectionDB db;
    TextToSpeech tts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_exercise_type_four,container, false);
        context=view.getContext();
        tts=new TextToSpeech(context,this);

        ImageView imgLang=(ImageView)view.findViewById(R.id.img_lang_listen_question);
        TextView txtLang = (TextView) view.findViewById(R.id.txt_lang_listen_question);
        ImageButton listen=(ImageButton)view.findViewById(R.id.btn_listen);

        answer =(EditText)view.findViewById(R.id.edit_listen);

        Button btnOk=(Button)view.findViewById(R.id.ok_btn_listen);
        Button btnOut=(Button)view.findViewById(R.id.out_btn_listen);


        int position=(getArguments()!=null)?getArguments().getInt("position"):1;
        id_subject=(getArguments()!=null)?getArguments().getInt("id_subject"):1;


        db= new ConnectionDB(view.getContext());
        exercises= db.getExercisesBySubject(id_subject+"");

        Cursor c= db.getUserLog();
        if(c.moveToFirst())id_user=c.getInt(c.getColumnIndex("id"));

        if(exercises.moveToPosition(position)) {
            order_exercise=exercises.getInt(exercises.getColumnIndex("order_exercise"));
            //exercise
            Cursor ex = db.getExerciseById(exercises.getInt(0) + "", exercises.getInt(1));
            if (ex.moveToFirst()) {

                //option sentence
                Cursor sentence1 = db.getSentenceById(ex.getString(ex.getColumnIndex("question")));
                if (sentence1.moveToFirst()) {
                    Cursor k=db.getLanguageById(sentence1.getInt(
                            sentence1.getColumnIndex("id_language"))+"");
                    lang="ES";
                    if(k.moveToFirst()){
                        lang=k.getString(2);
                    }

                    Images images=new Images();
                    Drawable dwq=images.getImgLang(view.getContext(),lang);
                    imgLang.setImageDrawable(dwq);
                    txtLang.setText(lang);



                    answers=sentence1.getString(sentence1.getColumnIndex("caption_sentence"));

                }

            }



            listen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status=tts.setLanguage(new Locale(lang));
                    speech(answers, status);
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ans=answer.getText().toString();
                    String val="";
                    if(answers.toLowerCase().trim().equals(ans.toLowerCase().trim())){
                        val=getString(R.string.answer_ok);
                        db.validateEvaluation(id_user,id_subject,order_exercise,1);
                    }else{
                        val=getString(R.string.answer_bad)+" '"+ answers +"'";
                        db.validateEvaluation(id_user,id_subject,order_exercise,2);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getString(R.string.answer).toUpperCase())
                            .setMessage(val)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    next(exercises);
                                }
                            });

                    builder.create().show();

                }
            });
            btnOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.validateEvaluation(id_user,id_subject,order_exercise,0);
                    next(exercises);
                }
            });
        }


        return view;
    }

    public void speech( String txt, int status){
        if ( status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED )
        {
            Toast.makeText( context, "ERROR LANG_MISSING_DATA | LANG_NOT_SUPPORTED",
                    Toast.LENGTH_SHORT ).show();
            Intent install=new Intent();
            install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            context.startActivity(install);
        }else {

            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
            tts.setSpeechRate(0.0f);
            tts.setPitch(0.0f);
        }

    }

    public void next(Cursor exercises){
        if(exercises.moveToNext()){
            int type=exercises.getInt(exercises.getColumnIndex("type"));
            int position1= exercises.getPosition();
            Bundle args = new Bundle();
            args.putInt("position", position1);
            args.putInt("id_subject", id_subject);

            switch (type){
                case 1:
                    FragmentTypeOne fragmentTypeOne = new FragmentTypeOne();
                    fragmentTypeOne.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.content_exercise, fragmentTypeOne).commit();
                    break;
                case 2:
                    FragmentTypeTwo fragmentTypeTwo = new FragmentTypeTwo();
                    fragmentTypeTwo.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.content_exercise, fragmentTypeTwo).commit();
                    break;
                case 3:
                    FragmentTypeThree fragmentTypeThree = new FragmentTypeThree();
                    fragmentTypeThree.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.content_exercise, fragmentTypeThree).commit();
                    break;
                case 4:
                    FragmentTypeFour fragmentTypeFour = new FragmentTypeFour();
                    fragmentTypeFour.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.content_exercise, fragmentTypeFour).commit();
                    break;

            }

        }else {
            Bundle args = new Bundle();
            args.putInt("id_subject", id_subject);

            FragmentStaticsExercise fragment = new FragmentStaticsExercise();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.content_exercise, fragment).commit();


        }
    }


    @Override
    public void onInit(int status) {


    }

    @Override
    public void onStop() {
        tts.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        tts.shutdown();
        super.onDestroy();


    }
}
