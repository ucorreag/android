package cu.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateFromServer {
    private static final String SERVER_URL="https://ucorreag1.000webhostapp.com/";
    private Context context;
    private ConnectionDB db;

    public UpdateFromServer(Context context, ConnectionDB db) {
        this.db=db;
        this.context = context;
    }

    public void updateLanguage(){
        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");
                            int id= data.getInt("id");
                            String caption= data.getString("caption_language");
                            String iso = data.getString("iso");
                            db.addLanguage(id,caption,iso);

                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getLanguage.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);

    }

    public void updateLevel(){
        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");
                            int id= data.getInt("id");
                            String caption= data.getString("caption_level");
                            db.addLevel(id,caption);


                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getLevel.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);
    }

    public void updateSubject(){
        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");
                            int id= data.getInt("id");
                            int idLevel= data.getInt("id_level");
                            db.addSubject(id,idLevel);


                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getSubject.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);
    }

    public void updateSubjectLanguage(){
        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");

                            int id= data.getInt("id");
                            int idSubject= data.getInt("id_subject");
                            int idLanguage= data.getInt("id_language");
                            String caption= data.getString("caption_subject");
                            db.addSubjectLanguage(id,idSubject, idLanguage,caption);



                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getSubjectLanguage.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);
    }

    public void updateSentence(){
        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");

                            int id= data.getInt("id");
                            int idSubject= data.getInt("id_subject");
                            int idLanguage= data.getInt("id_language");
                            int sequence= data.getInt("sequence");
                            String caption= data.getString("caption");
                            db.addSentence(id,idSubject, idLanguage,sequence,caption);


                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getSentence.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);
    }

    public void updateExerciseFirstType(){
        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");
                            ContentValues newData= new ContentValues();
                            newData.put("id", data.getInt("id"));
                            newData.put("id_subject", data.getInt("id_subject"));
                            newData.put("type", data.getInt("type"));
                            newData.put("order_exercise",data.getInt("order_exercise"));
                            newData.put("question",data.getString("question"));
                            newData.put("option1", data.getInt("option1"));
                            newData.put("option2", data.getInt("option2"));
                            newData.put("option3", data.getInt("option3"));
                            newData.put("option4",data.getInt("option4"));
                            newData.put("option5",data.getString("option5"));
                            newData.put("answer",data.getInt("answer"));

                            db.addExerciseTypeOne(newData);


                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getExerciseTypeOne.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);
    }

    public void updateExerciseSecondType(){
        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");
                            ContentValues newData = new ContentValues();
                            newData.put("id", data.getInt("id"));
                            newData.put("id_subject", data.getInt("id_subject"));
                            newData.put("type", data.getInt("type"));
                            newData.put("order_exercise",data.getInt("order_exercise"));
                            newData.put("question",data.getInt("question"));
                            newData.put("answer",data.getInt("answer"));
                            db.addExerciseTypeTwo(newData);


                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getExerciseTypeTwo.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);
    }

    public void updateExerciseThirdType(){
        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");
                            ContentValues newData = new ContentValues();
                            newData.put("id", data.getInt("id"));
                            newData.put("id_subject", data.getInt("id_subject"));
                            newData.put("type", data.getInt("type"));
                            newData.put("order_exercise",data.getInt("order_exercise"));
                            newData.put("introduction",data.getString("introduction"));
                            newData.put("question",data.getInt("question"));
                            db.addExerciseTypeThree(newData);


                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getExerciseTypeThree.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);
    }

    public void updateExerciseFourthType(){

        Response.Listener<String> responseListenLanguage= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    boolean success= jsonResponse.getBoolean("success");
                    if(success){
                        for(int i=0; i<(jsonResponse.length()-1); i++ ){
                            JSONObject data=jsonResponse.getJSONObject(i+"");
                            ContentValues newData = new ContentValues();
                            newData.put("id", data.getInt("id"));
                            newData.put("id_subject", data.getInt("id_subject"));
                            newData.put("type", data.getInt("type"));
                            newData.put("order_exercise",data.getInt("order_exercise"));
                            newData.put("question",data.getInt("question"));
                            db.addExerciseTypeFour(newData);


                        }

                    }else{
                        Toast.makeText(context,"data no found",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(context,"error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        };
        Listener update= new Listener(SERVER_URL+"getExerciseTypeFour.php",responseListenLanguage);
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(update);
    }




}
