package cu.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Console;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


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
        db.execSQL("CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_language INTEGER DEFAULT 1," +
                "user TEXT, password TEXT, name TEXT," +
                "last_date_open TEXT, last_date_close TEXT," +
                "active INTEGER, log INTEGER, " +
                "CONSTRAINT id_language FOREIGN KEY (id_language) " +
                "REFERENCES language(id), CONSTRAINT user UNIQUE (user ASC))");

        //create Historial of use of app
        db.execSQL("CREATE TABLE history_app(id INTEGER PRIMARY KEY AUTOINCREMENT," + 
            "id_user INTEGER,week INTEGER, day INTEGER, month INTEGER, year INTEGER, quantity_hours TEXT," +
            "CONSTRAINT id_user FOREIGN KEY (id_user) REFERENCES user(id))");


       //create exercise

       //create a types of exercises that inherits of exercise
        //first type
        db.execSQL("CREATE TABLE exercise_first (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "id_subject INTEGER,"+
                "type INTEGER(1) DEFAULT 1,"+
                "order_exercise INTEGER,"+
                "question text, option1 INTEGER, option2 INTEGER,"+
                " option3 INTEGER, option4 INTEGER, option5 TEXT, answer INTEGER,"+
                "CONSTRAINT id_subject FOREIGN KEY (id_subject) REFERENCES subject (id),"+
                " CONSTRAINT option1 FOREIGN KEY (option1) REFERENCES sentence (id),"+
                " CONSTRAINT option2 FOREIGN KEY (option2) REFERENCES sentence (id),"+
                " CONSTRAINT option3 FOREIGN KEY (option3) REFERENCES sentence (id),"+
                " CONSTRAINT option4 FOREIGN KEY (option4) REFERENCES sentence (id),"+
                " CONSTRAINT answer FOREIGN KEY (answer) REFERENCES sentence (id))");

       //second type
        db.execSQL("CREATE TABLE exercise_second (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "id_subject INTEGER,"+
                "type INTEGER(1) DEFAULT 2,"+
                "order_exercise INTEGER,"+
                " question INTEGER, answer INTEGER,"+
                "CONSTRAINT id_subject FOREIGN KEY (id_subject) REFERENCES subject (id),"+
                " CONSTRAINT question FOREIGN KEY (question) REFERENCES sentence (id),"+
                " CONSTRAINT answer FOREIGN KEY (answer) REFERENCES sentence (id))") ;

        //third type
        db.execSQL("CREATE TABLE exercise_third (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "id_subject INTEGER,"+
                "type INTEGER(1) DEFAULT 3,"+
                "order_exercise INTEGER,"+
                "introduction TEXT,"+
                "question INTEGER,"+
                "CONSTRAINT id_subject FOREIGN KEY (id_subject) REFERENCES subject (id),"+
               " CONSTRAINT question FOREIGN KEY (question) REFERENCES sentence (id))");

        //fourth type
        db.execSQL("CREATE TABLE exercise_fourth (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "id_subject INTEGER,"+
                "type INTEGER(1) DEFAULT 4,"+
                "order_exercise INTEGER,"+
                " question INTEGER,"+
                "CONSTRAINT id_subject FOREIGN KEY (id_subject) REFERENCES subject (id),"+
                " CONSTRAINT question FOREIGN KEY (question) REFERENCES sentence (id))");


        //Evaluation
        db.execSQL("CREATE TABLE evaluation(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_subject INTEGER REFERENCES subject(id)," +
                "id_user INTEGER REFERENCES user(id)," +
                "order_exercise INTEGER," +
                "type INTEGER," +
                "evaluation INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //evaluation
    public void addEvaluation(int id_user){
        SQLiteDatabase db= getWritableDatabase();

        Cursor all=getReadableDatabase().rawQuery(
                "SELECT id, type, order_exercise, id_subject  FROM exercise_first" +
                        " UNION SELECT id, type, order_exercise, id_subject FROM exercise_second " +
                        " UNION SELECT id, type, order_exercise, id_subject  FROM exercise_third " +
                        " UNION SELECT id, type, order_exercise, id_subject  FROM exercise_fourth " +
                        " ORDER BY id_subject", new String[]{});

        while(all.moveToNext()) {
            Cursor c = getReadableDatabase().query("evaluation", null,
                    "id_user LIKE ? AND id_subject LIKE ? AND order_exercise LIKE ?",
                    new String[]{id_user + "",
                            all.getInt(all.getColumnIndex("id_subject")) + "",
                            all.getInt(all.getColumnIndex("order_exercise")) + ""},
                    null, null, null);
            if (!c.moveToFirst()) {


                ContentValues newEvaluation = new ContentValues();
                newEvaluation.put("id_user", id_user);
                newEvaluation.put("id_subject", all.getInt(all.getColumnIndex("id_subject")));
                newEvaluation.put("order_exercise", all.getInt(all.getColumnIndex("order_exercise")));
                newEvaluation.put("evaluation", 0);
                newEvaluation.put("type", all.getInt(all.getColumnIndex("type")));

                db.insert("evaluation", null, newEvaluation);


            }


        }


    }

    public int validateEvaluation(int id_user, int id_subject, int order_exercise, int evaluation){
        SQLiteDatabase db= getWritableDatabase();
        Cursor c = getReadableDatabase().query("evaluation", null,
                "id_user LIKE ? AND id_subject LIKE ? AND order_exercise LIKE ?",
                new String[]{id_user + "",
                        id_subject + "",
                        order_exercise + ""},
                null, null, null);
        if(c.moveToFirst()){
            ContentValues newEvaluation = new ContentValues();
            newEvaluation.put("evaluation", evaluation);
            return db.update("evaluation", newEvaluation, "id = '"+c.getInt(c.getColumnIndex("id"))+"'", null);


        }

        return -1;
    }

    public int getEvaluationSize(int id_user, int evaluation){
        Cursor c= getReadableDatabase().query("evaluation", null,
                "id_user LIKE ? AND evaluation LIKE ?",
                new String[]{id_user + "", evaluation+""},
                null, null, null);
        return c.getCount();

    }

    public int getEvaluationSizeBySubject(int id_user, int id_subject, int evaluation){
        Cursor c= getReadableDatabase().query("evaluation", null,
                "id_user LIKE ? AND id_subject LIKE ? AND evaluation LIKE ?",
                new String[]{id_user + "",id_subject+"", evaluation+""},
                null, null, null);
        return c.getCount();

    }

    public ArrayList<String[]>  getSugerencias(String iso){
        ArrayList<String[]> arr=new ArrayList<>();

        int id_user=1;
        Cursor user=getUserLog();
        if(user.moveToFirst())id_user=user.getInt(0);
        String all="select id_subject, count(id) as cantidad from evaluation " +
                "where id_user LIKE ? GROUP BY id_subject";
        Cursor c=getReadableDatabase().rawQuery(all,new String[]{id_user+""});

        while (c.moveToNext()){
            int id=c.getInt(c.getColumnIndex("id_subject"));
            Cursor desap=getReadableDatabase().query(
                    "evaluation",
                    null,
                    "id_subject LIKE ? AND id_user LIKE ? AND evaluation LIKE ?",
                    new String[]{id+"",id_user+"",2+""},
                    null,null,
                    null);

            Cursor k= getReadableDatabase().rawQuery("SELECT "+
                            " subject_language.caption_subject FROM subject" +
                            "  JOIN subject_language " +
                            "ON subject.id = subject_language.id_subject JOIN language " +
                            "ON subject_language.id_language=language.id WHERE " +
                            "subject_language.id_subject=? AND language.iso=? ORDER BY subject.id",
                    new String[]{id+"", iso+""});
            k.moveToFirst();

            if ((c.getInt(c.getColumnIndex("cantidad"))*0.30)<=desap.getCount()){
                arr.add(new String[]{"2",k.getString(0)});
            }
            else if((c.getInt(c.getColumnIndex("cantidad"))*0.10)<=desap.getCount()){

                arr.add(new String[]{"1",k.getString(0)});


            }


        }
        return arr;

    }


    //language upercase
    public Cursor getSubject(String idLevel, String languajeISO){
        return getReadableDatabase().rawQuery("SELECT subject.id," +
                " subject_language.caption_subject FROM level JOIN subject" +
                " ON level.id=subject.id_level JOIN subject_language " +
                "ON subject.id = subject_language.id_subject JOIN language " +
                "ON subject_language.id_language=language.id WHERE " +
                "level.id=? AND language.iso=? ORDER BY subject.id",
                new String[]{idLevel, languajeISO});

    }

    public Cursor getSubjectById(String id){
        Cursor c=getReadableDatabase().query(
                "subject",null,"id LIKE ?",new String[]{id},null,null,null);


        if(c.moveToFirst()){
            return c;
        }
        return null;
    }

    public Long addSubject(int id, int idLevel){
        SQLiteDatabase db= getWritableDatabase();

        if(getSubjectById(id+"")==null) {
            ContentValues newSubject = new ContentValues();
            newSubject.put("id", id);
            newSubject.put("id_level", idLevel);

            return db.insert("subject", null, newSubject);
        }
        return -1L;
    }

    public Cursor getSubjectLanguageById(String id){
        return getReadableDatabase().query(
                "subject_language",null,"id LIKE ?",
                new String[]{id},null,null,null);



    }

    public Long addSubjectLanguage(int id,int idSubject, int idLanguage, String caption){
        SQLiteDatabase db= getWritableDatabase();

        if(!getSubjectLanguageById(id+"").moveToFirst()) {
            ContentValues newSubjectLanguage = new ContentValues();
            newSubjectLanguage.put("id", id);
            newSubjectLanguage.put("id_subject", idSubject);
            newSubjectLanguage.put("id_language", idLanguage);
            newSubjectLanguage.put("caption_subject",caption);

            return db.insert("subject_language", null, newSubjectLanguage);
        }
        return -1L;

    }

    public Cursor getSentenceById(String id){
        return getReadableDatabase().query(
                "sentence",null,"id LIKE ?",new String[]{id},null,null,null);


    }

    public Long addSentence(int id,int idSubject, int idLanguage,int sequence, String caption){
        SQLiteDatabase db= getWritableDatabase();

        if(!getSentenceById(id+"").moveToFirst()) {
            ContentValues newSentence = new ContentValues();
            newSentence.put("id", id);
            newSentence.put("id_subject", idSubject);
            newSentence.put("id_language", idLanguage);
            newSentence.put("sequence",sequence);
            newSentence.put("caption_sentence",caption);

            return db.insert("sentence", null, newSentence);
        }
        return -1L;

    }

    public Cursor getSentencesBySequence(String idSubject, String sequence){
        return getReadableDatabase().query(
                "sentence",null,"id_subject LIKE ? and sequence LIKE ?",
                new String[]{idSubject, sequence},null,null,"sequence");

    }

    public Long addLanguage(int id, String caption, String iso){
        SQLiteDatabase db= getWritableDatabase();

        if(!getLanguageById(id+"").moveToFirst()) {

            ContentValues newLang = new ContentValues();
            newLang.put("id", id);
            newLang.put("caption_language", caption);
            newLang.put("iso", iso);

            return db.insert("language", null, newLang);
        }
        return -1L;
    }

    public Long addLevel(int id, String caption){
        SQLiteDatabase db= getWritableDatabase();

        if(getLevelById(id+"")==null) {

            ContentValues newLevel = new ContentValues();
            newLevel.put("id", id);
            newLevel.put("caption_level", caption);

            return db.insert("level", null, newLevel);
        }
        return -1L;
    }

    public Cursor getLanguageById(String id){
        return getReadableDatabase().query(
                "language",null,"id LIKE ?",
                new String[]{id},null,null,null);


    }

    public Cursor getLevelById(String id){
        Cursor c=getReadableDatabase().query(
                "level",null,"id LIKE ?",new String[]{id},null,null,null);

        if(c.moveToFirst())return c;
        return null;
    }

    @Nullable
    public Cursor getUserLog(){
        return getReadableDatabase().query(
                "user",null,"log LIKE 1",null,null,null,"id");
    }

    public int setUserActive(int id_user, int active){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("active",active);

        return db.update("user", values, "id LIKE ?",
                new String[]{id_user+""});

    }

    public boolean isUserActive(int id_user){
        Cursor c= getReadableDatabase().query(
                "user",null,"id LIKE ? AND active LIKE 1",
                new String[]{id_user+""},null,null,null);

        return c.moveToFirst();
    }

    public boolean isUserLoged(){
        Cursor c= getReadableDatabase().query(
                "user",null,"log LIKE 1",null,null,null,"id");

        return c.moveToFirst();
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
        return c.moveToFirst();

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

        return db.insert("user", null, newLog);


    }

    public int deleteUser(String user){
        SQLiteDatabase db= getWritableDatabase();

        return db.delete("user", "user = ?",new String[]{user});
    }

    public int setLangConfigSystem(String lang, String user){

        SQLiteDatabase db= getWritableDatabase();

        Cursor c= getReadableDatabase().query(
                "language",null,"iso LIKE ?",new String[]{lang},null,null,"id");

        ContentValues values = new ContentValues();

        if(c.moveToFirst()) {
            values.put("id_language", c.getInt(c.getColumnIndex("id")));
        }

        return db.update("user", values, "user = '"+user+"'", null);
    }

    public int setLastDate(String column, String user){
        SQLiteDatabase db= getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",new Locale("es"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String lastDateOpen =formatter.format(calendar.getTime());

        ContentValues values = new ContentValues();
        values.put(column,lastDateOpen );


        return db.update("user", values, "user = '"+user+"'", null);
    }

   public Date getLastDateOpen(String user)throws ParseException{
       Cursor c=getReadableDatabase().query(
               "user",null,"user LIKE ?",new String[]{user},null,null,null);


       String myDate = "2017/12/20 18:10:45";
       if(c.moveToFirst()){
           myDate=c.getString(c.getColumnIndex("last_date_open"));
       }

       SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",new Locale("es"));
       return sdf.parse(myDate);

   }

    public Date getLastDateClose(String user)throws ParseException{
        Cursor c=getReadableDatabase().query(
                "user",null,"user LIKE ?",new String[]{user},null,null,null);


        String myDate = "2017/12/20 18:10:45";
        if(c.moveToFirst()){
            myDate=c.getString(c.getColumnIndex("last_date_close"));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",new Locale("es"));
        return sdf.parse(myDate);
    }

    //exercises
    public Cursor getExercisesBySubject(String idSubject){
        return getReadableDatabase().rawQuery(
               "SELECT id, type, order_exercise  FROM exercise_first WHERE id_subject LIKE ?" +
               " UNION SELECT id, type, order_exercise FROM exercise_second WHERE id_subject LIKE ?" +
               " UNION SELECT id, type, order_exercise  FROM exercise_third WHERE id_subject LIKE ?" +
               " UNION SELECT id, type, order_exercise  FROM exercise_fourth WHERE id_subject LIKE ?" +
               " ORDER BY order_exercise", new String[]{idSubject, idSubject, idSubject, idSubject});

    }

    public Cursor getExerciseById(String id, int type){
        String table="";
        switch(type){
            case 1:
                table="exercise_first";
                break;
            case 2:
                table="exercise_second";
                break;
            case 3:
                table="exercise_third";
                break;
            case 4:
                table="exercise_fourth";
                break;
        }
        return getReadableDatabase().query(
                table,null,"id LIKE ?",new String[]{id},null,null,null);

    }

    public Long addExerciseTypeOne(ContentValues newExercise){
        Cursor cursor=getReadableDatabase().rawQuery("SELECT * FROM exercise_first " +
                "WHERE id LIKE ?", new String[]{newExercise.get("id").toString()});

        if(!cursor.moveToFirst()) {

            return getWritableDatabase().insert("exercise_first", null, newExercise);
        }

        return -1L;

    }
    public Long addExerciseTypeTwo(ContentValues newExercise){
        Cursor cursor=getReadableDatabase().rawQuery("SELECT * FROM exercise_second " +
                "WHERE id LIKE ?", new String[]{newExercise.get("id").toString()});

        if(!cursor.moveToFirst()) {

            return getWritableDatabase().insert("exercise_second", null, newExercise);
        }

        return -1L;

    }
    public Long addExerciseTypeThree(ContentValues newExercise){
        Cursor cursor=getReadableDatabase().rawQuery("SELECT * FROM exercise_third " +
                "WHERE id LIKE ?", new String[]{newExercise.get("id").toString()});

        if(!cursor.moveToFirst()) {

            return getWritableDatabase().insert("exercise_third", null, newExercise);
        }

        return -1L;

    }
    public Long addExerciseTypeFour(ContentValues newExercise){
        Cursor cursor=getReadableDatabase().rawQuery("SELECT * FROM exercise_fourth " +
                "WHERE id LIKE ?", new String[]{newExercise.get("id").toString()});

        if(!cursor.moveToFirst()) {

            return getWritableDatabase().insert("exercise_fourth", null, newExercise);
        }

        return -1L;

    }

    //add history app
    public boolean addOrUpdateHistory(int user, int week, int day, int month, int year, double quantity_hours){


        Cursor c=getReadableDatabase().query(
                "history_app",null,"id_user LIKE ? AND week LIKE ? AND day LIKE ? AND year LIKE ?",
                new String[]{user+"",week+"",day+"",year+""},null,null,null);

        SQLiteDatabase db= getWritableDatabase();

        if(c.moveToFirst()) {

            String hours=c.getString(c.getColumnIndex("quantity_hours" ));
            String newHours=(Double.parseDouble(hours)+quantity_hours)+"";

            ContentValues values = new ContentValues();
            values.put("quantity_hours", newHours);

            db.update("history_app", values,"id LIKE ?" ,new String[]{c.getInt(0)+""});
            return true;

        }else {

            try {
                ContentValues newHistory = new ContentValues();
                newHistory.put("id_user", user);
                newHistory.put("week", week);
                newHistory.put("day", day);
                newHistory.put("month", month);
                newHistory.put("year", year);
                newHistory.put("quantity_hours", quantity_hours+"");

                db.insert("history_app", null, newHistory);

                return true;

            } catch (Exception e) {
                e.getStackTrace();
            }
        }


        return false;


    }

    public float getHistoryByDay(int id_user, int week, int day, int year){
        Cursor c=getReadableDatabase().query(
                "history_app",null,
                "id_user LIKE ? AND week LIKE ? AND day LIKE ? AND year LIKE ?",
                new String[]{id_user+"", week+"",day+"", year+""},null,
                null,"day");
        if(c.moveToFirst()){

            String hours = c.getString(c.getColumnIndex("quantity_hours" ));
            //DecimalFormat df = new DecimalFormat("##0.0000");
            //String hoursFormat=df.format(Double.parseDouble(hours));
            return Float.parseFloat(hours);

        }
        return 0.0f;
    }

    public Cursor getSequence(String id_subject){
       return getReadableDatabase().rawQuery(
                "select sequence from sentence where id_subject LIKE ? GROUP BY sequence",
               new String[]{id_subject});

    }


}
