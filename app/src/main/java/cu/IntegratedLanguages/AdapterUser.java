package cu.IntegratedLanguages;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import cu.DataBase.ConnectionDB;
import cu.Utile.Images;
import cu.Plurilingual.User;


class AdapterUser extends ArrayAdapter<User> {

    AdapterUser(@NonNull Context context, @NonNull ArrayList<User> objects) {
        super(context, 0, objects);
    }



    ConnectionDB db;
    MenuItem item1;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_user, parent, false);

        final User user = getItem(position);
        db = new ConnectionDB(this.getContext());
        TextView name = (TextView) convertView.findViewById(R.id.user_name);
        TextView userName = (TextView) convertView.findViewById(R.id.user_user);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_user);


        Images image = new Images();
        Drawable roundedDrawable = image.roundImage(convertView.getContext(), R.drawable.ic_action_user);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_user);
        imageView.setBackground(roundedDrawable);


        name.setText(user.getName());
        userName.setText(user.getUser());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("user", user.getUser());
                args.putString("name", user.getName());
                FragmentLogin fragment = new FragmentLogin();
                fragment.setArguments(args);
                ((Activity) v.getContext()).getFragmentManager().beginTransaction().replace(R.id.content_login, fragment).commit();


            }
        });

        convertView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.setHeaderTitle(getContext().getString(R.string.delete));
                //groupId, itemId, order, title
                item1 = menu.add(0, v.getId(), 0, "Eliminar " + user.getUser());
                item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (db.deleteUser(user.getUser()) != -1) {
                            Toast.makeText(getContext(), user.getUser() + " eliminado", Toast.LENGTH_SHORT).show();
                            ((Activity) getContext()).getFragmentManager().beginTransaction()
                                    .replace(R.id.content_login, new FragmentBaseTabs()).commit();

                        }
                        return true;
                    }
                });
            }
        });

        return convertView;
    }


}
