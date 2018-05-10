package cu.IntegratedLanguages;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cu.DataBase.ConnectionDB;
import cu.Utile.Images;

public class FragmentTypeTwo extends Fragment {

    private int position, id_subject, id_user, order_exercise;
    private EditText answer;
    private String answers;
    private Context context;
    private Cursor exercises;
    private ConnectionDB db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_exercise_type_two,container, false);

        context=view.getContext();
        ImageView imgLangQuestion=(ImageView)view.findViewById(R.id.img_lang_translate_question);
        TextView txtLangQuestion=(TextView)view.findViewById(R.id.txt_lang_translate_question);
        TextView question=(TextView)view.findViewById(R.id.question_translate);

        ImageView imgLangAnswer=(ImageView)view.findViewById(R.id.img_lang_translate_answer);
        TextView txtLangAnswer=(TextView)view.findViewById(R.id.txt_lang_translate_answer);
        answer=(EditText)view.findViewById(R.id.edit_translate);

        Button ok=(Button)view.findViewById(R.id.ok_btn_translate);
        Button out=(Button)view.findViewById(R.id.out_btn_translate);

        position=(getArguments()!=null)?getArguments().getInt("position"):1;
        id_subject=(getArguments()!=null)?getArguments().getInt("id_subject"):1;

        db= new ConnectionDB(view.getContext());
        exercises= db.getExercisesBySubject(id_subject+"");
        Cursor c=db.getUserLog();
        if(c.moveToFirst())id_user=c.getInt(c.getColumnIndex("id"));


        if(exercises.moveToPosition(position)){
            order_exercise=exercises.getInt(exercises.getColumnIndex("order_exercise"));

            Cursor ex=db.getExerciseById(exercises.getInt(0)+"",exercises.getInt(1)) ;


            if(ex.moveToFirst()) {

                Images images= new Images();
                Cursor sentence1= db.getSentenceById(ex.getString(ex.getColumnIndex("question")));
                if(sentence1.moveToFirst()){
                    String lang=db.getLanguageById(sentence1.getInt(sentence1.getColumnIndex("id_language"))+"");

                    //
                    Drawable dwq=images.getImgLang(view.getContext(),lang);
                    imgLangQuestion.setImageDrawable(dwq);
                    txtLangQuestion.setText(lang);
                    question.setText(sentence1.getString(sentence1.getColumnIndex("caption_sentence")));

                }
                Cursor sentence2= db.getSentenceById(ex.getString(ex.getColumnIndex("answer")));

                 if(sentence2.moveToFirst()){
                     answers=sentence2.getString(sentence2.getColumnIndex("caption_sentence"));

                     String lang1=db.getLanguageById(sentence2.getInt(sentence2.getColumnIndex("id_language"))+"");

                     Drawable dwa= images.getImgLang(view.getContext(),lang1);
                     imgLangAnswer.setImageDrawable(dwa);
                     txtLangAnswer.setText(lang1);
                 }

            }


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //answer
                    String answer1="";
                    if(answer.getText().toString().trim().toLowerCase().equals(answers.toLowerCase())){
                        answer1=getString(R.string.answer_ok);
                        db.validateEvaluation(id_user,id_subject,order_exercise,1);
                    }else{
                        answer1=getString(R.string.answer_bad)+" '"+ answers +"'";
                        db.validateEvaluation(id_user,id_subject,order_exercise,2);
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getString(R.string.answer).toUpperCase())
                            .setMessage(answer1)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    next(exercises);
                                }
                            });

                    builder.create().show();

                }
            });

            out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.validateEvaluation(id_user,id_subject,order_exercise,0);
                    next(exercises);
                }
            });


            }


        return view;
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

}
