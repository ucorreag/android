package cu.Utile;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import cu.DataBase.ConnectionDB;
import cu.IntegratedLanguages.R;

public class Images {

    /*round image*/
    public Drawable roundImage(Context context, int idOfImage){
        Drawable originalDrawable = context.getResources().getDrawable(idOfImage);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(context.getResources(), originalBitmap);
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());
        return roundedDrawable;
    }

    public Drawable getImgLang(Context context, String lang){
        lang=lang.toUpperCase();
        Drawable dw=null;
        switch (lang){
            case "ES":
                dw= context.getResources().getDrawable(R.drawable.es);
                break;
            case "EN":
                dw= context.getResources().getDrawable(R.drawable.en);
                break;
            case "FR":
                dw= context.getResources().getDrawable(R.drawable.fr);
                break;
            case "IT":
                dw= context.getResources().getDrawable(R.drawable.it);
                break;
            case "PT":
                dw= context.getResources().getDrawable(R.drawable.pt);
                break;
        }
        return  dw;
    }

}
