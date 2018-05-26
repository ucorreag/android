package cu.IntegratedLanguages;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import cu.Interface.DialogFragmentListener;
import cu.DataBase.ConnectionDB;


public class Plurilingual extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogFragmentListener {

    private ConnectionDB db;
    private int id_user;
    private String user;
    private Configuration systemConfig= new Configuration();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plurilingual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences sharedPreferences = getSharedPreferences("cu.integratedlanguages_preferences", MODE_PRIVATE);
        int theme = Integer.parseInt(sharedPreferences.getString("theme", 1+""));


        switch (theme){
            case 1: setTheme(R.style.AppTheme);
                break;
            case 2: setTheme(R.style.DarkAppTheme);
                break;
        }

        db = new ConnectionDB(this);


        //-start language
        Cursor c=db.getUserLog();
        String lang="ES";
        assert c != null;
        if(c.moveToFirst()){
            String lg=db.getLanguageById(c.getInt(c.getColumnIndex("id_language"))+"");
            if(!lg.equals(""))lang=lg;

            id_user=c.getInt(c.getColumnIndex("id"));
            user=c.getString(c.getColumnIndex("user"));
            //int history
            if(c.getInt(c.getColumnIndex("active"))!=1){
                db.setLastDate("last_date_open",user);
                db.setUserActive(id_user,1);
            }

            db.addEvaluation(id_user);
        }

        Locale locale= new Locale(lang.toLowerCase());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            systemConfig.setLocale(locale);
        }else{
            systemConfig.locale=locale;
        }

        getResources().updateConfiguration(systemConfig, null);

        //-end language-

        setTitle(getResources().getString(R.string.app_name));

        if (savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.content_pl, new FragmentHome()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    boolean canExitApp=false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{

             if (!canExitApp) {
                canExitApp = true;
                Toast.makeText(this, getString(R.string.app_back_pressed_exit), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        canExitApp = false;
                    }
                }, 2000);

                }else {

                    if(db.isUserActive(id_user)) {
                        history();
                    }
                     super.onBackPressed();
                     this.finishAffinity();


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.plurilingual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_lang:
                 //el 0 es spanish
                SelectLanguageApp dialogFragment = SelectLanguageApp.newInstance(0, getResources().getStringArray(R.array.language), getString(R.string.lang));
                        dialogFragment.show(getSupportFragmentManager(), SelectLanguageApp.class.getSimpleName());

                break;
            case R.id.action_settings:
                startActivity( new Intent(this, Preferences.class));
                break;
            case R.id.action_logout:
                Cursor c=db.getUserLog();

                if (c != null) {
                   c.moveToFirst();
                   String user=c.getString(c.getColumnIndex("user"));
                   db.setLoged(user,0);
                   startActivity( new Intent(this, Login.class));
                   this.finishAffinity();
                }

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_home){
            getFragmentManager().beginTransaction().replace(R.id.content_pl, new FragmentHome()).commit();
        }else if (id == R.id.nav_level_A1) {
            Bundle args = new Bundle();
            args.putInt("level", 1);
            FragmentSubject fragment = new FragmentSubject();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.content_pl, fragment).commit();
        }else if (id == R.id.nav_level_A2) {//enviar id
            Bundle args = new Bundle();
            args.putInt("level", 2);
            FragmentSubject fragment = new FragmentSubject();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.content_pl, fragment).commit();
        }else if (id == R.id.nav_level_B1) {//enviar id
        Bundle args = new Bundle();
        args.putInt("level", 3);
        FragmentSubject fragment = new FragmentSubject();
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.content_pl, fragment).commit();
    } else if (id == R.id.nav_level_B2) {//enviar id
        Bundle args = new Bundle();
        args.putInt("level", 4);
        FragmentSubject fragment = new FragmentSubject();
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.content_pl, fragment).commit();
    }

        else if (id == R.id.nav_config) {
            startActivity( new Intent(this, Preferences.class));
        }else if (id==R.id.nav_shared){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Rompe las barreras del habla con 'Plurilingual'" +
                    " \nhttp://atipaq.blogspot.com/");
            startActivity(Intent.createChooser(intent, "Compartir con"));
        }

        else if (id == R.id.nav_info) {
            Info info = new Info();
            info.show(getSupportFragmentManager(), Info.class.getSimpleName());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void getSelected(int index, String value) {
        String lang="EN";
        switch (index){
            case 0:
                lang="ES";
                break;
            case 1:
                lang="EN";
                break;
            case 2:
                lang="FR";
                break;
            case 3:
                lang="IT";
                break;
            case 4:
                lang="PT";

                break;
        }

        Cursor userLog=db.getUserLog();
        assert userLog != null;
        if(userLog.moveToFirst()){
            db.setLangConfigSystem(lang,userLog.getString(userLog.getColumnIndex("user")));
        }

        Toast.makeText(this,"Restart app",Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();



    }


    @Override
    protected void onDestroy() {
        if(db.isUserActive(id_user)) {
            history();

        }
        db.close();

        super.onDestroy();


    }

    public void history(){
            db.setLastDate("last_date_close",user);

            try{
                Date open=db.getLastDateOpen(user);
                Calendar calendar=Calendar.getInstance();

                Date close=db.getLastDateClose(user);

                int time= (int)((close.getTime() - open.getTime())/1000);//hours
                int days=0;
                int hours=0;
                int minutes=0;

                if(time>86400) {
                    days=(int)Math.floor(time/86400);
                    time=time-(days*86400);
                }
                if(time>3600) {
                    hours=(int)Math.floor(time/3600);
                    time=time-(hours*3600);
                }
                if(time>60) {
                    minutes=(int)Math.floor(time/60);
                    time=time-(minutes*60);
                }
                DecimalFormat df = new DecimalFormat("##0.0000");

                //minutes
                // double newTime=(time/3600.0)+(minutes/60.0)
                //        + hours+(days*24.0);

                double newTime=(time/60.0)+minutes
                        + (hours*60.0)+(days*24.0*60.0);


                //String value=df.format(newTime);


                db.addOrUpdateHistory(
                        id_user,
                        calendar.get(Calendar.WEEK_OF_YEAR),
                        calendar.get(Calendar.DAY_OF_WEEK),
                        close.getMonth()+1,
                        close.getYear()+1900,
                        newTime);


            }catch(Exception e){
                e.getStackTrace();
            }



        db.setUserActive(id_user,0);
    }


}
