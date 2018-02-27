package www.integratedlanguages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Plurilingual.*;



class AdapterUser extends ArrayAdapter<User> {

    AdapterUser(@NonNull Context context,  @NonNull ArrayList<User> objects) {
        super(context, 0,objects);
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_user, parent, false);

        final User user = getItem(position);
        TextView name=(TextView) convertView.findViewById(R.id.user_name);
        TextView userName=(TextView) convertView.findViewById(R.id.user_user);
        LinearLayout layout=(LinearLayout)convertView.findViewById(R.id.layout_user);

        name.setText(user.getName());
        userName.setText(user.getUser());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args=new Bundle();
                args.putString("user",user.getUser());
                FragmentLogin fragment=new FragmentLogin();
                fragment.setArguments(args);
                ((Activity)v.getContext()).getFragmentManager().beginTransaction().replace(R.id.content_login, fragment).commit();


            }
        });


        return convertView;
    }
}
