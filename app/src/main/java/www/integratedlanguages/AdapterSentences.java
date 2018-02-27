package www.integratedlanguages;


import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import Plurilingual.Sentence;


class AdapterSentences extends ArrayAdapter<Sentence> implements
        TextToSpeech.OnInitListener {


    private TextToSpeech tts;

    AdapterSentences(@NonNull Context context, ArrayList<Sentence> sentences) {
        super(context,0, sentences);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_sentences, parent, false);


        TextView txtSentence=(TextView)convertView.findViewById(R.id.txt_sentences);
        ImageView img=(ImageView)convertView.findViewById(R.id.img_lang);
        TextView lang=(TextView)convertView.findViewById(R.id.txt_lang);
        final LinearLayout layout=(LinearLayout)convertView.findViewById(R.id.adapter_sentences);


        final Sentence sentence = getItem(position);

        tts=new TextToSpeech(getContext(),this);

        assert sentence != null;
        txtSentence.setText(sentence.getSentence());



        String locale=sentence.getLanguage();
        lang.setText(locale);

        switch (locale){
            case "ES":
                img.setImageResource(R.drawable.es);
                break;
            case "EN":
                img.setImageResource(R.drawable.en);
                break;
            case "FR":
                img.setImageResource(R.drawable.fr);
                break;
            case "IT":
                img.setImageResource(R.drawable.it);
                break;
            case "PT":
                img.setImageResource(R.drawable.pt);
                break;
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tts.isSpeaking()){
                    Locale loc=new Locale(sentence.getLanguage().toLowerCase());
                    int result=TextToSpeech.SUCCESS;
                    if(!loc.getDisplayLanguage().equals(Locale.getDefault().getDisplayLanguage())) {
                        result = tts.setLanguage(loc);
                    }

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(getContext(),"Language not supported",Toast.LENGTH_SHORT).show();
                        Intent install=new Intent();
                        install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        getContext().startActivity(install);

                    }else {
                        tts.speak(sentence.getSentence(), TextToSpeech.QUEUE_FLUSH, null);
                        tts.setLanguage(Locale.getDefault());
                    }

                }else{
                    tts.stop();
                    onClick(v);
                }
            }
        });




        return convertView;
    }



    @Override
    public void onInit(int status) {

    }



}
