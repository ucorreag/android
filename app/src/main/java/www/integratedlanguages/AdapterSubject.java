package www.integratedlanguages;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Plurilingual.Subject;

/**
 * @author Isai
 * Created by Isai on 7/01/2018.
 */

class AdapterSubject extends ArrayAdapter<Subject>{
    AdapterSubject(@NonNull Context context, ArrayList<Subject> subjects) {
        super(context,0, subjects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_subject, parent, false);

        TextView tvSubject= (TextView)convertView.findViewById(R.id.txt_subject);
        ImageButton sentences=(ImageButton)convertView.findViewById(R.id.sentences);
        //Button exercices=(Button)convertView.findViewById(R.id.exercise);
        LinearLayout layout1= (LinearLayout)convertView.findViewById(R.id.show_btn);
        final LinearLayout layout2= (LinearLayout)convertView.findViewById(R.id.hide_btn);



        final Subject subject = getItem(position);
        assert subject != null;
        tvSubject.setText(subject.getSubject());

        sentences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent =new Intent(getContext(),Sentences.class);
                intent.putExtra("id",subject.getId()+"");
                getContext().startActivity(intent);
            }
        });



       layout1.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               if(event.getAction()==MotionEvent.ACTION_DOWN){
                   if(layout2.getVisibility()==View.VISIBLE){
                       layout2.setVisibility(View.GONE);
                   }else {
                       layout2.setVisibility(View.VISIBLE);
                   }
               }
               return true;
           }
       });

        return convertView;
    }
}
