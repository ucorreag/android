package cu.IntegratedLanguages;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import cu.Plurilingual.Sentence;
import cu.Utile.Images;

import static android.content.Context.MODE_PRIVATE;


class AdapterSentences extends ArrayAdapter<Sentence>{


    private TextToSpeech tts;
    private String locale;
    private LinearLayout layout;
    private TextView lang;

    AdapterSentences(@NonNull Context context, ArrayList<Sentence> sentences, TextToSpeech tts) {
        super(context,0, sentences);
        this.tts=tts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_sentences, parent, false);


        TextView txtSentence=(TextView)convertView.findViewById(R.id.txt_sentences);
        ImageView img=(ImageView)convertView.findViewById(R.id.img_lang);
        lang=(TextView)convertView.findViewById(R.id.txt_lang);
        layout=(LinearLayout)convertView.findViewById(R.id.adapter_sentences);


        final Sentence sentence = getItem(position);

        //tts=new TextToSpeech(getContext(),this);

        assert sentence != null;
        txtSentence.setText(sentence.getSentence());

        locale=sentence.getLanguage();
        lang.setText(locale);
        Images images= new Images();
        Drawable image= images.getImgLang(this.getContext(),locale);
        img.setImageDrawable(image);


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tts.isSpeaking()){
                    //Locale loc=new Locale(sentence.getLanguage().toLowerCase());
                    //int result=TextToSpeech.SUCCESS;
                    //if(!loc.getDisplayLanguage().equals(Locale.getDefault().getDisplayLanguage())) {
                     //   result = tts.setLanguage(loc);
                    //}

                    int result=tts.setLanguage(new Locale(sentence.getLanguage()));
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(getContext(),"Listener not supported",Toast.LENGTH_SHORT).show();
                        Intent install=new Intent();
                        install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        getContext().startActivity(install);

                    }else {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cu.integratedlanguages_preferences", MODE_PRIVATE);
                        boolean swt = sharedPreferences.getBoolean("check_editar_audio", false);
                        float run = ((sharedPreferences.getInt("seekBar_velocidad", 0)-9)/10.0f)+1;
                        float tone = ((sharedPreferences.getInt("seekBar_tono", 0)-9)/10.0f)+1;


                        if(swt){
                            tts.setSpeechRate(run);
                            tts.setPitch(tone);
                        }
                       else {
                            tts.setSpeechRate(1.0f);
                            tts.setPitch(1.0f);
                        }

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


/**
    @Override
    public void onInit(int status) {
            tts.stop();
    }
*/




}
