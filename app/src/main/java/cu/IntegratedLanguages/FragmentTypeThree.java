package cu.IntegratedLanguages;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import cu.DataBase.ConnectionDB;
import cu.Utile.Images;

public class FragmentTypeThree extends Fragment {
   private Context context;
   private Button tv;
   private LinearLayout answer;
   private int position, id_subject, id_user, order_exercise;
   private Cursor exercises;
   private String[] answers;
   private ArrayList<String> data;
   private String valueAnswer;
    private boolean[] isSelect;
    private ConnectionDB db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_exercise_type_three,container,false);

        context=view.getContext();
        TextView info=(TextView)view.findViewById(R.id.info);
        answer= (LinearLayout) view.findViewById(R.id.question_complete);

        ImageView imgLangQuestion=(ImageView)view.findViewById(R.id.img_lang_complete_question);
        TextView txtLangQuestion=(TextView)view.findViewById(R.id.txt_lang_complete_question);
        LinearLayout layout=(LinearLayout)view.findViewById(R.id.layout_question);

        Button btnOk=(Button)view.findViewById(R.id.ok_btn_complete);
        Button btnOut=(Button)view.findViewById(R.id.out_btn_complete);

        position=(getArguments()!=null)?getArguments().getInt("position"):1;
        id_subject=(getArguments()!=null)?getArguments().getInt("id_subject"):1;


        db= new ConnectionDB(view.getContext());
        exercises= db.getExercisesBySubject(id_subject+"");

        Cursor c=db.getUserLog();
        if(c.moveToFirst())id_user=c.getInt(c.getColumnIndex("id"));
        
        data=new ArrayList<>();

        if(exercises.moveToPosition(position)) {
            order_exercise=exercises.getInt(exercises.getColumnIndex("order_exercise"));
            //exercise
            Cursor ex = db.getExerciseById(exercises.getInt(0) + "", exercises.getInt(1));

            if (ex.moveToFirst()) {
                //option sentence

                info.setText(ex.getString(ex.getColumnIndex("introduction")));


                Cursor sentence1 = db.getSentenceById(ex.getString(ex.getColumnIndex("question")));
                if (sentence1.moveToFirst()) {

                    String lang = db.getLanguageById(sentence1.getInt(sentence1.getColumnIndex("id_language")) + "");

                    Images images = new Images();
                    Drawable dwq = images.getImgLang(view.getContext(), lang);
                    imgLangQuestion.setImageDrawable(dwq);
                    txtLangQuestion.setText(lang);

                    valueAnswer = sentence1.getString(sentence1.getColumnIndex("caption_sentence")).trim();
                    answers=valueAnswer.split(" ");

                    isSelect=new boolean[answers.length];
                    //put in panel
                    int j=1;

                    layout.removeAllViews();
                    LinearLayout layout1=new LinearLayout(context);
                    layout1.setOrientation(LinearLayout.HORIZONTAL);
                    layout1.setGravity(Gravity.CENTER);


                        for (int i = 0; i < answers.length; i++) {
                           tv = new Button(context);

                           int index=getIndex(answers.length);

                            tv.setText(answers[index]);


                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TextView txt=new TextView(context);
                                    txt.setPadding(5,5,5,5);
                                    txt.setBackgroundColor(getResources().getColor(R.color.md_grey_350));
                                    final String value=((Button)v).getText().toString();
                                    txt.setText(value);
                                    v.setVisibility(View.INVISIBLE);
                                   
                                    data.add(value);
                                    
                                    final View k=v;

                                    txt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            v.setVisibility(View.GONE);
                                            data.remove(value);
                                            k.setVisibility(View.VISIBLE);
                                        }
                                    });

                                     answer.addView(txt);
                                }
                            });

                            layout1.addView(tv);

                            if (j == 3) {
                                layout.addView(layout1);

                                layout1 = new LinearLayout(context);
                                layout1.setOrientation(LinearLayout.HORIZONTAL);
                                layout1.setGravity(Gravity.CENTER);
                                j = 0;
                            }else if(i==answers.length-1){
                                layout.addView(layout1);
                            }
                            j++;
                        }




                }
                
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       String value="";
                        for (String txt:data) {
                          value+=txt+" ";

                        }

                        String answer="";
                        if(valueAnswer.trim().toLowerCase().equals(value.trim().toLowerCase())){

                           answer=getString(R.string.answer_ok);
                           db.validateEvaluation(id_user,id_subject,order_exercise,1);

                        }else{
                            answer=getString(R.string.answer_bad)+" '"+valueAnswer.trim()+"'";
                            db.validateEvaluation(id_user,id_subject,order_exercise,2);
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
                
                
                
                
            }
        }



        return  view;
    }


    public void next(Cursor exercises) {
        if (exercises.moveToNext()) {
            int type = exercises.getInt(exercises.getColumnIndex("type"));
            int position1 = exercises.getPosition();
            Bundle args = new Bundle();
            args.putInt("position", position1);
            args.putInt("id_subject", id_subject);

            switch (type) {
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


    private int getIndex(int length){
        int k=(int)(Math.random()*length);
        if(isSelect[k]) return getIndex(length);
        isSelect[k]=true;
        return k;
    }

}
