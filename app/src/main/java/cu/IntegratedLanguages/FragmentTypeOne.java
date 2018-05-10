package cu.IntegratedLanguages;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import android.widget.TextView;

import cu.DataBase.ConnectionDB;

public class FragmentTypeOne extends Fragment {
    private  int id_subject, position, order_exercise, id_user, answer;
    private String val;
    private  RadioButton op1, op2, op3, op4, op5;
    private Context context;
    private ConnectionDB db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_exercise_type_one, container,false);
        context=view.getContext();
        TextView question= (TextView) view.findViewById(R.id.question_select);



        op1=(RadioButton)view.findViewById(R.id.option_one);
        op2=(RadioButton)view.findViewById(R.id.option_two);
        op3=(RadioButton)view.findViewById(R.id.option_three);
        op4=(RadioButton)view.findViewById(R.id.option_four);
        op5=(RadioButton)view.findViewById(R.id.option_five);

        Button btnOk=(Button)view.findViewById(R.id.ok_btn);
        Button btnOut=(Button)view.findViewById(R.id.out_btn);


        position=(getArguments()!=null)?getArguments().getInt("position"):1;
        id_subject=(getArguments()!=null)?getArguments().getInt("id_subject"):1;


        db= new ConnectionDB(view.getContext());
        final Cursor exercises= db.getExercisesBySubject(id_subject+"");

        Cursor c=db.getUserLog();
        if(c.moveToFirst())id_user=c.getInt(c.getColumnIndex("id"));

        if(exercises.moveToPosition(position)){
            order_exercise=exercises.getInt(exercises.getColumnIndex("order_exercise"));


            //exercise
            Cursor ex=db.getExerciseById(exercises.getInt(0)+"",exercises.getInt(1)) ;
           answer=5;
            if(ex.moveToFirst()){


                question.setText(ex.getString(ex.getColumnIndex("question")));

                //option sentence
                Cursor sentence1= db.getSentenceById(ex.getString(ex.getColumnIndex("option1")));
                if(sentence1.moveToFirst()) op1.setText(sentence1.getString(sentence1.getColumnIndex("caption_sentence")));

                Cursor sentence2= db.getSentenceById(ex.getString(ex.getColumnIndex("option2")));
                if(sentence2.moveToFirst()) op2.setText(sentence2.getString(sentence2.getColumnIndex("caption_sentence")));

                Cursor sentence3= db.getSentenceById(ex.getString(ex.getColumnIndex("option3")));
                if(sentence3.moveToFirst()) op3.setText(sentence3.getString(sentence3.getColumnIndex("caption_sentence")));

                Cursor sentence4= db.getSentenceById(ex.getString(ex.getColumnIndex("option4")));
                if(sentence4.moveToFirst()) op4.setText(sentence4.getString(sentence4.getColumnIndex("caption_sentence")));

                op5.setText(ex.getString(ex.getColumnIndex("option5")));

                Cursor answers= db.getSentenceById(ex.getString(ex.getColumnIndex("answer")));
                if(answers.moveToFirst()){
                    int idAnswer=answers.getInt(answers.getColumnIndex("id"));
                    if(sentence1.getInt(0)==idAnswer)answer=1;
                    else if(sentence2.getInt(0)==idAnswer)answer=2;
                    else if(sentence3.getInt(0)==idAnswer)answer=3;
                    else answer=4;
                    val=answers.getString(answers.getColumnIndex("caption_sentence"));
                }

            }

        }


//button ok
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //answer
                boolean res=false;
                if(op1.isChecked() & answer==1)res=true;
                else if(op2.isChecked() & answer==2)res=true;
                else if(op3.isChecked() & answer==3)res=true;
                else if(op4.isChecked() & answer==4)res=true;
                else if(op5.isChecked() & answer==5)res=true;


                String answer="";
                if(res){
                   answer=getString(R.string.answer_ok);
                   db.validateEvaluation(id_user,id_subject, order_exercise,1);
                }else{
                     answer=getString(R.string.answer_bad)+" '"+ val +"'";
                    db.validateEvaluation(id_user,id_subject, order_exercise,2);

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getString(R.string.answer).toUpperCase())
                        .setMessage(answer)
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
