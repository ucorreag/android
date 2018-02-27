package www.integratedlanguages;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import Interface.DialogFragmentListener;
import Utile.ConnectionDB;


public class Plurilingual extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogFragmentListener {

    private ConnectionDB db;
    private Configuration systemConfig= new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plurilingual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new ConnectionDB(this);

        if (savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.content_pl, new FragmentHome()).commit();

        }


        //Toast.makeText( this,getResources().getConfiguration().locale.getLanguage(), Toast.LENGTH_SHORT ).show();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

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
                   finish();
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
        }else if (id == R.id.nav_level_one) {
            getFragmentManager().beginTransaction().replace(R.id.content_pl, new FragmentSubject()).commit();
        } else if (id == R.id.nav_config) {
            startActivity( new Intent(this, Preferences.class));
        } else if (id == R.id.nav_help) {
            Help help = new Help();
            help.show(getSupportFragmentManager(), Help.class.getSimpleName());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void getSelected(int index, String value) {

        switch (index){
            case 0:
                Locale locale= new Locale("es");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    systemConfig.setLocale(locale);
                }else{
                    systemConfig.locale=locale;
                }
                break;
            case 1:
                Locale locale1= new Locale("en");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    systemConfig.setLocale(locale1);
                }else{
                    systemConfig.locale=locale1;
                }
                break;
            case 2:
                Locale locale2= new Locale("fr");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    systemConfig.setLocale(locale2);
                }else{
                    systemConfig.locale=locale2;
                }
                break;
            case 3:
                Locale locale3= new Locale("it");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    systemConfig.setLocale(locale3);
                }else{
                    systemConfig.locale=locale3;
                }
                break;
            case 4:
                Locale locale4= new Locale("pt");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    systemConfig.setLocale(locale4);
                }else{
                    systemConfig.locale=locale4;
                }
                break;
        }

        getResources().updateConfiguration(systemConfig, null);
        Intent refresh = new Intent(Plurilingual.this, Plurilingual.class);
        startActivity(refresh);
        finish();

    }
}
