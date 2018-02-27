package Utile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import Plurilingual.User;


public class ConnectionDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="language.db";

    public ConnectionDB(Context context) {
        super(context, context.getExternalFilesDir(null).getAbsolutePath()+
                "/"+DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create level
        db.execSQL("CREATE TABLE level(id integer primary key autoincrement, caption_level text)");
        //create language
        db.execSQL("CREATE TABLE language(id integer primary key autoincrement, caption_language text, iso text)");
        //create subject
        db.execSQL("CREATE TABLE subject(id integer primary key autoincrement, id_level integer ," +
                " CONSTRAINT id_level FOREIGN KEY (id_level) REFERENCES level(id))");
        //create sentence
        db.execSQL("CREATE TABLE sentence(id integer primary key autoincrement, id_language integer,id_subject integer," +
                "  sequence integer, caption_sentence text, CONSTRAINT id_subject FOREIGN KEY (id_subject) REFERENCES subject(id)," +
                "  CONSTRAINT id_language FOREIGN KEY (id_language) REFERENCES language(id))");
        //create subject_language
        db.execSQL("create table subject_language(id INTEGER PRIMARY KEY, id_subject INTEGER, id_language INTEGER," +
                " caption_subject TEXT, CONSTRAINT id_subject FOREIGN KEY (id_subject) REFERENCES subject(id), " +
                "CONSTRAINT id_language FOREIGN KEY (id_language) REFERENCES language(id))");
        //create user
        db.execSQL("CREATE TABLE user(id integer primary key autoincrement, user text, password TEXT, name text, last_date_open TEXT, " +
                "last_date_close TEXT, active INTEGER, log INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //language upercase
    public Cursor getSubject(String idLevel, String languajeISO){
        Cursor c=getReadableDatabase().rawQuery("SELECT subject.id," +
                " subject_language.caption_subject FROM level JOIN subject" +
                " ON level.id=subject.id_level JOIN subject_language " +
                "ON subject.id = subject_language.id_subject JOIN language " +
                "ON subject_language.id_language=language.id WHERE " +
                "level.id=? AND language.iso=? ORDER BY subject.id",
                new String[]{idLevel, languajeISO});
        return c;
    }



    public Cursor getSentencesBySequence(String idSubject, String sequence){
        Cursor c=getReadableDatabase().query(
                "sentence",null,"id_subject LIKE ? and sequence LIKE ?",new String[]{idSubject, sequence},null,null,"sequence");
        return c;
    }

    public String getLanguageById(String id){
        Cursor c=getReadableDatabase().query(
                "language",null,"id LIKE ?",new String[]{id},null,null,null);

        c.moveToFirst();
        return c.getString(c.getColumnIndex("iso")) ;
    }

    @Nullable
    public Cursor getUserLog(){
        return getReadableDatabase().query(
                "user",null,"log LIKE 1",null,null,null,"id");
    }

    public boolean isUserLoged(){
        Cursor c= getReadableDatabase().query(
                "user",null,"log LIKE 1",null,null,null,"id");

        if(c.moveToFirst()){
            return true;
        }
        return false;
    }

    public int setLoged(String user, int status){

        SQLiteDatabase db= getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("log",status);

        return db.update("user", values, "user = '"+user+"'", null);
    }


    public boolean existUser(String user, String password){
        Cursor c=getReadableDatabase().query(
                "user",null,"user LIKE ? AND password LIKE ?",new String []{user,password},null,null,"id");
        if(c.moveToFirst()){
            return true;
        }
        return false;
    }

    @Nullable
    public Cursor getUsers(){
        return getReadableDatabase().query(
                "user",null,null,null,null,null,"id");
    }


    public Long addUser(String name, String user, String password){
        SQLiteDatabase db= getWritableDatabase();

        ContentValues newLog = new ContentValues();
        newLog.put("user", user);
        newLog.put("password", password);
        newLog.put("name",name);

        Long in=db.insert("user", null, newLog);

        return in;
    }




}
