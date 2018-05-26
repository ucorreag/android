package cu.DataBase;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

 class Listener extends StringRequest {


     Listener(String url, Response.Listener<String> listener){
        super(Method.GET, url,listener, null);

    }

}
