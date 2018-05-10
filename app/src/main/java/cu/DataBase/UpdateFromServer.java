package cu.DataBase;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateFromServer {
    private static final String SERVER_URL="http://10.26.22.224/";

    public void updateLanguage(final Context context){
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
                            ConnectionDB db= new ConnectionDB(context);
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

    public void updateLevel(final Context context){
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
                            ConnectionDB db= new ConnectionDB(context);
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

    public void updateSubject(final Context context){
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
                            ConnectionDB db= new ConnectionDB(context);
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

    public void updateSubjectLanguage(final Context context){
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

                            ConnectionDB db= new ConnectionDB(context);
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

    public void updateSentence(final Context context){
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

                            ConnectionDB db= new ConnectionDB(context);
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


}
